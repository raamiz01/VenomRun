/*
 * File: ScoreEntry.java
 * Project: Venom Run
 * Authors: Arham Rashad, Noah Hunt
 *
 * Purpose:
 * Stores one leaderboard score entry.
 */
public class ScoreEntry {
    private int scoreID;
    private int rank;
    private String playerName;
    private int score;

    public ScoreEntry(int scoreID, int rank, String playerName, int score) {
        this.scoreID = scoreID;
        this.rank = rank;
        this.playerName = playerName;
        this.score = score;
    }

    public int getScoreID() {
        return scoreID;
    }

    public int getRank() {
        return rank;
    }

    public String getPlayerName() {
        return playerName;
    }

    public int getScore() {
        return score;
    }
}
