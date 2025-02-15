package com.example.websocket_demo_chat.client;

import com.example.websocket_demo_chat.Message;

import java.util.List;

public interface MessageListener {

    void onMessageRecieve(Message message);

    void onActiveUsersUpdated(List<String> users);
}
