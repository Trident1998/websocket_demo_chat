package com.example.websocket_demo_chat;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

/**
 * Service to manage WebSocket sessions and track active users.
 * Provides functionality to add and remove users from the active users list
 * and broadcast the updated list to subscribed clients.
 */
@Service
public class WebSocketSessionManager {

    private final Set<String> activeUsers = new HashSet<>();
    private final SimpMessagingTemplate messagingTemplate;

    /**
     * Constructs a WebSocketSessionManager with the given messaging template.
     *
     * @param messagingTemplate the SimpMessagingTemplate used for broadcasting messages
     */
    @Autowired
    public WebSocketSessionManager(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    /**
     * Adds a user to the active users set.
     *
     * @param username the username to add
     */
    public void addUser(String username) {
        activeUsers.add(username);
    }

    /**
     * Removes a user from the active users set.
     *
     * @param username the username to remove
     */
    public void removeUser(String username) {
        activeUsers.remove(username);
    }

    /**
     * Broadcasts the current list of active users to all subscribed clients.
     */
    public void broadcastActiveUsernames() {
        messagingTemplate.convertAndSend("/topic/users", activeUsers);
        System.out.printf("Broadcast active users to /topic/users %s\n", activeUsers);
    }
}
