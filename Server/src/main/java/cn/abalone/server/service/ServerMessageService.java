package cn.abalone.server.service;

import cn.abalone.server.entity.Message;
import cn.abalone.server.entity.User;
import jakarta.annotation.PostConstruct;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.List;

import static cn.abalone.server.tools.Tools.*;

public class ServerMessageService {
    private final List<User> users = new ArrayList<>();
    private DatagramSocket serverSocket;

    //register
    private boolean isContain(String name) {
        for (User user : users) {
            if (user.getName().equals(name)) {
                return true;
            }
        }
        return name.equals("Server");
    }
    @SneakyThrows
    private void userNameWarning(InetAddress address, int port) {
        Message message = new Message("Server", "用户名已被占用或用户名不合法");
        String jsonData = messageToJson(message);
        serverSocket.send(new DatagramPacket(jsonData.getBytes(), jsonData.getBytes().length, new InetSocketAddress(address, port)));
    }
    @SneakyThrows
    private void userRegisterSuccess(String name) {
        Message message = new Message("Server", name + "加入了聊天室");
        message.setMsgDate(getTime());
        String jsonData = messageToJson(message);
        System.out.println(jsonData);
        for (User user : users) {
            serverSocket.send(new DatagramPacket(jsonData.getBytes(), jsonData.getBytes().length, new InetSocketAddress(user.getAddress(), user.getPort())));
        }
    }
    private void register(Message msg, DatagramPacket packet) {
        if (isContain(msg.getName())) {
            userNameWarning(packet.getAddress(), packet.getPort());
        } else {
            User user = new User(msg.getName(), packet.getAddress(), packet.getPort());
            users.add(user);
            userRegisterSuccess(user.getName());
        }
    }
    //register

    //normalSend
    @SneakyThrows
    private void normalSend(Message msg){
        String jsonData = messageToJson(msg);
        for (User user : users) {
            if (!user.getName().equals(msg.getName())){
                serverSocket.send(new DatagramPacket(jsonData.getBytes(), jsonData.getBytes().length, new InetSocketAddress(user.getAddress(), user.getPort())));
            }else{
                Message tmp = new Message("Server","您已离开聊天室",getTime());
                if (msg.getMessage().equals("exit"))serverSocket.send(new DatagramPacket(messageToJson(tmp).getBytes(), messageToJson(tmp).getBytes().length, new InetSocketAddress(user.getAddress(), user.getPort())));
            }
        }
    }
    //normalSend

    @SneakyThrows
    public void initServer() {
        System.out.println("服务器启动");
        serverSocket = new DatagramSocket(6666);
        System.out.println(serverSocket.getLocalPort());
        while (true) {
            DatagramPacket packet = new DatagramPacket(new byte[1024], 1024);
            serverSocket.receive(packet);
            String rawData = new String(packet.getData(), 0, packet.getLength());
            System.out.println(rawData);
            Message msg = jsonToMessage(rawData);
            if (msg.getMessage().equals("/register/")) {
                register(msg, packet);
            } else if (msg.getMessage().equals("exit")){
                normalSend(new Message("Server",msg.getName()+"离开了聊天室"));
                for (User user : users) {
                    if (user.getName().equals(msg.getName())) {
                        users.remove(user);
                        break;
                    }
                }
            }else{
                normalSend(msg);
            }
        }
    }
    public ServerMessageService(){
        this.initServer();
    }
}
