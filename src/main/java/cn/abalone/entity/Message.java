package cn.abalone.entity;

import cn.abalone.service.Tools;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Message {
    String msg;
    String time;
    User user;
    public Message(String msg,User user) {
        this.msg = msg;
        this.time = Tools.getTime();
        this.user = user;
    }
    public Message(String msg, String time,User user) {
        this.msg = msg;
        this.time = time;
        this.user = user;
    }
}
