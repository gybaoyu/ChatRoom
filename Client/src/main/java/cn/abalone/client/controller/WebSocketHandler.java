package cn.abalone.client.controller;

import cn.abalone.client.entity.Message;
import cn.abalone.client.service.MessageSender;
import lombok.Getter;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.concurrent.ConcurrentHashMap;

import static cn.abalone.client.tools.JsonTools.jsonToMessage;
import static cn.abalone.client.tools.Tools.getTime;

@Component
public class WebSocketHandler extends TextWebSocketHandler {
    @Getter
    private static ConcurrentHashMap<String, WebSocketSession> sessionMap = new ConcurrentHashMap<>();

    private final MessageSender messageSender;

    @Autowired
    public WebSocketHandler(MessageSender messageSender) {
        this.messageSender = messageSender;
    }
    @SneakyThrows
    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        sessionMap.put(session.getId(), session);
        System.out.println("连接成功");
        System.out.println("session: "+session.getId());
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        sessionMap.remove(session.getId());
        session.close();
        System.out.println("连接关闭");
        System.out.println("session: "+session.getId());
    }

    @SneakyThrows
    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage textMessage) {
        // 1. 接收前端消息
        String payload = textMessage.getPayload();
        System.out.println("收到前端消息: " + payload);
        // 2. 将消息转换为Message对象（假设前端发送的是JSON格式的消息）
        Message message = jsonToMessage(payload);
        message.setMsgDate(getTime());
        session.sendMessage(new TextMessage(messageSender.sendMessage(message)));
    }
}