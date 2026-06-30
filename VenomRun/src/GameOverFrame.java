import javax.swing.*;
import java.awt.*;

/*
 * File: GameOverFrame.java
 * Project: Venom Run
 * Authors: Arham Rashad
 *
 * Purpose:
 * Shows a game-over screen where the single-player user enters a name.
 * The player must enter a name before continuing to the leaderboard.
 */
public class GameOverFrame extends JFrame {
    private JTextField nameField;
    private int score;
    private Difficulty difficulty;

    public GameOverFrame(int score, Difficulty difficulty) {
        this.score = score;
        this.difficulty = difficulty;

        setTitle("Venom Run - Game Over");
        setSize(980, 680);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setContentPane(buildUI());
    }

    private JPanel buildUI() {
        JPanel root = new JPanel(new GridBagLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);

                Graphics2D g2 = (Graphics2D) g;

                GradientPaint gp = new GradientPaint(
                        0,
                        0,
                        new Color(56, 22, 22),
                        0,
                        getHeight(),
                        Theme.BG_DARK
                );

                g2.setPaint(gp);
                g2.fillRect(0, 0, getWidth(), getHeight());
            }
        };

        GridBagConstraints gbc = new GridBagConstraints();

        JPanel card = new JPanel();
        card.setPreferredSize(new Dimension(520, 320));
        card.setBackground(new Color(45, 28, 28));

        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Theme.ACCENT_GREEN, 2),
                BorderFactory.createEmptyBorder(25, 25, 25, 25)
        ));

        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));

        JLabel title = new JLabel("GAME OVER", SwingConstants.CENTER);
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        title.setFont(Theme.titleFont(34));
        title.setForeground(Theme.ACCENT_GREEN);

        card.add(title);
        card.add(Box.createVerticalStrut(18));

        JLabel scoreLabel = new JLabel("Score: " + score + "    Difficulty: " + difficulty.getDisplayName());
        scoreLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        scoreLabel.setFont(Theme.bodyFont(18));
        scoreLabel.setForeground(Theme.TEXT_LIGHT);

        card.add(scoreLabel);
        card.add(Box.createVerticalStrut(24));

        JLabel nameLabel = new JLabel("Enter your name or initials");
        nameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        nameLabel.setFont(Theme.bodyFont(18));
        nameLabel.setForeground(Theme.TEXT_LIGHT);

        card.add(nameLabel);
        card.add(Box.createVerticalStrut(14));

        nameField = new JTextField();
        nameField.setMaximumSize(new Dimension(260, 38));
        nameField.setFont(Theme.bodyFont(18));
        nameField.setHorizontalAlignment(JTextField.CENTER);

        card.add(nameField);
        card.add(Box.createVerticalStrut(20));

        /*
         * Only one button is shown.
         * The player cannot continue unless they enter a name.
         */
        JButton continueButton = Theme.makeButton("CONTINUE");
        continueButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        continueButton.addActionListener(e -> saveScoreAndContinue());

        card.add(continueButton);

        root.add(card, gbc);

        return root;
    }

    private void saveScoreAndContinue() {
        String name = nameField.getText().trim();

        /*
         * Input validation:
         * The player must enter a name or initials before continuing.
         */
        if (name.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter your name or initials before continuing.");
            return;
        }

        /*
         * Same names are allowed because this is an arcade-style leaderboard,
         * not an account/login system.
         * Every completed game saves as a separate score entry.
         */
        int newScoreId = DBUtil.saveScore(difficulty, name, score);

        new LeaderboardFrame(difficulty, newScoreId).setVisible(true);
        dispose();
    }
}