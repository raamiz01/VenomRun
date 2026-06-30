/*
 * File: Main.java
 * Project: Venom Run
 * Authors: Raamiz Abrar, Noah Hunt
 *
 * Purpose:
 * Starts the Venom Run program.
 *
 * References used for understanding Java/Swing/database concepts:
 * - Oracle Java Swing: https://docs.oracle.com/javase/tutorial/uiswing/
 * - Oracle JFrame: https://docs.oracle.com/javase/tutorial/uiswing/components/frame.html
 * - UCanAccess: https://ucanaccess.sourceforge.net/site.html
 */
public class Main {
    public static void main(String[] args) {
        DBUtil.initializeDatabase();
        new MainMenuFrame().setVisible(true);
    }
}
