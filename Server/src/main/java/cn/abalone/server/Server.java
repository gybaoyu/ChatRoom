package cn.abalone.server;

import cn.abalone.server.service.ServerMessageService;
import lombok.SneakyThrows;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.socket.config.annotation.EnableWebSocket;

public class Server {
    public static void main(String[] args) {
        new ServerMessageService();
    }
}
