package com.sky.task;

import com.sky.sokecet.WebSocketServer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Component
@Slf4j
public class SocketTask {
    @Autowired
    private WebSocketServer webSocketServer;

    //@Scheduled(cron = "0/5 * * * * ?")
    public void sendClientMsg(){
        webSocketServer.sendToAllClient("这是来自服务器的消息" +
                DateTimeFormatter.ofPattern("HH:mm:ss").format(LocalDateTime.now()));
    }

}
