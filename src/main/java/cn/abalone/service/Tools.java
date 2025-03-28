package cn.abalone.service;

import cn.abalone.entity.Message;
import cn.abalone.entity.User;
import lombok.SneakyThrows;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.net.UnknownHostException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;

public class Tools {

    public static final InetAddress serverHost;
    public static final int serverPort = 6666;

    public static String getTime() {
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern("MM-dd HH:mm:ss"));
    }

    @SneakyThrows
    public static Message unpackMessage(String msg) {
        String[] str = msg.split("_", 5);
        String usrHost = str[0], usrName = str[2], time = str[3], message = str[4];
        int port = Integer.parseInt(str[1]);
        return new Message(message, time, new User(usrName,InetAddress.getByName(usrHost),port));
    }
    @SneakyThrows
    public static Message unpackMessageWithoutHost(String msg) {
        String[] str = msg.split("_", 3);
        String usrName = str[0], time = str[1], message = str[2];
        return new Message(message, time, new User(usrName));
    }

    public static String packMessage(Message msg) {
        User  u = msg.getUser();
        return u.getHost()+"_"+u.getPort()+"_"+u.getName()+"_"+ msg.getTime() + "_" + msg.getMsg();
    }

    public static String packMessageWithoutHost(Message msg) {
        return msg.getUser().getName()+"_"+ msg.getTime() + "_" + msg.getMsg();
    }

    static {
        try {
            serverHost = InetAddress.getByName("47.107.253.223");
//            serverHost = InetAddress.getByName("localhost");
        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        }
    }
}
