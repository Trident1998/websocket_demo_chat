package com.example.websocket_demo_chat;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class WebSocketSessionManager {
    private final Set<String> activeUsers = new HashSet<>();
    private final SimpMessagingTemplate messagingTemplate;

    @Autowired
    public WebSocketSessionManager(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    public void addUser(String username) {
        activeUsers.add(username);
    }

    public void removeUser(String username) {
        activeUsers.remove(username);
    }

    public void broadcastActiveUsernames() {
        messagingTemplate.convertAndSend("/topic/users", activeUsers);
        System.out.printf("Broadcast active users to /topic/users %s\n", activeUsers);
    }
}
