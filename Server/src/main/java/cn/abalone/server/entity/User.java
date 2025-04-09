package cn.abalone.server.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.net.InetAddress;

@Data
@AllArgsConstructor
public class User {
    private String name;
    private InetAddress address;
    private int port;
}
