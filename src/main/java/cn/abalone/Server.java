package cn.abalone;

import cn.abalone.service.ServerMessageService;

import java.io.IOException;

public class Server {
    public static void main(String[] args) throws IOException {
        ServerMessageService service = new ServerMessageService();
        service.initServer();
    }
}
