package com.example.websocket_demo_chat.client;

import javax.swing.*;
import java.awt.*;
import java.util.List;

class ConnectedUsersPanel extends JPanel {
    private JPanel userListPanel;

    public ConnectedUsersPanel() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBackground(Utilities.SECONDARY_COLOR);
        setPreferredSize(new Dimension(200, getHeight()));
        addUsersLabel();
        userListPanel = new JPanel();
        userListPanel.setLayout(new BoxLayout(userListPanel, BoxLayout.Y_AXIS));
        userListPanel.setBackground(Utilities.SECONDARY_COLOR);
        add(userListPanel);
    }

    private void addUsersLabel() {
        JLabel label = new JLabel("Connected Users");
        label.setFont(new Font("Inter", Font.BOLD, 18));
        label.setForeground(Utilities.TEXT_COLOR);
        add(label);
    }

    public void updateUsers(List<String> users) {
        userListPanel.removeAll();
        for (String user : users) {
            JLabel usernameLabel = new JLabel(user);
            usernameLabel.setFont(new Font("Inter", Font.BOLD, 16));
            usernameLabel.setForeground(Utilities.TEXT_COLOR);
            userListPanel.add(usernameLabel);
        }
        revalidate();
        repaint();
    }
}