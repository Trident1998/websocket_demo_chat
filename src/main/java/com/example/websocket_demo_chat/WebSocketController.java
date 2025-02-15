package com.example.websocket_demo_chat;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
public class WebSocketController {

    private final SimpMessagingTemplate simpMessagingTemplate;
    private final WebSocketSessionManager sessionManager;

    @Autowired
    public WebSocketController(SimpMessagingTemplate simpMessagingTemplate, WebSocketSessionManager sessionManager) {
        this.simpMessagingTemplate = simpMessagingTemplate;
        this.sessionManager = sessionManager;
    }

    @MessageMapping("/message")
    public void handleMessaging(Message message) {
        System.out.printf("Receive a message from user: %s : %s\n", message.user(), message.message());
        simpMessagingTemplate.convertAndSend("/topic/messages", message);
        System.out.printf("Sent a message to /topic/messages: %s : %s\n", message.user(), message.message());
    }

    @MessageMapping("/connect")
    public void connectUser(String username) {
        sessionManager.addUser(username);
        sessionManager.broadcastActiveUsernames();
        System.out.printf("%s connected\n", username);
    }

    @MessageMapping("/disconnect")
    public void disconnectUser(String username) {
        sessionManager.removeUser(username);
        sessionManager.broadcastActiveUsernames();
        System.out.printf("%s disconnected\n", username);
    }
}
