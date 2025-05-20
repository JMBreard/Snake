import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.*;

public class Game extends JPanel implements ActionListener, KeyListener {
    private class Tile {
        int x, y;

        Tile(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }

    int boardWidth;
    int boardHeight;
    int tileSize = 25;

    // Snake
    Tile snakeHead;
    ArrayList<Tile> snakeBody;
    // Tile snakeSegment;

    // Food
    Tile target;
    Random random;

    // Game logic
    Timer gameLoop;
    int velocityX;
    int velocityY;
    boolean gameOver;
    int score;

    Game(int boardWidth, int boardHeight) {
        this.boardWidth = boardWidth;
        this.boardHeight = boardHeight;
        setPreferredSize(new Dimension(this.boardWidth, this.boardHeight));
        setBackground(Color.black);
        addKeyListener(this);
        setFocusable(true);

        snakeHead = new Tile(5, 5);
        snakeBody = new ArrayList<Tile>();

        target = new Tile(10, 10);
        random = new Random();
        placeTarget();

        velocityX = 0;
        velocityY = 0;
        gameOver = false;
        score = 0;

        gameLoop = new Timer(80, this);
        gameLoop.start();
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        draw(g);
    }

    public void draw(Graphics g) {
        // Grid
        for (int i = 0; i < boardWidth / tileSize; i++) {
            g.drawLine(i * tileSize, 0, i * tileSize, boardHeight);
            g.drawLine(0, i * tileSize, boardWidth, i * tileSize);
        }

        // Target
        g.setColor(Color.red);
        g.fillRect(target.x * tileSize, target.y * tileSize, tileSize, tileSize);

        // Snake
        g.setColor(Color.green);
        g.fillRect(snakeHead.x * tileSize, snakeHead.y * tileSize, tileSize, tileSize);
        for (int i = 0; i < snakeBody.size(); i++) {
            g.setColor(Color.green);
            g.fillRect(snakeBody.get(i).x * tileSize, snakeBody.get(i).y * tileSize, tileSize, tileSize);
        }

        // Gameover
        if (gameOver) {
            g.setColor(Color.white);
            g.setFont(new Font("Castellar", Font.BOLD, 48));
            FontMetrics metrics = getFontMetrics(g.getFont());
            String msg1 = "Game Over";
            String msg2 = "Score: " + score;
            g.drawString(msg1, (boardWidth - metrics.stringWidth(msg1)) / 2, boardHeight / 3);
            g.drawString(msg2, (boardWidth - metrics.stringWidth(msg2)) / 2, boardHeight / 2);
        }
    }

    public void placeTarget() {
        int x = random.nextInt(boardWidth / tileSize); // 600/25 = 24 columns
        int y = random.nextInt(boardHeight / tileSize); // 600/25 = 24 rows
        int i = 0;
        while (i < snakeBody.size()) {
            if ((x == snakeBody.get(i).x && y == snakeBody.get(i).y) ||
                    (x == snakeHead.x && y == snakeHead.y)) {
                x = random.nextInt(boardWidth / tileSize);
                y = random.nextInt(boardHeight / tileSize);
                i = 0;
            }
            i++;
        }
        target.x = x;
        target.y = y;
    }

    public void move() {

        Tile tempHead = new Tile(snakeHead.x, snakeHead.y);
        snakeHead.x += velocityX;
        snakeHead.y += velocityY;

        Tile tempNext = new Tile(snakeHead.x, snakeHead.y); // Next segment's position: current segment's starting
                                                            // position
        Tile tempCurrent = new Tile(snakeHead.x, snakeHead.y); // Current segment's position: previous segment's
                                                               // starting position
        for (int i = 0; i < snakeBody.size(); i++) {
            if (i == 0) {
                tempNext.x = snakeBody.get(i).x;
                tempNext.y = snakeBody.get(i).y;
                snakeBody.get(i).x = tempHead.x;
                snakeBody.get(i).y = tempHead.y;
            } else {
                tempCurrent.x = tempNext.x;
                tempCurrent.y = tempNext.y;
                tempNext.x = snakeBody.get(i).x;
                tempNext.y = snakeBody.get(i).y;
                snakeBody.get(i).x = tempCurrent.x;
                snakeBody.get(i).y = tempCurrent.y;
            }
        }
    }

    public void eat() {
        if (snakeHead.x == target.x && snakeHead.y == target.y) {
            Tile snakeSegment = new Tile(target.x, target.y);
            snakeBody.add(snakeSegment);
            placeTarget();
            score++;
        }
    }

    public boolean biteOwnTail() {
        for (int i = 0; i < snakeBody.size(); i++) {
            if (snakeHead.x == snakeBody.get(i).x && snakeHead.y == snakeBody.get(i).y) {
                return true;
            }
        }
        return false;
    }

    public boolean outOfBounds() {
        if (snakeHead.x < 0 || snakeHead.x > 23 || snakeHead.y < 0 || snakeHead.y > 23) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        move();
        if (biteOwnTail() || outOfBounds()) {
            gameOver = true;
            gameLoop.stop();
        }
        eat();
        repaint(); // Calls draw() repeatedly in accordance with the game loop timer
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if ((e.getKeyCode() == KeyEvent.VK_W || e.getKeyCode() == KeyEvent.VK_UP)
                && velocityY != 1) {
            velocityX = 0;
            velocityY = -1;
        } else if ((e.getKeyCode() == KeyEvent.VK_A || e.getKeyCode() == KeyEvent.VK_LEFT)
                && velocityX != 1) {
            velocityX = -1;
            velocityY = 0;
        } else if ((e.getKeyCode() == KeyEvent.VK_S || e.getKeyCode() == KeyEvent.VK_DOWN)
                && velocityY != -1) {
            velocityX = 0;
            velocityY = 1;
        } else if ((e.getKeyCode() == KeyEvent.VK_D || e.getKeyCode() == KeyEvent.VK_RIGHT)
                && velocityX != -1) {
            velocityX = 1;
            velocityY = 0;
        }
    }

    public static void main(String[] args) {
    }

    // --------------------------------------------------------------------------------------------------------------------------------------------------------------------
    // Do not need but exisits within KeyListener impementation (I think it's an
    // interface)
    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }
}
