import javax.swing.*;

/*
 * File: GameFrame.java
 * Project: Venom Run
 * Authors: Raamiz Abrar, Noah Hunt
 *
 * Purpose:
 * Holds the main gameplay panel inside a window.
 */
public class GameFrame extends JFrame {

    public GameFrame(Difficulty difficulty, boolean twoPlayer) {
        setTitle("Venom Run - " + difficulty.getDisplayName());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        add(new GamePanel(this, difficulty, twoPlayer));
        pack();
        setLocationRelativeTo(null);
    }
}
