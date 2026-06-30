import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

/*
 * File: DBUtil.java
 * Project: Venom Run
 * Authors: Raamiz Abrar
 *
 * Purpose:
 * Handles the Microsoft Access database connection and leaderboard score saving.
 *
 * References:
 * - UCanAccess database connection help: https://ucanaccess.sourceforge.net/site.html
 * - Java JDBC basics: https://docs.oracle.com/javase/tutorial/jdbc/
 */
public class DBUtil {
    private static final String DB_FILE = "VenomRun.accdb";

    public static Connection getConnection() throws Exception {
        /*
         * Explicitly loads the UCanAccess JDBC driver.
         * This helps fix the "No suitable driver found" error when Java
         * does not automatically detect the driver from the classpath.
         */
        Class.forName("net.ucanaccess.jdbc.UcanaccessDriver");

        String url = "jdbc:ucanaccess://" + DB_FILE;
        return DriverManager.getConnection(url);
    }

    public static void initializeDatabase() {
        createScoreTable("tblEasyScores");
        createScoreTable("tblMediumScores");
        createScoreTable("tblHardScores");
    }

    private static void createScoreTable(String tableName) {
        String sql = "CREATE TABLE " + tableName + " (" +
                "ScoreID COUNTER PRIMARY KEY, " +
                "PlayerName TEXT(50), " +
                "Score INTEGER" +
                ")";

        try (Connection conn = getConnection();
             Statement st = conn.createStatement()) {
            st.executeUpdate(sql);
        } catch (Exception ex) {
            /*
             * If the table already exists, Access throws an error.
             * That is okay because we do not want to recreate or erase the table.
             */
        }
    }

    public static int saveScore(Difficulty difficulty, String playerName, int score) {
        String tableName = getTableName(difficulty);

        try (Connection conn = getConnection()) {
            String insertSQL = "INSERT INTO " + tableName + " (PlayerName, Score) VALUES (?, ?)";
            PreparedStatement ps = conn.prepareStatement(insertSQL);
            ps.setString(1, playerName);
            ps.setInt(2, score);
            ps.executeUpdate();

            String idSQL = "SELECT MAX(ScoreID) AS NewID FROM " + tableName;
            PreparedStatement idStatement = conn.prepareStatement(idSQL);
            ResultSet rs = idStatement.executeQuery();

            if (rs.next()) {
                return rs.getInt("NewID");
            }
        } catch (Exception ex) {
            System.out.println("Error saving score: " + ex.getMessage());
        }

        return -1;
    }

    public static String getTableName(Difficulty difficulty) {
        if (difficulty == Difficulty.EASY) {
            return "tblEasyScores";
        } else if (difficulty == Difficulty.MEDIUM) {
            return "tblMediumScores";
        }
        return "tblHardScores";
    }
}