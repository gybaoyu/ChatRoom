package cn.abalone;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class GetTimeServer {
    private final ServerSocket serverSocket;

    public GetTimeServer(int port) throws IOException {
        serverSocket = new ServerSocket(port);
    }

    private BufferedReader getReader(Socket socket) throws IOException {
        return new BufferedReader(new InputStreamReader(socket.getInputStream()));//获取这个socket的输入流
    }

    private PrintWriter getWriter(Socket socket) throws IOException {
        return new PrintWriter(socket.getOutputStream(), true);//获取这个socket的输出流
    }

    public void service() throws IOException {
        Socket socket;
        socket = serverSocket.accept();
        BufferedReader reader = getReader(socket);
        PrintWriter writer = getWriter(socket);
        writer.println(socket.getInetAddress() + ":" + socket.getPort() + "已连接");
        System.out.println(socket.getInetAddress() + ":" + socket.getPort() + "已连接");
        while (true) {
            String msg = null;
            while ((msg = reader.readLine()) != null) {
                System.out.println("Client "+socket.getInetAddress()+"说: " + msg);
                if (msg.equals("Exit")) {
                    writer.println("OK,Bye~");
                    socket.close();
                    return;
                }
                if (msg.equals("Time")) {
                    writer.println("现在是: " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
                }
            }
        }
    }

    public static void main(String[] args) throws IOException {
        new GetTimeServer(6666).service();
    }
}
