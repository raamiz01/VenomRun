import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.Random;

/*
 * File: GamePanel.java
 * Project: Venom Run
 * Authors: Raamiz Abrar, Arham Rashad, Noah Hunt, Abbas Haider
 *
 * Purpose:
 * Handles the main Snake gameplay, custom graphics, collisions, food,
 * obstacles, score tracking, and screen transitions after the game.
 *
 * References:
 * - Oracle JPanel custom painting: https://docs.oracle.com/javase/tutorial/uiswing/painting/
 * - Oracle Key Bindings: https://docs.oracle.com/javase/tutorial/uiswing/misc/keybinding.html
 * - Oracle Swing Timer: https://docs.oracle.com/javase/tutorial/uiswing/misc/timer.html
 */
public class GamePanel extends JPanel {
    private final int PANEL_WIDTH = 980;
    private final int PANEL_HEIGHT = 700;

    private final int BOARD_X = 70;
    private final int BOARD_Y = 120;
    private final int BOARD_WIDTH = 840;
    private final int BOARD_HEIGHT = 510;

    private GameFrame gameFrame;
    private Difficulty difficulty;
    private boolean twoPlayer;

    private int cellSize;
    private int rows;
    private int cols;

    private Timer timer;
    private Random random = new Random();

    private ArrayList<Point> snake1 = new ArrayList<>();
    private ArrayList<Point> snake2 = new ArrayList<>();
    private ArrayList<Point> foods = new ArrayList<>();
    private ArrayList<Point> obstacles = new ArrayList<>();

    private int dx1 = 1;
    private int dy1 = 0;

    private int dx2 = -1;
    private int dy2 = 0;

    private int score1 = 0;
    private int score2 = 0;

    private boolean player1Alive = true;
    private boolean player2Alive = true;

    public GamePanel(GameFrame gameFrame, Difficulty difficulty, boolean twoPlayer) {
        this.gameFrame = gameFrame;
        this.difficulty = difficulty;
        this.twoPlayer = twoPlayer;

        this.cellSize = difficulty.getCellSize();
        this.rows = BOARD_HEIGHT / cellSize;
        this.cols = BOARD_WIDTH / cellSize;

        setPreferredSize(new Dimension(PANEL_WIDTH, PANEL_HEIGHT));
        setBackground(Theme.BG_DARK);
        setFocusable(true);

        setupKeyBindings();
        startGame();
    }

    private void startGame() {
        snake1.clear();
        snake2.clear();
        foods.clear();
        obstacles.clear();

        score1 = 0;
        score2 = 0;

        player1Alive = true;
        player2Alive = twoPlayer;

        dx1 = 1;
        dy1 = 0;

        dx2 = -1;
        dy2 = 0;

        int centerRow = rows / 2;

        snake1.add(new Point(cols / 4, centerRow));
        snake1.add(new Point(cols / 4 - 1, centerRow));
        snake1.add(new Point(cols / 4 - 2, centerRow));

        if (twoPlayer) {
            snake2.add(new Point((cols * 3) / 4, centerRow));
            snake2.add(new Point((cols * 3) / 4 + 1, centerRow));
            snake2.add(new Point((cols * 3) / 4 + 2, centerRow));
        }

        createObstacles();

        int foodCount = difficulty.getFoodCount(twoPlayer);

        for (int i = 0; i < foodCount; i++) {
            spawnFood();
        }

        timer = new Timer(difficulty.getDelay(), e -> gameLoop());
        timer.start();
    }

    private void setupKeyBindings() {
        bindKey("W", "p1Up", () -> changeDirection(1, 0, -1));
        bindKey("S", "p1Down", () -> changeDirection(1, 0, 1));
        bindKey("A", "p1Left", () -> changeDirection(1, -1, 0));
        bindKey("D", "p1Right", () -> changeDirection(1, 1, 0));

        bindKey("UP", "p2Up", () -> changeDirection(2, 0, -1));
        bindKey("DOWN", "p2Down", () -> changeDirection(2, 0, 1));
        bindKey("LEFT", "p2Left", () -> changeDirection(2, -1, 0));
        bindKey("RIGHT", "p2Right", () -> changeDirection(2, 1, 0));
    }

    private void bindKey(String key, String actionName, Runnable action) {
        getInputMap(WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(key), actionName);

        getActionMap().put(actionName, new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                action.run();
            }
        });
    }

    private void changeDirection(int player, int newDx, int newDy) {
        if (player == 1) {
            if (dx1 + newDx == 0 && dy1 + newDy == 0) {
                return;
            }

            dx1 = newDx;
            dy1 = newDy;

        } else if (twoPlayer && player2Alive) {
            if (dx2 + newDx == 0 && dy2 + newDy == 0) {
                return;
            }

            dx2 = newDx;
            dy2 = newDy;
        }
    }

    private void gameLoop() {
        if (player1Alive) {
            moveSnake(1);
        }

        if (twoPlayer && player2Alive) {
            moveSnake(2);
        }

        if (twoPlayer && !player1Alive && !player2Alive) {
            timer.stop();
            new TwoPlayerResultFrame(score1, score2, difficulty).setVisible(true);
            gameFrame.dispose();
            return;
        }

        if (!twoPlayer && !player1Alive) {
            timer.stop();
            new GameOverFrame(score1, difficulty).setVisible(true);
            gameFrame.dispose();
            return;
        }

        repaint();
    }

    private void moveSnake(int player) {
        ArrayList<Point> snake = (player == 1) ? snake1 : snake2;

        int dx = (player == 1) ? dx1 : dx2;
        int dy = (player == 1) ? dy1 : dy2;

        Point head = snake.get(0);
        Point newHead = new Point(head.x + dx, head.y + dy);

        if (isDead(newHead, player)) {
            if (player == 1) {
                player1Alive = false;
            } else {
                player2Alive = false;
            }

            return;
        }

        snake.add(0, newHead);

        int foodIndex = getFoodIndex(newHead);

        if (foodIndex != -1) {
            if (player == 1) {
                score1 += 10;
            } else {
                score2 += 10;
            }

            foods.remove(foodIndex);
            spawnFood();

        } else {
            snake.remove(snake.size() - 1);
        }
    }

    private boolean isDead(Point head, int player) {
        if (head.x < 0 || head.x >= cols || head.y < 0 || head.y >= rows) {
            return true;
        }

        if (obstacles.contains(head)) {
            return true;
        }

        ArrayList<Point> ownSnake = player == 1 ? snake1 : snake2;
        ArrayList<Point> otherSnake = player == 1 ? snake2 : snake1;

        for (int i = 1; i < ownSnake.size(); i++) {
            if (ownSnake.get(i).equals(head)) {
                return true;
            }
        }

        if (twoPlayer) {
            for (Point point : otherSnake) {
                if (point.equals(head)) {
                    return true;
                }
            }
        }

        return false;
    }

    private int getFoodIndex(Point head) {
        for (int i = 0; i < foods.size(); i++) {
            if (foods.get(i).equals(head)) {
                return i;
            }
        }

        return -1;
    }

    private void createObstacles() {
        int count = difficulty.getObstacleCount();

        for (int i = 0; i < count; i++) {
            Point p = getRandomEmptyPoint();
            obstacles.add(p);
        }
    }

    private void spawnFood() {
        foods.add(getRandomEmptyPoint());
    }

    private Point getRandomEmptyPoint() {
        Point p;

        do {
            p = new Point(random.nextInt(cols), random.nextInt(rows));
        } while (snake1.contains(p) || snake2.contains(p) || foods.contains(p) || obstacles.contains(p));

        return p;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2 = (Graphics2D) g;

        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        GradientPaint gp = new GradientPaint(0, 0, new Color(56, 22, 22), 0, getHeight(), Theme.BG_DARK);
        g2.setPaint(gp);
        g2.fillRect(0, 0, getWidth(), getHeight());

        drawHeader(g2);
        drawBoard(g2);
        drawGrid(g2);
        drawObstacles(g2);
        drawFoods(g2);

        drawSnake(g2, snake1, Theme.PLAYER_ONE, player1Alive, true);

        if (twoPlayer) {
            drawSnake(g2, snake2, Theme.PLAYER_TWO, player2Alive, false);
        }
    }

    private void drawHeader(Graphics2D g2) {
        g2.setColor(Theme.ACCENT_GREEN);
        g2.setFont(Theme.titleFont(26));

        String modeText = twoPlayer ? "2 PLAYER" : "1 PLAYER";
        g2.drawString(modeText, 70, 52);

        String diffText = difficulty.getDisplayName().toUpperCase();
        int diffWidth = g2.getFontMetrics().stringWidth(diffText);

        g2.drawString(diffText, PANEL_WIDTH / 2 - diffWidth / 2, 52);

        if (twoPlayer) {
            g2.drawString("P1: " + score1 + "    P2: " + score2, 710, 52);

            g2.setFont(Theme.bodyFont(14));
            g2.setColor(Theme.TEXT_LIGHT);
            g2.drawString("WASD / ARROWS", 760, 75);
        } else {
            g2.drawString("SCORE: " + score1, 780, 52);

            g2.setFont(Theme.bodyFont(14));
            g2.setColor(Theme.TEXT_LIGHT);
            g2.drawString("WASD TO MOVE", 770, 75);
        }
    }

    private void drawBoard(Graphics2D g2) {
        g2.setColor(new Color(35, 23, 23));
        g2.fillRoundRect(BOARD_X - 8, BOARD_Y - 8, BOARD_WIDTH + 16, BOARD_HEIGHT + 16, 24, 24);

        g2.setColor(Theme.GRID_BG);
        g2.fillRoundRect(BOARD_X, BOARD_Y, BOARD_WIDTH, BOARD_HEIGHT, 18, 18);
    }

    private void drawGrid(Graphics2D g2) {
        g2.setColor(Theme.GRID_LINE);

        for (int x = 0; x <= cols; x++) {
            int drawX = BOARD_X + x * cellSize;
            g2.drawLine(drawX, BOARD_Y, drawX, BOARD_Y + rows * cellSize);
        }

        for (int y = 0; y <= rows; y++) {
            int drawY = BOARD_Y + y * cellSize;
            g2.drawLine(BOARD_X, drawY, BOARD_X + cols * cellSize, drawY);
        }
    }

    private void drawObstacles(Graphics2D g2) {
        for (Point p : obstacles) {
            int x = BOARD_X + p.x * cellSize;
            int y = BOARD_Y + p.y * cellSize;

            g2.setColor(Theme.WALL_COLOR);
            g2.fillRoundRect(x + 2, y + 2, cellSize - 4, cellSize - 4, 8, 8);

            g2.setColor(Theme.WALL_DARK);
            g2.drawRoundRect(x + 2, y + 2, cellSize - 4, cellSize - 4, 8, 8);
            g2.drawLine(x + cellSize / 2, y + 5, x + cellSize / 2, y + cellSize - 5);
            g2.drawLine(x + 5, y + cellSize / 2, x + cellSize - 5, y + cellSize / 2);
        }
    }

    private void drawFoods(Graphics2D g2) {
        for (Point p : foods) {
            int x = BOARD_X + p.x * cellSize;
            int y = BOARD_Y + p.y * cellSize;

            int size = Math.max(12, cellSize - 8);
            int offset = (cellSize - size) / 2;

            int ax = x + offset;
            int ay = y + offset;

            g2.setColor(new Color(215, 48, 48));
            g2.fillOval(ax, ay, size, size);

            g2.setColor(new Color(170, 32, 32));
            g2.drawOval(ax, ay, size, size);

            g2.setColor(new Color(125, 60, 20));
            g2.fillRect(ax + size / 2 - 1, ay - 5, 3, 8);

            g2.setColor(new Color(80, 150, 80));
            g2.fillOval(ax + size / 2 + 3, ay - 2, 8, 5);
        }
    }

    private void drawSnake(Graphics2D g2, ArrayList<Point> snake, Color color, boolean alive, boolean isPlayerOne) {
        if (snake.isEmpty()) {
            return;
        }

        Color fill = alive ? color : color.darker().darker();
        Color border = fill.darker();

        /*
         * Draws thick connected lines between the centers of each segment.
         * This makes the snake look connected instead of like separate squares.
         */
        Stroke oldStroke = g2.getStroke();

        g2.setStroke(new BasicStroke(
                Math.max(8, cellSize - 6),
                BasicStroke.CAP_ROUND,
                BasicStroke.JOIN_ROUND
        ));

        g2.setColor(fill);

        for (int i = 0; i < snake.size() - 1; i++) {
            Point current = snake.get(i);
            Point next = snake.get(i + 1);

            int x1 = BOARD_X + current.x * cellSize + cellSize / 2;
            int y1 = BOARD_Y + current.y * cellSize + cellSize / 2;

            int x2 = BOARD_X + next.x * cellSize + cellSize / 2;
            int y2 = BOARD_Y + next.y * cellSize + cellSize / 2;

            g2.drawLine(x1, y1, x2, y2);
        }

        g2.setStroke(oldStroke);

        /*
         * Draws circular body pieces on top of the connected line.
         */
        for (Point p : snake) {
            int x = BOARD_X + p.x * cellSize + 3;
            int y = BOARD_Y + p.y * cellSize + 3;
            int size = cellSize - 6;

            g2.setColor(fill);
            g2.fillOval(x, y, size, size);

            g2.setColor(border);
            g2.drawOval(x, y, size, size);
        }

        /*
         * Draws the head.
         */
        Point head = snake.get(0);

        int hx = BOARD_X + head.x * cellSize + 2;
        int hy = BOARD_Y + head.y * cellSize + 2;
        int headSize = cellSize - 4;

        g2.setColor(fill);
        g2.fillOval(hx, hy, headSize, headSize);

        g2.setColor(border);
        g2.drawOval(hx, hy, headSize, headSize);

        /*
         * Gets the direction so the eyes face the direction the snake is moving.
         */
        int dx;
        int dy;

        if (isPlayerOne) {
            dx = dx1;
            dy = dy1;
        } else {
            dx = dx2;
            dy = dy2;
        }

        drawSnakeEyes(g2, hx, hy, headSize, dx, dy);
    }

    private void drawSnakeEyes(Graphics2D g2, int hx, int hy, int size, int dx, int dy) {
        /*
        * Eyes scale based on the head size.
        * This keeps them looking normal in Easy, Medium, and Hard mode.
        */
        int eyeSize = Math.max(3, size / 4);
        int pupilSize = Math.max(1, eyeSize / 2);

        int padding = Math.max(2, size / 6);

        int eye1X;
        int eye1Y;
        int eye2X;
        int eye2Y;

        if (dx == 1) {
            /*
            * Moving right:
            * Eyes are placed near the right side of the head.
            */
            eye1X = hx + size - eyeSize - padding;
            eye1Y = hy + padding;

            eye2X = hx + size - eyeSize - padding;
            eye2Y = hy + size - eyeSize - padding;

        } else if (dx == -1) {
            /*
            * Moving left:
            * Eyes are placed near the left side of the head.
            */
            eye1X = hx + padding;
            eye1Y = hy + padding;

            eye2X = hx + padding;
            eye2Y = hy + size - eyeSize - padding;

        } else if (dy == -1) {
            /*
            * Moving up:
            * Eyes are placed near the top of the head.
            */
            eye1X = hx + padding;
            eye1Y = hy + padding;

            eye2X = hx + size - eyeSize - padding;
            eye2Y = hy + padding;

        } else {
            /*
            * Moving down:
            * Eyes are placed near the bottom of the head.
            */
            eye1X = hx + padding;
            eye1Y = hy + size - eyeSize - padding;

            eye2X = hx + size - eyeSize - padding;
            eye2Y = hy + size - eyeSize - padding;
        }

        /*
        * Draw the white part of both eyes.
        */
        g2.setColor(Color.WHITE);
        g2.fillOval(eye1X, eye1Y, eyeSize, eyeSize);
        g2.fillOval(eye2X, eye2Y, eyeSize, eyeSize);

        /*
        * Draw the black pupils.
        */
        g2.setColor(Color.BLACK);
        g2.fillOval(
                eye1X + eyeSize / 2,
                eye1Y + eyeSize / 2,
                pupilSize,
                pupilSize
        );

        g2.fillOval(
                eye2X + eyeSize / 2,
                eye2Y + eyeSize / 2,
                pupilSize,
                pupilSize
        );
    }
}