package cn.abalone.service;

import cn.abalone.entity.Message;
import cn.abalone.entity.User;
import cn.abalone.threads.ClientReceiver;
import cn.abalone.threads.ClientSender;

import javax.swing.*;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

import static cn.abalone.service.Tools.*;

public class ClientMessageService {
    private DatagramSocket socket;
    private ClientReceiver receiver;
    private ClientSender sender;
    private ClientGUIService guiService;
    public boolean testUserName(User user) throws IOException {
        String str = packMessageWithoutHost(new Message("/register/",user));
        socket.send(new DatagramPacket(str.getBytes(),str.getBytes().length,serverHost,serverPort));
        DatagramPacket packet = new DatagramPacket(new byte[1024], 1024);
        socket.receive(packet);
        str = new String(packet.getData(),0,packet.getLength());
        return !str.equals("当前用户名已被占用");
    }
    public void initClient() throws IOException {
        socket = new DatagramSocket();
        guiService = new ClientGUIService();
        User user = new User();
        String str = "输入你的聊天室昵称：";
        while(true){
            String name = JOptionPane.showInputDialog(str);
            user.setName(name);
            if (testUserName(user)) break;
            else str="当前用户名已被占用，请换一个: ";
        }
        guiService.createGUI();
        guiService.addMessage("成功连接到服务器,输入Exit退出聊天室");
        guiService.addMessage("开始加载聊天记录(如果有)");

        sender =  new ClientSender(socket,user,guiService);
        Thread senderThread = new Thread(sender);
        senderThread.start();

        receiver = new ClientReceiver(socket,user,guiService);
        Thread receiverThread = new Thread(receiver);
        receiverThread.start();
    }
}
