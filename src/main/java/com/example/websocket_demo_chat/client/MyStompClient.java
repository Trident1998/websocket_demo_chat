package com.example.websocket_demo_chat.client;

import com.example.websocket_demo_chat.Message;
import lombok.Data;
import lombok.SneakyThrows;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;
import org.springframework.web.socket.sockjs.client.SockJsClient;
import org.springframework.web.socket.sockjs.client.Transport;
import org.springframework.web.socket.sockjs.client.WebSocketTransport;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

@Data
public class MyStompClient {
    private StompSession session;
    private String username;
    private MessageListener messageListener;


    public MyStompClient(MessageListener messageListener, String username) throws ExecutionException, InterruptedException {
        this.username = username;
        this.messageListener = messageListener;


        final var transports = new ArrayList<Transport>();
        transports.add(new WebSocketTransport(new StandardWebSocketClient()));

        final var sockJsClient = new SockJsClient(transports);
        final var stompClient = new WebSocketStompClient(sockJsClient);
        stompClient.setMessageConverter(new MappingJackson2MessageConverter());

        final var sessionHandler = new MyStompSessionHandler(messageListener, username);
        final var url = "ws://localhost:8080/ws";

        this.session = stompClient.connectAsync(url, sessionHandler).get();
    }

    public void sendMessage(Message message) {
        try {
            session.send("/app/message", message);
            System.out.printf("Message sent: %s\n", message.message());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void disconnectUser(String username) {
        try {
            session.send("/app/disconnect", username);
            System.out.printf("Disconnect User: %s\n", username);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
