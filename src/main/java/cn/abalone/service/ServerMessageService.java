package cn.abalone.service;

import cn.abalone.entity.Message;
import cn.abalone.entity.User;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.ArrayList;
import java.util.List;

import static cn.abalone.service.Tools.*;

public class ServerMessageService {
    private List<Message> messages = new ArrayList<>();
    private List<User> users = new ArrayList<>();
    private DatagramSocket serverSocket;


    private void sendMsg(String msg, String name) throws IOException {
        for (User user : users) {
            if (!user.getName().equals(name)) {
                DatagramPacket packet = new DatagramPacket(msg.getBytes(), msg.getBytes().length, user.getHost(), user.getPort());
                serverSocket.send(packet);
            }
        }
    }

    private boolean contains(User user) {
        for (User u : users) {
            if (u.getName().equals(user.getName())) {
                return true;
            }
        }
        return false;
    }

    void someOneJoined(User user) throws IOException {
        String str = packMessage(new Message("您已进入聊天室", user));
        DatagramPacket packet = new DatagramPacket(str.getBytes(), str.getBytes().length, user.getHost(), user.getPort());
        serverSocket.send(packet);
        sendMsg(packMessageWithoutHost(new Message("进入了聊天室(系统提示)", user)), user.getName());
        users.add(user);
        getHistoryMessages(user);
    }

    public void initServer() throws IOException {
        System.out.println("服务器启动");
        serverSocket = new DatagramSocket(serverPort);
        while (true) {
            DatagramPacket packet = new DatagramPacket(new byte[1024], 1024);
            serverSocket.receive(packet);
            String rawData = new String(packet.getData(), 0, packet.getLength());
            Message msg = unpackMessageWithoutHost(rawData);
            User user = msg.getUser();
            user.setHost(packet.getAddress());
            user.setPort(packet.getPort());
            if (msg.getMsg().equals("/register/")) {
                System.out.print(packet.getAddress() + ":" + packet.getPort() + " 申请注册用户名: " + user.getName() + " ");
                if (contains(user)) {
                    String str = "当前用户名已被占用";
                    serverSocket.send(new DatagramPacket(str.getBytes(), str.getBytes().length, user.getHost(), user.getPort()));
                    System.out.println(packet.getAddress() + ":" + packet.getPort() + "注册失败,该用户名已被占用");
                } else {
                    System.out.println(packet.getAddress() + ":" + packet.getPort() + "注册成功");
                    someOneJoined(user);
                }
            } else if (msg.getMsg().equals("Exit")) {
                System.out.println("[" + msg.getTime() + "] [" + user.getHost() + ":" + user.getPort() + "] " + user.getName() + "输入了退出指令");
                sendMsg(packMessageWithoutHost(new Message("离开了聊天室(系统提示)", user)), user.getName());
                serverSocket.send(new DatagramPacket(rawData.getBytes(), rawData.getBytes().length, user.getHost(), user.getPort()));
                users.remove(user);
                System.out.println("已将" + user.getName() + "从用户名单中移除");
            } else {
                System.out.println("[" + msg.getTime() + "] [" + user.getHost() + ":" + user.getPort() + "] " + user.getName() + ": " + msg.getMsg());
                messages.add(msg);
                sendMsg(rawData, user.getName());
            }
        }
    }

    public void getHistoryMessages(User user) throws IOException {
        String str;
        DatagramPacket packet;
        for (Message message : messages) {
            str = packMessageWithoutHost(message);
            packet = new DatagramPacket(str.getBytes(), str.getBytes().length, user.getHost(), user.getPort());
            serverSocket.send(packet);
        }
    }
}
