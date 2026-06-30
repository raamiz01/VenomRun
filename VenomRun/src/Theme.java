import javax.swing.*;
import java.awt.*;

/*
 * File: Theme.java
 * Project: Venom Run
 * Authors: Raamiz Abrar
 *
 * Purpose:
 * Stores the main colours and a few helper methods used across the game's UI.
 *
 * References used for understanding Swing styling:
 * - Oracle Java Swing Tutorial: https://docs.oracle.com/javase/tutorial/uiswing/
 */
public class Theme {
    public static final Color BG_DARK = new Color(18, 10, 10);
    public static final Color BG_PANEL = new Color(32, 18, 18);
    public static final Color BG_CARD = new Color(46, 28, 28);
    public static final Color ACCENT_GREEN = new Color(138, 220, 120);
    public static final Color ACCENT_GREEN_DARK = new Color(72, 130, 64);
    public static final Color ACCENT_RED = new Color(180, 42, 42);
    public static final Color TEXT_LIGHT = new Color(240, 245, 235);
    public static final Color GRID_BG = new Color(191, 209, 169);
    public static final Color GRID_LINE = new Color(61, 87, 56);
    public static final Color WALL_COLOR = new Color(98, 104, 112);
    public static final Color WALL_DARK = new Color(67, 71, 77);
    public static final Color PLAYER_ONE = new Color(70, 153, 71);
    public static final Color PLAYER_TWO = new Color(69, 126, 204);

    public static Font titleFont(int size) {
        return new Font("Arial", Font.BOLD, size);
    }

    public static Font bodyFont(int size) {
        return new Font("Arial", Font.BOLD, size);
    }

    public static JButton makeButton(String text) {
        JButton button = new JButton(text);
        button.setFocusPainted(false);
        button.setBackground(BG_CARD);
        button.setForeground(ACCENT_GREEN);
        button.setFont(bodyFont(18));
        button.setBorder(BorderFactory.createLineBorder(ACCENT_GREEN, 2));
        return button;
    }
}
