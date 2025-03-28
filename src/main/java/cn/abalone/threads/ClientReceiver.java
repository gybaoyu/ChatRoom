package cn.abalone.threads;

import cn.abalone.entity.Message;
import cn.abalone.entity.User;
import cn.abalone.service.ClientGUIService;
import lombok.SneakyThrows;

import java.net.DatagramPacket;
import java.net.DatagramSocket;

import static cn.abalone.service.Tools.unpackMessageWithoutHost;

public class ClientReceiver implements Runnable {
    private DatagramSocket socket;
    private boolean running;
    private User user;
    private ClientGUIService guiService;

    public ClientReceiver(DatagramSocket socket, User user,ClientGUIService guiService) {
        this.socket = socket;
        this.running = true;
        this.user = user;
        this.guiService = guiService;
    }

    public void stop() {
        this.running = false;
    }

    @SneakyThrows
    @Override
    public void run() {
        DatagramPacket packet = new DatagramPacket(new byte[1024], 1024);
        while (running) {
            socket.receive(packet);
            Message message = unpackMessageWithoutHost(new String(packet.getData(), 0, packet.getLength()));
            if (!message.getMsg().equals("Exit")) {
                guiService.addMessage("[" + message.getTime() + "] " + message.getUser().getName() + ": " + message.getMsg());
//                System.out.println("[" + message.getTime() + "] " + message.getUser().getName() + ": " + message.getMsg());
            } else {
                running = false;
                guiService.addMessage("ClientReceiver已关闭");
                guiService.addMessage("您已退出聊天室");
            }
        }
    }
}
