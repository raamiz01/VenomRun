import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

/*
 * File: LeaderboardFrame.java
 * Project: Venom Run
 * Authors: Raamiz Abrar, Noah Hunt
 *
 * Purpose:
 * Displays leaderboard scores from the Microsoft Access database.
 *
 * References:
 * - Oracle JTable help: https://docs.oracle.com/javase/tutorial/uiswing/components/table.html
 */
public class LeaderboardFrame extends JFrame {
    private Difficulty currentDifficulty;
    private int focusScoreId;

    private JTable table;
    private DefaultTableModel model;
    private JLabel titleLabel;

    private int highlightedModelRow = -1;

    public LeaderboardFrame(Difficulty difficulty, int focusScoreId) {
        this.currentDifficulty = difficulty;
        this.focusScoreId = focusScoreId;

        setTitle("Venom Run - Leaderboard");
        setSize(980, 680);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setContentPane(buildUI());

        loadScores();
    }

    private JPanel buildUI() {
        JPanel root = new JPanel(new BorderLayout()) {
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

        JPanel top = new JPanel(new BorderLayout());
        top.setOpaque(false);
        top.setBorder(BorderFactory.createEmptyBorder(28, 35, 10, 35));

        titleLabel = new JLabel("LEADERBOARD - " + currentDifficulty.getDisplayName().toUpperCase(), SwingConstants.CENTER);
        titleLabel.setFont(Theme.titleFont(34));
        titleLabel.setForeground(Theme.ACCENT_GREEN);

        top.add(titleLabel, BorderLayout.CENTER);
        root.add(top, BorderLayout.NORTH);

        JPanel centerWrap = new JPanel(new GridBagLayout());
        centerWrap.setOpaque(false);

        JPanel centerCard = new JPanel(new BorderLayout(10, 10));
        centerCard.setPreferredSize(new Dimension(700, 500));
        centerCard.setBackground(Theme.BG_CARD);

        centerCard.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Theme.ACCENT_GREEN, 2),
                BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));

        JLabel infoLabel = new JLabel("Your score is highlighted if it was just saved.", SwingConstants.CENTER);
        infoLabel.setFont(Theme.bodyFont(18));
        infoLabel.setForeground(Theme.TEXT_LIGHT);

        centerCard.add(infoLabel, BorderLayout.NORTH);

        model = new DefaultTableModel(new String[] { "Rank", "Name", "Score" }, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        table = new JTable(model);

        /*
         * Bigger rows make the leaderboard fill the box better.
         * The scroll pane still allows the user to scroll through all entries.
         */
        table.setRowHeight(65);
        table.setFont(Theme.bodyFont(24));
        table.setBackground(new Color(36, 25, 25));
        table.setForeground(Theme.TEXT_LIGHT);
        table.setGridColor(Theme.ACCENT_GREEN_DARK);
        table.setFillsViewportHeight(true);

        table.getTableHeader().setFont(Theme.bodyFont(24));
        table.getTableHeader().setBackground(Theme.ACCENT_GREEN_DARK);
        table.getTableHeader().setForeground(Color.WHITE);
        table.getTableHeader().setPreferredSize(new Dimension(0, 55));

        DefaultTableCellRenderer renderer = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(
                    JTable table,
                    Object value,
                    boolean isSelected,
                    boolean hasFocus,
                    int row,
                    int column
            ) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

                setHorizontalAlignment(SwingConstants.CENTER);

                if (!isSelected) {
                    if (row == highlightedModelRow) {
                        c.setBackground(new Color(86, 126, 74));
                        c.setForeground(Color.WHITE);
                    } else {
                        c.setBackground(new Color(36, 25, 25));
                        c.setForeground(Theme.TEXT_LIGHT);
                    }
                }

                return c;
            }
        };

        for (int i = 0; i < 3; i++) {
            table.getColumnModel().getColumn(i).setCellRenderer(renderer);
        }

        /*
         * This height fits about 5 big rows plus the header.
         * If there are more entries, the user can scroll.
         */
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setPreferredSize(new Dimension(620, 385));

        scrollPane.getViewport().setBackground(new Color(36, 25, 25));
        scrollPane.setBackground(new Color(36, 25, 25));

        centerCard.add(scrollPane, BorderLayout.CENTER);

        JPanel bottomButtons = new JPanel();
        bottomButtons.setOpaque(false);

        JButton playAgain = Theme.makeButton("PLAY AGAIN");
        JButton endGame = Theme.makeButton("END GAME");

        bottomButtons.add(playAgain);
        bottomButtons.add(Box.createHorizontalStrut(20));
        bottomButtons.add(endGame);

        centerCard.add(bottomButtons, BorderLayout.SOUTH);

        playAgain.addActionListener(e -> {
            new GameFrame(currentDifficulty, false).setVisible(true);
            dispose();
        });

        endGame.addActionListener(e -> {
            new MainMenuFrame().setVisible(true);
            dispose();
        });

        centerWrap.add(centerCard);
        root.add(centerWrap, BorderLayout.CENTER);

        return root;
    }

    private void loadScores() {
        highlightedModelRow = -1;
        model.setRowCount(0);

        titleLabel.setText("LEADERBOARD - " + currentDifficulty.getDisplayName().toUpperCase());

        ArrayList<ScoreEntry> scores = new ArrayList<>();
        String tableName = DBUtil.getTableName(currentDifficulty);

        try (Connection conn = DBUtil.getConnection()) {
            String sql = "SELECT ScoreID, PlayerName, Score FROM " + tableName + " ORDER BY Score DESC";
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            int rank = 1;

            while (rs.next()) {
                scores.add(new ScoreEntry(
                        rs.getInt("ScoreID"),
                        rank,
                        rs.getString("PlayerName"),
                        rs.getInt("Score")
                ));

                rank++;
            }

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error loading leaderboard: " + ex.getMessage());
            return;
        }

        /*
         * Loads every score instead of only 5.
         * This allows the user to scroll through the full leaderboard.
         */
        for (int i = 0; i < scores.size(); i++) {
            ScoreEntry entry = scores.get(i);
            addScoreToTable(entry);

            if (entry.getScoreID() == focusScoreId) {
                highlightedModelRow = i;
            }
        }

        table.repaint();

        /*
         * Automatically scrolls to the saved score if there is one.
         */
        if (highlightedModelRow != -1) {
            SwingUtilities.invokeLater(() -> {
                Rectangle rowRectangle = table.getCellRect(highlightedModelRow, 0, true);
                table.scrollRectToVisible(rowRectangle);
            });
        }
    }

    private void addScoreToTable(ScoreEntry entry) {
        model.addRow(new Object[] {
                entry.getRank(),
                entry.getPlayerName(),
                entry.getScore()
        });
    }
}