package com.example.websocket_demo_chat.client;

import com.example.websocket_demo_chat.Message;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.List;
import java.util.concurrent.ExecutionException;

import static com.example.websocket_demo_chat.client.Utilities.TRANSPERENT_COLOR;

/**
 * Graphical User Interface (GUI) for the WebSocket chat client.
 * Implements {@link MessageListener} to handle incoming messages and active user updates.
 */
public class ClientGUI extends JFrame implements MessageListener {
    private JPanel connectedUsersPanel, messagePanel;
    private MyStompClient myStompClient;
    private String username;
    private JScrollPane messagePanelScrollPane;

    /**
     * Constructs the chat client GUI.
     *
     * @param username the username of the client
     * @throws ExecutionException   if the connection setup fails
     * @throws InterruptedException if the connection setup is interrupted
     */
    public ClientGUI(String username) throws ExecutionException, InterruptedException {
        super("User: " + username);
        this.username = username;
        myStompClient = new MyStompClient(this, username);

        setSize(1218, 685);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        setupWindowListeners();
        getContentPane().setBackground(Utilities.PRIMARY_COLOR);
        addGuiComponents();
    }

    /**
     * Sets up listeners for window closing and resizing events.
     */
    private void setupWindowListeners() {
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                int option = JOptionPane.showConfirmDialog(ClientGUI.this, "Do you really want to leave?",
                        "Exit", JOptionPane.YES_NO_OPTION);

                if (option == JOptionPane.YES_OPTION) {
                    myStompClient.disconnectUser(username);
                    ClientGUI.this.dispose();
                }
            }
        });

        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                updateMessageSize();
            }
        });
    }

    /**
     * Initializes GUI components for chat functionality.
     */
    private void addGuiComponents() {
        addConnectedUsersComponents();
        addChatComponents();
    }

    /**
     * Sets up the panel displaying the list of connected users.
     */
    private void addConnectedUsersComponents() {
        connectedUsersPanel = new JPanel();
        connectedUsersPanel.setBorder(Utilities.addPadding(10, 10, 10, 10));
        connectedUsersPanel.setLayout(new BoxLayout(connectedUsersPanel, BoxLayout.Y_AXIS));
        connectedUsersPanel.setBackground(Utilities.SECONDARY_COLOR);
        connectedUsersPanel.setPreferredSize(new Dimension(200, getHeight()));

        JLabel connectedUsersLabel = new JLabel("Connected Users");
        connectedUsersLabel.setFont(new Font("Inter", Font.BOLD, 18));
        connectedUsersLabel.setForeground(Utilities.TEXT_COLOR);
        connectedUsersPanel.add(connectedUsersLabel);

        add(connectedUsersPanel, BorderLayout.WEST);
    }

    /**
     * Sets up the chat panel, including the message display area and input field.
     */
    private void addChatComponents() {
        JPanel chatPanel = new JPanel(new BorderLayout());
        chatPanel.setBackground(TRANSPERENT_COLOR);

        setupMessagePanel();
        setupInputPanel(chatPanel);

        add(chatPanel, BorderLayout.CENTER);
    }

    /**
     * Configures the message panel for displaying chat messages.
     */
    private void setupMessagePanel() {
        messagePanel = new JPanel();
        messagePanel.setLayout(new BoxLayout(messagePanel, BoxLayout.Y_AXIS));
        messagePanel.setBackground(TRANSPERENT_COLOR);

        messagePanelScrollPane = new JScrollPane(messagePanel);
        messagePanelScrollPane.setBackground(TRANSPERENT_COLOR);
        messagePanelScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        messagePanelScrollPane.getVerticalScrollBar().setUnitIncrement(16);
        messagePanelScrollPane.getViewport().addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                revalidate();
                repaint();
            }
        });
    }

    /**
     * Configures the input field for sending messages.
     *
     * @param chatPanel the chat panel to which the input field is added
     */
    private void setupInputPanel(JPanel chatPanel) {
        JPanel inputPanel = new JPanel(new BorderLayout());
        inputPanel.setBorder(Utilities.addPadding(10, 10, 10, 10));
        inputPanel.setBackground(TRANSPERENT_COLOR);

        JTextField inputField = new JTextField();
        inputField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                if (e.getKeyChar() == KeyEvent.VK_ENTER) {
                    String input = inputField.getText();
                    if (!input.isEmpty()) {
                        inputField.setText("");
                        myStompClient.sendMessage(new Message(username, input));
                    }
                }
            }
        });

        inputField.setBackground(Utilities.SECONDARY_COLOR);
        inputField.setForeground(Utilities.TEXT_COLOR);
        inputField.setBorder(Utilities.addPadding(0, 10, 0, 10));
        inputField.setFont(new Font("Inter", Font.PLAIN, 16));
        inputField.setPreferredSize(new Dimension(inputPanel.getWidth(), 50));

        inputPanel.add(inputField, BorderLayout.CENTER);
        chatPanel.add(inputPanel, BorderLayout.SOUTH);
    }

    /**
     * Creates a panel displaying a chat message.
     *
     * @param message the chat message to display
     * @return a JPanel containing the message components
     */
    private JPanel createChatMessageComponent(Message message) {
        JPanel chatMessage = new JPanel();
        chatMessage.setBackground(TRANSPERENT_COLOR);
        chatMessage.setLayout(new BoxLayout(chatMessage, BoxLayout.Y_AXIS));
        chatMessage.setBorder(Utilities.addPadding(20, 20, 10, 20));

        JLabel usernameLabel = new JLabel(message.user());
        usernameLabel.setFont(new Font("Inter", Font.BOLD, 18));
        usernameLabel.setForeground(Utilities.TEXT_COLOR);
        chatMessage.add(usernameLabel);

        JLabel messageLabel = new JLabel();
        messageLabel.setText("<html><body style='width:" + (0.60 * getWidth()) + "px'>" +
                message.message() + "</body></html>");
        messageLabel.setFont(new Font("Inter", Font.PLAIN, 18));
        messageLabel.setForeground(Utilities.TEXT_COLOR);
        chatMessage.add(messageLabel);

        return chatMessage;
    }

    /**
     * Handles an incoming chat message.
     * Adds the message to the UI and scrolls to the latest message.
     *
     * @param message the received message
     */
    @Override
    public void onMessageRecieve(Message message) {
        messagePanel.add(createChatMessageComponent(message));
        revalidate();
        repaint();
        messagePanelScrollPane.getVerticalScrollBar().setValue(Integer.MAX_VALUE);
    }

    /**
     * Updates the list of active users displayed in the UI.
     *
     * @param users the list of currently connected users
     */
    @Override
    public void onActiveUsersUpdated(List<String> users) {
        if (connectedUsersPanel.getComponents().length >= 2) {
            connectedUsersPanel.remove(1);
        }

        JPanel userListPanel = new JPanel();
        userListPanel.setBackground(TRANSPERENT_COLOR);
        userListPanel.setLayout(new BoxLayout(userListPanel, BoxLayout.Y_AXIS));

        for (String user : users) {
            JLabel username = new JLabel(user);
            username.setForeground(Utilities.TEXT_COLOR);
            username.setFont(new Font("Inter", Font.BOLD, 16));
            userListPanel.add(username);
        }

        connectedUsersPanel.add(userListPanel);
        revalidate();
        repaint();
    }

    /**
     * Updates the width of chat message components when the window is resized.
     */
    private void updateMessageSize() {
        for (Component component : messagePanel.getComponents()) {
            if (component instanceof JPanel) {
                JPanel chatMessage = (JPanel) component;
                if (chatMessage.getComponent(1) instanceof JLabel) {
                    JLabel messageLabel = (JLabel) chatMessage.getComponent(1);
                    messageLabel.setText("<html><body style='width:" +
                            (0.60 * getWidth()) + "px'>" + messageLabel.getText() + "</body></html>");
                }
            }
        }
    }
}
