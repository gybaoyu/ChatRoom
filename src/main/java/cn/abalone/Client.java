package cn.abalone;

import cn.abalone.service.ClientMessageService;
import com.formdev.flatlaf.FlatDarkLaf;
import java.io.IOException;
import javax.swing.*;

public class Client {
    public static void main(String[] args) throws IOException, InterruptedException {
        FlatDarkLaf.setup(); // 暗色主题
        JFrame.setDefaultLookAndFeelDecorated(true);
        ClientMessageService service = new ClientMessageService();
        service.initClient();
    }
}
