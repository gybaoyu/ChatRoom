package cn.abalone.server.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import static cn.abalone.server.tools.Tools.getTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Message {
    private String name;
    private String message;
    private String msgDate;
    public Message(String name, String message) {
        this.name = name;
        this.message = message;
        this.msgDate = getTime();
    }
}
