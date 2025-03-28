package cn.abalone.threads;

import cn.abalone.entity.Message;
import cn.abalone.entity.User;
import cn.abalone.service.ClientGUIService;
import lombok.SneakyThrows;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.Scanner;

import static cn.abalone.service.Tools.*;

public class ClientSender implements Runnable {
    private DatagramSocket socket;
    private boolean running;
    private User user;
    private ClientGUIService clientGUIService;
    public ClientSender(DatagramSocket socket, User user, ClientGUIService clientGUIService) {
        this.socket = socket;
        this.running = true;
        this.user = user;
        this.clientGUIService = clientGUIService;
    }

    public void stop() {
        this.running = false;
    }

    @SneakyThrows
    @Override
    public void run() {
        while (running) {
            String input = clientGUIService.getNextInput();
            String msg = packMessageWithoutHost(new Message(input, user));
            socket.send(new DatagramPacket(msg.getBytes(), msg.getBytes().length, serverHost, serverPort));
            if (!input.equals("Exit")) {
                clientGUIService.addMessage("[" + getTime() + "] " + user.getName() + ": " + input);
            } else {
                clientGUIService.addMessage("ClientSender已关闭...");
                running = false;
            }
        }
    }
}
