package cn.abalone.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.net.InetAddress;
import java.net.SocketAddress;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
    private String name;
    private InetAddress host;
    private int port;
    public User(String name) {
        this.name = name;
    }
}
