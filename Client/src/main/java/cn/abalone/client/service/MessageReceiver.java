package cn.abalone.client.service;

import cn.abalone.client.controller.WebSocketHandler;
import cn.abalone.client.entity.Message;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.net.DatagramPacket;
import java.net.DatagramSocket;

import static cn.abalone.client.tools.JsonTools.jsonToMessage;
import static cn.abalone.client.tools.JsonTools.messageToJson;

@Service
public class MessageReceiver implements Runnable{

    private final MessageSender sender;
    private final DatagramSocket socket;
    private WebSocketSession webSocketSession;

    public MessageReceiver(MessageSender sender){
        this.sender = sender;
        this.socket = sender.getSocket();
        Thread thread = new Thread(this);
        thread.start();
    }

    @SneakyThrows
    @Override
    public void run() {
        System.out.println("Receiver is running...");
        DatagramPacket packet = new DatagramPacket(new byte[1024], 1024);
        while (true){
            socket.receive(packet);
            for (WebSocketSession session : WebSocketHandler.getSessionMap().values()) {
                if (session.isOpen()) {
                    webSocketSession = session;
                    break;
                }
            }
            String rawData = new String(packet.getData(), 0, packet.getLength());
            Message message = jsonToMessage(rawData);
            System.out.println(rawData);
            webSocketSession.sendMessage(new TextMessage(rawData));
            if (message.getMessage().equals("Exit")) {
                webSocketSession.close();
                break;
            }
        }
    }
}
