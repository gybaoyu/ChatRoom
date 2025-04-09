package cn.abalone.server.tools;

import cn.abalone.server.entity.Message;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Tools {
    private static final ObjectMapper mapper = new ObjectMapper();
    @SneakyThrows
    public static String messageToJson(Message msg) {
        return mapper.writeValueAsString(msg);
    }
    @SneakyThrows
    public static Message jsonToMessage(String json) {
        return mapper.readValue(json,Message.class);
    }
    public static String getTime() {
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern("MM-dd HH:mm:ss"));
    }
}
