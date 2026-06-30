import javax.swing.*;
import java.awt.*;

/*
 * File: DifficultyFrame.java
 * Project: Venom Run
 * Authors: Arham Rashad, Abbas Haider
 *
 * Purpose:
 * Lets the user choose Easy, Medium, or Hard on a full screen instead of a popup.
 */
public class DifficultyFrame extends JFrame {
    private boolean twoPlayer;

    public DifficultyFrame(boolean twoPlayer) {
        this.twoPlayer = twoPlayer;
        setTitle("Venom Run - Difficulty");
        setSize(980, 680);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setContentPane(buildUI());
    }

    private JPanel buildUI() {
        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(Theme.BG_DARK);

        JLabel title = new JLabel("DIFFICULTY LEVELS", SwingConstants.CENTER);
        title.setFont(Theme.titleFont(34));
        title.setForeground(Theme.ACCENT_GREEN);
        title.setBorder(BorderFactory.createEmptyBorder(30, 10, 15, 10));
        root.add(title, BorderLayout.NORTH);

        JPanel options = new JPanel(new GridBagLayout());
        options.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 20, 10, 20);

        gbc.gridx = 0;
        options.add(new DifficultyCard(Difficulty.EASY, twoPlayer), gbc);
        gbc.gridx = 1;
        options.add(new DifficultyCard(Difficulty.MEDIUM, twoPlayer), gbc);
        gbc.gridx = 2;
        options.add(new DifficultyCard(Difficulty.HARD, twoPlayer), gbc);
        root.add(options, BorderLayout.CENTER);

        JPanel bottom = new JPanel();
        bottom.setOpaque(false);
        JButton back = Theme.makeButton("BACK");
        back.addActionListener(e -> {
            new MainMenuFrame().setVisible(true);
            dispose();
        });
        bottom.add(back);
        bottom.setBorder(BorderFactory.createEmptyBorder(10, 10, 25, 10));
        root.add(bottom, BorderLayout.SOUTH);
        return root;
    }

    private class DifficultyCard extends JPanel {
        public DifficultyCard(Difficulty difficulty, boolean twoPlayer) {
            setPreferredSize(new Dimension(260, 330));
            setBackground(Theme.BG_CARD);
            setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(Theme.ACCENT_GREEN, 2),
                    BorderFactory.createEmptyBorder(18, 18, 18, 18)
            ));
            setLayout(new BorderLayout());

            JLabel label = new JLabel(difficulty.getDisplayName().toUpperCase(), SwingConstants.CENTER);
            label.setFont(Theme.titleFont(24));
            label.setForeground(Theme.ACCENT_GREEN);
            add(label, BorderLayout.NORTH);

            JTextArea info = new JTextArea();
            info.setEditable(false);
            info.setOpaque(false);
            info.setForeground(Theme.TEXT_LIGHT);
            info.setFont(Theme.bodyFont(15));
            info.setLineWrap(true);
            info.setWrapStyleWord(true);
            info.setText(getDescription(difficulty));
            add(info, BorderLayout.CENTER);

            JButton choose = Theme.makeButton("CHOOSE");
            choose.addActionListener(e -> {
                new ThingsToKnowFrame(twoPlayer, difficulty).setVisible(true);
                dispose();
            });
            add(choose, BorderLayout.SOUTH);
        }
    }

    private String getDescription(Difficulty difficulty) {
        if (difficulty == Difficulty.EASY) {
            return "Regular speed\nBig grid boxes\n1 food in 1-player\n2 foods in 2-player\nNo obstacles";
        } else if (difficulty == Difficulty.MEDIUM) {
            return "Regular speed\nSmaller boxes\nRandom wall obstacles\n2+ foods in 1-player\n4+ foods in 2-player";
        }
        return "Faster speed\nSmallest boxes\nMore obstacles\n4+ foods in 1-player\n8+ foods in 2-player";
    }
}
