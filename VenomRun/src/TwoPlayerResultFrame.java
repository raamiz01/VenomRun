import javax.swing.*;
import java.awt.*;

/*
 * File: TwoPlayerResultFrame.java
 * Project: Venom Run
 * Authors: Raamiz Abrar, Arham Rashad, Noah Hunt
 *
 * Purpose:
 * Shows the end screen for 2-player mode and compares both scores.
 */
public class TwoPlayerResultFrame extends JFrame {
    public TwoPlayerResultFrame(int score1, int score2, Difficulty difficulty) {
        setTitle("Venom Run - 2 Player Results");
        setSize(980, 680);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setContentPane(buildUI(score1, score2, difficulty));
    }

    private JPanel buildUI(int score1, int score2, Difficulty difficulty) {
        JPanel root = new JPanel(new GridBagLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                GradientPaint gp = new GradientPaint(0, 0, new Color(56, 22, 22), 0, getHeight(), Theme.BG_DARK);
                g2.setPaint(gp);
                g2.fillRect(0, 0, getWidth(), getHeight());
            }
        };

        JPanel card = new JPanel();
        card.setPreferredSize(new Dimension(540, 330));
        card.setBackground(new Color(45, 28, 28));
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Theme.ACCENT_GREEN, 2),
                BorderFactory.createEmptyBorder(25, 25, 25, 25)
        ));
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));

        JLabel title = new JLabel("2 PLAYER RESULTS", SwingConstants.CENTER);
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        title.setFont(Theme.titleFont(32));
        title.setForeground(Theme.ACCENT_GREEN);
        card.add(title);
        card.add(Box.createVerticalStrut(18));

        JLabel diff = new JLabel("Difficulty: " + difficulty.getDisplayName());
        diff.setAlignmentX(Component.CENTER_ALIGNMENT);
        diff.setForeground(Theme.TEXT_LIGHT);
        diff.setFont(Theme.bodyFont(18));
        card.add(diff);
        card.add(Box.createVerticalStrut(18));

        JLabel p1 = new JLabel("Player 1 Score: " + score1);
        p1.setAlignmentX(Component.CENTER_ALIGNMENT);
        p1.setForeground(Theme.PLAYER_ONE);
        p1.setFont(Theme.bodyFont(22));
        card.add(p1);
        card.add(Box.createVerticalStrut(12));

        JLabel p2 = new JLabel("Player 2 Score: " + score2);
        p2.setAlignmentX(Component.CENTER_ALIGNMENT);
        p2.setForeground(Theme.PLAYER_TWO);
        p2.setFont(Theme.bodyFont(22));
        card.add(p2);
        card.add(Box.createVerticalStrut(18));

        JLabel winner = new JLabel(getWinnerText(score1, score2));
        winner.setAlignmentX(Component.CENTER_ALIGNMENT);
        winner.setForeground(Theme.TEXT_LIGHT);
        winner.setFont(Theme.titleFont(24));
        card.add(winner);
        card.add(Box.createVerticalStrut(24));

        JPanel buttons = new JPanel();
        buttons.setOpaque(false);
        JButton playAgain = Theme.makeButton("PLAY AGAIN");
        JButton menu = Theme.makeButton("MAIN MENU");
        buttons.add(playAgain);
        buttons.add(menu);
        card.add(buttons);

        playAgain.addActionListener(e -> {
            new GameFrame(difficulty, true).setVisible(true);
            dispose();
        });
        menu.addActionListener(e -> {
            new MainMenuFrame().setVisible(true);
            dispose();
        });

        root.add(card);
        return root;
    }

    private String getWinnerText(int score1, int score2) {
        if (score1 > score2) {
            return "Player 1 Wins!";
        } else if (score2 > score1) {
            return "Player 2 Wins!";
        }
        return "It's a Tie!";
    }
}
