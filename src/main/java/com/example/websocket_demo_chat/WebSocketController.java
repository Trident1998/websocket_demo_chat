package com.example.websocket_demo_chat;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

/**
 * Controller for handling WebSocket messages.
 * Provides endpoints for sending messages, connecting, and disconnecting users.
 */
@Controller
public class WebSocketController {

    private final SimpMessagingTemplate simpMessagingTemplate;
    private final WebSocketSessionManager sessionManager;

    /**
     * Constructs a WebSocketController with the given messaging template and session manager.
     *
     * @param simpMessagingTemplate the messaging template for broadcasting messages
     * @param sessionManager the session manager for tracking active users
     */
    @Autowired
    public WebSocketController(SimpMessagingTemplate simpMessagingTemplate, WebSocketSessionManager sessionManager) {
        this.simpMessagingTemplate = simpMessagingTemplate;
        this.sessionManager = sessionManager;
    }

    /**
     * Handles incoming messages and broadcasts them to subscribed clients.
     *
     * @param message the message received from a user
     */
    @MessageMapping("/message")
    public void handleMessaging(Message message) {
        System.out.printf("Receive a message from user: %s : %s\n", message.user(), message.message());
        simpMessagingTemplate.convertAndSend("/topic/messages", message);
        System.out.printf("Sent a message to /topic/messages: %s : %s\n", message.user(), message.message());
    }

    /**
     * Handles user connection, adds the user to the session manager, and broadcasts active users.
     *
     * @param username the username of the connecting user
     */
    @MessageMapping("/connect")
    public void connectUser(String username) {
        sessionManager.addUser(username);
        sessionManager.broadcastActiveUsernames();
        System.out.printf("%s connected\n", username);
    }

    /**
     * Handles user disconnection, removes the user from the session manager, and broadcasts active users.
     *
     * @param username the username of the disconnecting user
     */
    @MessageMapping("/disconnect")
    public void disconnectUser(String username) {
        sessionManager.removeUser(username);
        sessionManager.broadcastActiveUsernames();
        System.out.printf("%s disconnected\n", username);
    }
}
