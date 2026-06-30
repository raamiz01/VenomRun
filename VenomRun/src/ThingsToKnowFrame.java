import javax.swing.*;
import java.awt.*;

/*
 * File: ThingsToKnowFrame.java
 * Project: Venom Run
 * Authors: Noah Hunt
 *
 * Purpose:
 * Shows a "Things To Know" screen between mode selection and gameplay.
 * It explains controls and previews apples, obstacles, and snakes.
 */
public class ThingsToKnowFrame extends JFrame {
    private boolean twoPlayer;
    private Difficulty difficulty;

    public ThingsToKnowFrame(boolean twoPlayer, Difficulty difficulty) {
        this.twoPlayer = twoPlayer;
        this.difficulty = difficulty;

        setTitle("Venom Run - Things To Know");
        setSize(980, 680);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setContentPane(buildUI());
    }

    private JPanel buildUI() {
        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(Theme.BG_DARK);

        JLabel title = new JLabel("THINGS TO KNOW", SwingConstants.CENTER);
        title.setFont(Theme.titleFont(34));
        title.setForeground(Theme.ACCENT_GREEN);
        title.setBorder(BorderFactory.createEmptyBorder(25, 10, 10, 10));

        root.add(title, BorderLayout.NORTH);

        JPanel center = new JPanel(new GridLayout(1, 2, 20, 20));
        center.setOpaque(false);
        center.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));

        center.add(new InfoTextPanel());
        center.add(new PreviewInfoPanel());

        root.add(center, BorderLayout.CENTER);

        JPanel bottom = new JPanel();
        bottom.setOpaque(false);

        JButton back = Theme.makeButton("BACK");
        JButton start = Theme.makeButton("START GAME");

        back.addActionListener(e -> {
            new DifficultyFrame(twoPlayer).setVisible(true);
            dispose();
        });

        start.addActionListener(e -> {
            new GameFrame(difficulty, twoPlayer).setVisible(true);
            dispose();
        });

        bottom.add(back);
        bottom.add(Box.createHorizontalStrut(25));
        bottom.add(start);
        bottom.setBorder(BorderFactory.createEmptyBorder(10, 10, 25, 10));

        root.add(bottom, BorderLayout.SOUTH);

        return root;
    }

    private class InfoTextPanel extends JPanel {
        public InfoTextPanel() {
            setBackground(Theme.BG_CARD);

            setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(Theme.ACCENT_GREEN, 2),
                    BorderFactory.createEmptyBorder(18, 18, 18, 18)
            ));

            setLayout(new BorderLayout(10, 10));

            JLabel modeLabel = new JLabel(twoPlayer ? "2-PLAYER CONTROLS" : "1-PLAYER CONTROLS");
            modeLabel.setFont(Theme.titleFont(24));
            modeLabel.setForeground(Theme.ACCENT_GREEN);

            add(modeLabel, BorderLayout.NORTH);

            JTextArea area = new JTextArea();
            area.setEditable(false);
            area.setOpaque(false);
            area.setForeground(Theme.TEXT_LIGHT);
            area.setFont(Theme.bodyFont(16));
            area.setLineWrap(true);
            area.setWrapStyleWord(true);
            area.setText(getThingsToKnowText());

            add(area, BorderLayout.CENTER);
        }
    }

    private class PreviewInfoPanel extends JPanel {
        public PreviewInfoPanel() {
            setBackground(Theme.BG_CARD);

            setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(Theme.ACCENT_GREEN, 2),
                    BorderFactory.createEmptyBorder(18, 18, 18, 18)
            ));
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);

            Graphics2D g2 = (Graphics2D) g;

            g2.setColor(Theme.TEXT_LIGHT);
            g2.setFont(Theme.titleFont(20));
            g2.drawString("PREVIEW", 18, 30);

            drawLabel(g2, "Apple", 22, 85);
            drawApple(g2, 255, 58, 34);

            drawLabel(g2, "Obstacle / Wall", 22, 165);
            drawObstacle(g2, 320, 135, 44);

            drawLabel(g2, "Player 1 Snake", 22, 250);
            drawSnakePreview(g2, 310, 228, Theme.PLAYER_ONE);

            if (twoPlayer) {
                drawLabel(g2, "Player 2 Snake", 22, 335);
                drawSnakePreview(g2, 310, 313, Theme.PLAYER_TWO);
            }
        }

        private void drawLabel(Graphics2D g2, String text, int x, int y) {
            g2.setColor(Theme.ACCENT_GREEN);
            g2.setFont(Theme.bodyFont(18));
            g2.drawString(text, x, y);
        }
    }

    private String getThingsToKnowText() {
        StringBuilder sb = new StringBuilder();

        sb.append("Difficulty: ").append(difficulty.getDisplayName()).append("\n\n");

        if (!twoPlayer) {
            sb.append("Controls:\n");
            sb.append("W = Up\n");
            sb.append("A = Left\n");
            sb.append("S = Down\n");
            sb.append("D = Right\n\n");
        } else {
            sb.append("Player 1 Controls:\n");
            sb.append("W = Up\n");
            sb.append("A = Left\n");
            sb.append("S = Down\n");
            sb.append("D = Right\n\n");

            sb.append("Player 2 Controls:\n");
            sb.append("Arrow Keys = Move\n\n");
        }

        sb.append("What to know:\n");
        sb.append("- Eat apples to gain points.\n");
        sb.append("- Avoid walls, your own body, and obstacles.\n");

        if (twoPlayer) {
            sb.append("- Avoid the other snake too.\n");
            sb.append("- If one player gets out first, the other keeps playing until they also lose.\n");
        }

        sb.append("- Apples and obstacles are shown on the right.\n");

        return sb.toString();
    }

    private void drawApple(Graphics2D g2, int x, int y, int size) {
        g2.setColor(new Color(215, 48, 48));
        g2.fillOval(x, y, size, size);

        g2.setColor(new Color(170, 32, 32));
        g2.drawOval(x, y, size, size);

        g2.setColor(new Color(125, 60, 20));
        g2.fillRect(x + size / 2 - 1, y - 6, 3, 10);

        g2.setColor(new Color(80, 150, 80));
        g2.fillOval(x + size / 2 + 4, y - 3, 10, 6);
    }

    private void drawObstacle(Graphics2D g2, int x, int y, int size) {
        g2.setColor(Theme.WALL_COLOR);
        g2.fillRoundRect(x, y, size, size, 8, 8);

        g2.setColor(Theme.WALL_DARK);
        g2.drawRoundRect(x, y, size, size, 8, 8);
        g2.drawLine(x + 8, y + size / 2, x + size - 8, y + size / 2);
        g2.drawLine(x + size / 2, y + 8, x + size / 2, y + size - 8);
    }

    private void drawSnakePreview(Graphics2D g2, int x, int y, Color color) {
        for (int i = 0; i < 4; i++) {
            int px = x - i * 24;

            g2.setColor(color);
            g2.fillRoundRect(px, y, 22, 22, 14, 14);

            g2.setColor(color.darker());
            g2.drawRoundRect(px, y, 22, 22, 14, 14);
        }

        /*
         * Draws two clear eyes on the snake head.
         */
        g2.setColor(Color.WHITE);
        g2.fillOval(x + 13, y + 5, 5, 5);
        g2.fillOval(x + 13, y + 13, 5, 5);

        g2.setColor(Color.BLACK);
        g2.fillOval(x + 15, y + 7, 2, 2);
        g2.fillOval(x + 15, y + 15, 2, 2);
    }
}