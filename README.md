# Venom Run

A desktop Snake game built with Java Swing, featuring 1-player and 2-player modes, three difficulty levels, obstacles, and a persistent leaderboard stored in a Microsoft Access database.

## Features

- **1 Player and 2 Player modes** — head-to-head snake battles with independent controls
- **Three difficulty levels** (Easy, Medium, Hard) — each with its own grid size, speed, food count, and obstacle count
- **Custom Swing graphics** — hand-drawn snakes, apples, and obstacles rendered with `Graphics2D`
- **Persistent leaderboard** — scores are saved to and read from a Microsoft Access `.accdb` database via UCanAccess/JDBC
- **Full game flow** — main menu → difficulty select → controls/preview screen → gameplay → game over / results → leaderboard

## Controls

| Player | Up | Down | Left | Right |
|--------|----|------|------|-------|
| Player 1 | W | S | A | D |
| Player 2 | ↑ | ↓ | ← | → |

- Eat apples to score points.
- Avoid walls, your own body, obstacles, and (in 2-player mode) the other snake.
- In 2-player mode, if one player loses, the other keeps playing until they also lose.

## Project Structure

```
VenomRun/
├── Main.java                  # Entry point — initializes the DB and shows the main menu
├── MainMenuFrame.java         # Main menu UI (mode selection)
├── DifficultyFrame.java       # Difficulty selection screen
├── ThingsToKnowFrame.java     # Controls/preview screen shown before gameplay
├── GameFrame.java             # Window that hosts the gameplay panel
├── GamePanel.java             # Core gameplay loop, rendering, collisions, input
├── GameOverFrame.java         # Single-player end screen (name entry)
├── TwoPlayerResultFrame.java  # Two-player end screen (winner comparison)
├── LeaderboardFrame.java      # Leaderboard display, pulled from the database
├── Difficulty.java            # Enum defining settings per difficulty
├── ScoreEntry.java            # Simple model for a leaderboard row
├── Theme.java                 # Shared colors, fonts, and button styling
├── DBUtil.java                # Database connection and score persistence logic
├── VenomRun.accdb             # Microsoft Access database (leaderboard storage)
└── lib/                       # Required JAR dependencies (see below)
```

## Requirements

- **Java JDK 8+**
- The following JAR files on the classpath:
  - `ucanaccess-5_0_1.jar`
  - `jackcess-3_0_1.jar`
  - `hsqldb-2_5_0.jar`
  - `commons-lang3-3_8_1.jar`
  - `commons-logging-1_2.jar`

## Running the Game

1. Make sure `VenomRun.accdb` is in the project's working directory (same folder you run the program from).
2. Compile all `.java` files with the JARs on the classpath:

   ```bash
   javac -cp ".:lib/*" *.java
   ```

   *(On Windows, use `;` instead of `:` as the classpath separator.)*

3. Run the game:

   ```bash
   java -cp ".:lib/*" Main
   ```

On startup, `Main` initializes the leaderboard tables (if they don't already exist) and opens the main menu.

## How the Leaderboard Works

- Each difficulty has its own table (`tblEasyScores`, `tblMediumScores`, `tblHardScores`) in `VenomRun.accdb`.
- When a single-player game ends, the player enters a name and their score is inserted into the matching table.
- The leaderboard screen queries the relevant table, sorts by score descending, and highlights the row that was just saved.
- Duplicate names are allowed — this is an arcade-style leaderboard, not an account system, so every completed game is logged as its own entry.

## Authors

Raamiz Abrar

## License

N/A
