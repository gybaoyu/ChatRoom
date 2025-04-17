package cn.abalone.client.service;

import cn.abalone.client.controller.WebSocketHandler;
import cn.abalone.client.entity.Message;
import jakarta.annotation.PostConstruct;
import lombok.Getter;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

import static cn.abalone.client.tools.JsonTools.messageToJson;

@Service
public class MessageSender {
    @Value("${app.serverPort}")
    private int serverPort;
    @Value("${app.serverHost}")
    private String serverHost;
    @Getter
    private final DatagramSocket socket;

    public MessageSender() throws SocketException {
        System.out.println("Sender is running...");
        socket = new DatagramSocket();
    }
    @SneakyThrows
    public String sendMessage(Message message){
        String jsonData = messageToJson(message);
        socket.send(new DatagramPacket(jsonData.getBytes(), jsonData.getBytes().length, InetAddress.getByName(serverHost), serverPort));
        return jsonData;
    }
}
