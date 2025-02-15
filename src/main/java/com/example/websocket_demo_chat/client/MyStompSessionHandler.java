package com.example.websocket_demo_chat.client;

import com.example.websocket_demo_chat.Message;
import org.springframework.messaging.simp.stomp.StompFrameHandler;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;

import java.lang.reflect.Type;
import java.util.ArrayList;

/**
 * Custom STOMP session handler for managing WebSocket connections and message subscriptions.
 * Handles incoming messages and active user updates.
 */
public class MyStompSessionHandler extends StompSessionHandlerAdapter {
    private final String username;
    private final MessageListener messageListener;

    public MyStompSessionHandler(MessageListener messageListener, String username) {
        this.username = username;
        this.messageListener = messageListener;
    }

    /**
     * Called after successfully connecting to the WebSocket server.
     * Subscribes to relevant topics and sends initial connection messages.
     *
     * @param session          the active STOMP session
     * @param connectedHeaders headers associated with the connection
     */
    @Override
    public void afterConnected(StompSession session, StompHeaders connectedHeaders) {
        System.out.println("Client Connected");

        // Subscribe to the "/topic/messages" destination
        session.subscribe("/topic/messages", new StompFrameHandler() {
            @Override
            public Type getPayloadType(StompHeaders headers) {
                return Message.class;
            }

            @Override
            public void handleFrame(StompHeaders headers, Object payload) {
                try {
                    if (payload instanceof Message) {
                        Message message = (Message) payload;
                        messageListener.onMessageRecieve(message);
                        System.out.println("Received message: " + message.user() + ": " + message.message());
                    } else {
                        System.out.println("Received unexpected payload type: " + payload.getClass());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        System.out.println("Client Subscribed to /topic/messages");

        // Subscribe to the "/topic/users" destination
        session.subscribe("/topic/users", new StompFrameHandler() {
            @Override
            public Type getPayloadType(StompHeaders headers) {
                return new ArrayList<String>().getClass();
            }

            @Override
            public void handleFrame(StompHeaders headers, Object payload) {
                try {
                    if (payload instanceof ArrayList) {
                        ArrayList<String> activeUsers = (ArrayList<String>) payload;
                        messageListener.onActiveUsersUpdated(activeUsers);
                        System.out.println("Received active users: " + activeUsers);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        System.out.println("Subscribed to /topic/users");

        // Notify server about user connection
        session.send("/app/connect", username);

        // Request the list of active users
        session.send("/app/request-users", "");
    }

    /**
     * Handles transport-level errors that occur during WebSocket communication.
     *
     * @param session   the STOMP session associated with the error
     * @param exception the exception representing the transport error
     */
    @Override
    public void handleTransportError(StompSession session, Throwable exception) {
        exception.printStackTrace();
    }
}
