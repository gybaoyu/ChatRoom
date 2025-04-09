package cn.abalone.client;

import cn.abalone.client.service.MessageReceiver;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.socket.config.annotation.EnableWebSocket;

import java.io.IOException;

@SpringBootApplication
@EnableWebSocket
public class Client {
    public static void main(String[] args) throws IOException {
        SpringApplication.run(Client.class, args);
    }
}
