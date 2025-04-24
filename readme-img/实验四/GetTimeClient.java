package cn.abalone;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class GetTimeClient {
    private final Socket socket;
    public GetTimeClient(String host,int port) throws IOException {
        this.socket = new Socket(host,port);
    }
    private PrintWriter getWriter() throws IOException {
        return new PrintWriter(socket.getOutputStream(),true);
    }
    private BufferedReader getReader() throws IOException {
        return new BufferedReader(new InputStreamReader(socket.getInputStream()));
    }
    public void connect() throws IOException {
        BufferedReader reader = getReader();
        PrintWriter writer = getWriter();
        System.out.println("Sever: "+reader.readLine());
        Scanner sc = new Scanner(System.in);
        String msg;
        while((msg = sc.next())!=null){
            writer.println(msg);
            System.out.println("Sever: "+reader.readLine());
            if (msg.equals("Exit")){
                socket.close();
                break;
            }
        }
    }
    public static void main(String[] args) throws IOException {
        new GetTimeClient("47.107.253.223",6666).connect();
    }
}
