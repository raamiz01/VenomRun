/*
 * File: Difficulty.java
 * Project: Venom Run
 * Authors: Raamiz Abrar, Arham Rashad, Noah Hunt
 *
 * Purpose:
 * Stores the difficulty levels and their game settings.
 */
public enum Difficulty {
    EASY,
    MEDIUM,
    HARD;

    public int getCellSize() {
        if (this == EASY) {
            return 30;
        } else if (this == MEDIUM) {
            return 20;
        }
        return 15;
    }

    public int getDelay() {
        if (this == HARD) {
            return 75;
        }
        return 110;
    }

    public int getFoodCount(boolean twoPlayer) {
        if (this == EASY) {
            return twoPlayer ? 2 : 1;
        } else if (this == MEDIUM) {
            return twoPlayer ? 4 : 2;
        }
        return twoPlayer ? 8 : 4;
    }

    public int getObstacleCount() {
        if (this == EASY) {
            return 0;
        } else if (this == MEDIUM) {
            return 12;
        }
        return 25;
    }

    public String getDisplayName() {
        if (this == EASY) {
            return "Easy";
        } else if (this == MEDIUM) {
            return "Medium";
        }
        return "Hard";
    }
}
