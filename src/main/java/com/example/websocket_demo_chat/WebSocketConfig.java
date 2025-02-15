package com.example.websocket_demo_chat;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

/**
 * Configuration class for setting up WebSocket messaging.
 * Enables WebSocket message brokering and defines STOMP endpoints.
 */
@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    /**
     * Configures the message broker.
     * Enables a simple in-memory message broker with the prefix "/topic" for subscriptions.
     * Sets application destination prefixes for messages sent from clients to the server.
     *
     * @param registry the MessageBrokerRegistry to configure
     */
    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        registry.enableSimpleBroker("/topic");
        registry.setApplicationDestinationPrefixes("/app");
    }

    /**
     * Registers STOMP endpoints to allow WebSocket connections.
     * The "/ws" endpoint is added with SockJS support for fallback options.
     *
     * @param registry the StompEndpointRegistry to configure
     */
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/ws").withSockJS();
    }
}
