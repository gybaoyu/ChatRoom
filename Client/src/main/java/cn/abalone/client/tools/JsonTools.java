package cn.abalone.client.tools;

import cn.abalone.client.entity.Message;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;

public class JsonTools {
    private static final ObjectMapper mapper = new ObjectMapper();
    @SneakyThrows
    public static String messageToJson(Message msg) {
        return mapper.writeValueAsString(msg);
    }
    @SneakyThrows
    public static Message jsonToMessage(String json) {
        return mapper.readValue(json,Message.class);
    }
}
