package cn.abalone.client.tools;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Tools {
    public static String getTime() {
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }
}
