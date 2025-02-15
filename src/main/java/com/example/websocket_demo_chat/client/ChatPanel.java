package com.example.websocket_demo_chat.client;

import com.example.websocket_demo_chat.Message;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

class ChatPanel extends JPanel {
    private JPanel messagePanel;
    private JScrollPane scrollPane;
    private MyStompClient myStompClient;
    private String username;

    public ChatPanel(MyStompClient myStompClient, String username) {
        this.myStompClient = myStompClient;
        this.username = username;
        setLayout(new BorderLayout());
        setBackground(Utilities.TRANSPERENT_COLOR);
        addMessagePanel();
        addInputField();
    }

    private void addMessagePanel() {
        messagePanel = new JPanel();
        messagePanel.setLayout(new BoxLayout(messagePanel, BoxLayout.Y_AXIS));
        messagePanel.setBackground(Utilities.TRANSPERENT_COLOR);

        scrollPane = new JScrollPane(messagePanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        add(scrollPane, BorderLayout.CENTER);
    }

    private void addInputField() {
        JTextField inputField = new JTextField();
        inputField.setFont(new Font("Inter", Font.PLAIN, 16));
        inputField.setPreferredSize(new Dimension(getWidth(), 50));
        inputField.setBackground(Utilities.SECONDARY_COLOR);
        inputField.setForeground(Utilities.TEXT_COLOR);
        inputField.setBorder(Utilities.addPadding(0, 10, 0, 10));
        inputField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                if (e.getKeyChar() == KeyEvent.VK_ENTER) {
                    String input = inputField.getText().trim();
                    if (!input.isEmpty()) {
                        inputField.setText("");
                        myStompClient.sendMessage(new Message(username, input));
                    }
                }
            }
        });
        add(inputField, BorderLayout.SOUTH);
    }

    public void addMessage(Message message) {
        JPanel chatMessage = new JPanel();
        chatMessage.setLayout(new BoxLayout(chatMessage, BoxLayout.Y_AXIS));
        chatMessage.setBackground(Utilities.TRANSPERENT_COLOR);
        chatMessage.setBorder(Utilities.addPadding(20, 20, 10, 20));

        JLabel usernameLabel = new JLabel(message.user());
        usernameLabel.setFont(new Font("Inter", Font.BOLD, 18));
        usernameLabel.setForeground(Utilities.TEXT_COLOR);
        chatMessage.add(usernameLabel);

        JLabel messageLabel = new JLabel("<html><body style='width: 600px'>" + message.message() + "</body></html>");
        messageLabel.setFont(new Font("Inter", Font.PLAIN, 18));
        messageLabel.setForeground(Utilities.TEXT_COLOR);
        chatMessage.add(messageLabel);

        messagePanel.add(chatMessage);
        revalidate();
        repaint();
        scrollPane.getVerticalScrollBar().setValue(scrollPane.getVerticalScrollBar().getMaximum());
    }
}
