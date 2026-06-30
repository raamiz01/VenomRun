import javax.swing.*;
import java.awt.*;

/*
 * File: MainMenuFrame.java
 * Project: Venom Run
 * Authors: Raamiz Abrar, Arham Rashad, Noah Hunt,
 *
 * Purpose:
 * Displays the main menu using a more arcade-style layout.
 *
 * References:
 * - Oracle JFrame help: https://docs.oracle.com/javase/tutorial/uiswing/components/frame.html
 */
public class MainMenuFrame extends JFrame {

    public MainMenuFrame() {
        setTitle("Venom Run - Main Menu");
        setSize(980, 680);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setContentPane(new MainMenuPanel());
    }

    private class MainMenuPanel extends JPanel {
        public MainMenuPanel() {
            setLayout(new BorderLayout());
            setBackground(Theme.BG_DARK);

            JPanel topPanel = new JPanel(new BorderLayout());
            topPanel.setOpaque(false);
            topPanel.setBorder(BorderFactory.createEmptyBorder(28, 35, 10, 35));

            JLabel title = new JLabel("VENOM RUN", SwingConstants.CENTER);
            title.setFont(Theme.titleFont(40));
            title.setForeground(Theme.ACCENT_GREEN);
            topPanel.add(title, BorderLayout.CENTER);

            JLabel sub = new JLabel("Choose a mode", SwingConstants.CENTER);
            sub.setFont(Theme.bodyFont(16));
            sub.setForeground(Theme.TEXT_LIGHT);
            topPanel.add(sub, BorderLayout.SOUTH);

            add(topPanel, BorderLayout.NORTH);

            JPanel centerPanel = new JPanel(new GridBagLayout());
            centerPanel.setOpaque(false);
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.insets = new Insets(10, 20, 10, 20);

            ModeCard onePlayer = new ModeCard("1 PLAYER", "Classic solo run", false);
            ModeCard twoPlayer = new ModeCard("2 PLAYER", "Head-to-head snake battle", true);

            gbc.gridx = 0;
            centerPanel.add(onePlayer, gbc);

            gbc.gridx = 1;
            centerPanel.add(twoPlayer, gbc);

            add(centerPanel, BorderLayout.CENTER);

            JPanel bottomPanel = new JPanel();
            bottomPanel.setOpaque(false);
            bottomPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 25, 20));

            JButton exitButton = Theme.makeButton("EXIT");
            exitButton.addActionListener(e -> System.exit(0));

            bottomPanel.add(exitButton);
            add(bottomPanel, BorderLayout.SOUTH);
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);

            Graphics2D g2 = (Graphics2D) g;
            GradientPaint gp = new GradientPaint(0, 0, new Color(60, 24, 24), 0, getHeight(), Theme.BG_DARK);

            g2.setPaint(gp);
            g2.fillRect(0, 0, getWidth(), getHeight());

            /*
             * Decorative background shapes to make the screen look less plain.
             */
            g2.setColor(new Color(120, 35, 35, 70));

            for (int i = 0; i < 7; i++) {
                g2.fillRoundRect(60 + i * 120, 500 - (i % 2) * 20, 90, 30, 18, 18);
            }
        }
    }

    private class ModeCard extends JPanel {
        public ModeCard(String titleText, String subtitle, boolean twoPlayer) {
            setPreferredSize(new Dimension(310, 280));
            setBackground(Theme.BG_CARD);

            setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(Theme.ACCENT_GREEN, 2),
                    BorderFactory.createEmptyBorder(20, 20, 20, 20)
            ));

            setLayout(new BorderLayout());

            JLabel title = new JLabel(titleText, SwingConstants.CENTER);
            title.setFont(Theme.titleFont(24));
            title.setForeground(Theme.ACCENT_GREEN);
            add(title, BorderLayout.NORTH);

            PreviewPanel preview = new PreviewPanel(twoPlayer);
            add(preview, BorderLayout.CENTER);

            JLabel desc = new JLabel(subtitle, SwingConstants.CENTER);
            desc.setFont(Theme.bodyFont(15));
            desc.setForeground(Theme.TEXT_LIGHT);
            add(desc, BorderLayout.SOUTH);

            /*
             * Clicking the card opens the difficulty screen.
             */
            addMouseListener(new java.awt.event.MouseAdapter() {
                @Override
                public void mouseClicked(java.awt.event.MouseEvent e) {
                    new DifficultyFrame(twoPlayer).setVisible(true);
                    dispose();
                }
            });
        }
    }

    private class PreviewPanel extends JPanel {
        private boolean twoPlayer;

        public PreviewPanel(boolean twoPlayer) {
            this.twoPlayer = twoPlayer;
            setOpaque(false);
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);

            Graphics2D g2 = (Graphics2D) g;

            g2.setColor(new Color(25, 20, 20));
            g2.fillRoundRect(20, 15, getWidth() - 40, getHeight() - 30, 24, 24);

            g2.setColor(Theme.GRID_BG);
            g2.fillRoundRect(34, 28, getWidth() - 68, getHeight() - 56, 18, 18);

            int x1 = 85;
            int y1 = 85;

            drawMiniSnake(g2, x1, y1, Theme.PLAYER_ONE);

            if (twoPlayer) {
                drawMiniSnake(g2, x1 + 120, y1 + 10, Theme.PLAYER_TWO);
                drawApple(g2, x1 + 75, y1 + 85, 18);
                drawApple(g2, x1 + 135, y1 + 65, 18);
            } else {
                drawApple(g2, x1 + 110, y1 + 70, 20);
            }
        }
    }

    private void drawMiniSnake(Graphics2D g2, int x, int y, Color color) {
        for (int i = 0; i < 3; i++) {
            int px = x - i * 22;

            g2.setColor(color);
            g2.fillRoundRect(px, y, 20, 20, 12, 12);

            g2.setColor(color.darker());
            g2.drawRoundRect(px, y, 20, 20, 12, 12);
        }

        /*
         * Draws two eyes on the snake head.
         */
        g2.setColor(Color.WHITE);
        g2.fillOval(x + 10, y + 4, 4, 4);
        g2.fillOval(x + 10, y + 12, 4, 4);

        g2.setColor(Color.BLACK);
        g2.fillOval(x + 11, y + 5, 2, 2);
        g2.fillOval(x + 11, y + 13, 2, 2);
    }

    private void drawApple(Graphics2D g2, int x, int y, int size) {
        g2.setColor(new Color(215, 48, 48));
        g2.fillOval(x, y, size, size);

        g2.setColor(new Color(125, 60, 20));
        g2.fillRect(x + size / 2 - 1, y - 4, 3, 7);

        g2.setColor(new Color(80, 150, 80));
        g2.fillOval(x + size / 2 + 2, y - 2, 8, 5);
    }
}