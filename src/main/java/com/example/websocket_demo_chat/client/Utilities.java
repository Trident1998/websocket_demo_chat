package com.example.websocket_demo_chat.client;

import javax.swing.border.EmptyBorder;
import java.awt.*;

/**
 * Utility class providing common colors and UI-related helper methods for the chat client.
 */
public class Utilities {
    public static final Color TRANSPERENT_COLOR = new Color(0, 0, 0, 0);
    public static final Color PRIMARY_COLOR = Color.decode("#2F2D2D");
    public static final Color SECONDARY_COLOR = Color.decode ("#484444");
    public static final Color TEXT_COLOR = Color.WHITE;

    /**
     * Creates an {@link EmptyBorder} with the specified padding values.
     *
     * @param top    the top padding in pixels
     * @param left   the left padding in pixels
     * @param bottom the bottom padding in pixels
     * @param right  the right padding in pixels
     * @return an {@link EmptyBorder} instance with the given padding
     */
    public static EmptyBorder addPadding(int top, int left, int bottom, int right) {
        return new EmptyBorder(top, left, bottom, right);
    }
}
