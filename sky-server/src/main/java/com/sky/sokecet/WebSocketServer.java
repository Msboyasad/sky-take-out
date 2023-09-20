package com.sky.sokecet;


import org.springframework.stereotype.Component;

import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Component
@ServerEndpoint("/ws/{sid}")
public class WebSocketServer {


    private static Map<String, Session> sessionMap = new HashMap<>();

    /**
     * 建立连接
     *
     * @param session
     * @param sid
     */
    @OnOpen
    public void onOpen(Session session, @PathParam("sid") String sid) {
        System.out.println("客户端" + sid + "建立起连接");
        sessionMap.put(sid, session);
    }

    /**
     * 接收客户端的消息
     *
     * @param message
     * @param sid
     */
    @OnMessage
    public void onMessage(String message, @PathParam("sid") String sid) {
        System.out.println("收到客户端：" + sid + "的消息" + message);
    }

    /**
     * 断开连接
     * @param sid
     */
    @OnClose
    public void onClose(@PathParam("sid") String sid) {
        System.out.println("连接断开" + sid);
        sessionMap.remove(sid);
    }

    /**
     * 群发消息
     * @param message
     */
    public void sendToAllClient(String message){
        Collection<Session> values = sessionMap.values();
        values.forEach(
                session -> {
                    try {
                        //向客户端发消息
                        session.getBasicRemote().sendText(message);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
        );
    }
}
