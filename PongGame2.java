import javax.swing.JFrame;// Creates the outer game window that appears on screen

import javax.swing.JPanel; // Creates the drawing canvas placed inside JFrame where all game elements are drawn

import javax.swing.Timer; // Creates the game loop that automatically fires every 16ms.It is triggering actionPerformed() to update and redraw the game

import java.awt.Graphics;// A drawing tool (paintbrush) passed by Java as parameter into paintComponent(). It is Used to draw shapes, text and colors on the canvas

import java.awt.Graphics2D;// Upgraded child class of Graphics with smoother and more precise drawing capabilities. It Enables antialiasing and advanced rendering features

import java.awt.Color;// Used to set colors for background, paddles, ball, text and any game element

import java.awt.Font;// Used to set custom font style, weight and size for text drawn on screen

import java.awt.RenderingHints;// Used to enable antialiasing — makes edges of drawn shapes smooth instead of pixelated

import java.awt.event.ActionListener;// Interface that GamePanel implements to receive and respond to Timer event.It Requires overriding actionPerformed() method
import java.awt.event.ActionEvent;// Object automatically created by Timer and passed into actionPerformed().It Carries information about the Timer event that fired — not the executor itself

import java.awt.event.KeyListener;// Interface that GamePanel implements to receive and respond to keyboard events. It Requires overriding keyPressed(), keyReleased() and keyTyped() methods

import java.awt.event.KeyEvent;
// It is an  Object that automatically created by Java and passed into keyPressed() / keyReleased()
// It Carries complete information about keyboard event — which key, its keycode,
// whether a key is pressed or released — extracted using e.getKeyCode()
// ===================== GAME PANEL =====================
class GamePanel extends JPanel implements ActionListener, KeyListener {

    // -------- SCREEN --------
    static final int WIDTH  = 800;
    static final int HEIGHT = 600;

    // -------- PADDLE --------
    static final int PADDLE_WIDTH  = 15;
    static final int PADDLE_HEIGHT = 80;
    static final int PADDLE_SPEED  = 5;

    // Player 1 (LEFT paddle)
    int p1X = 30;
    int p1Y = HEIGHT / 2 - PADDLE_HEIGHT / 2;

    // Player 2 (RIGHT paddle)
    int p2X = WIDTH - 30 - PADDLE_WIDTH;
    int p2Y = HEIGHT / 2 - PADDLE_HEIGHT / 2;

    // -------- PADDLE MOVEMENT FLAGS --------
    boolean p1Up, p1Down, p2Up, p2Down;

    // -------- BALL --------
    static final int BALL_SIZE = 20;
    int ballX = WIDTH  / 2 - BALL_SIZE / 2;
    int ballY = HEIGHT / 2 - BALL_SIZE / 2;
    int ballVX = 6;  // velocity X
    int ballVY = 5;  // velocity Y

    // -------- SCORE --------
    int score1 = 0;
    int score2 = 0;
    static final int TARGET_SCORE = 3; // first to 5 wins!

    // -------- GAME STATE --------
    boolean gameOver = false;
    String winner = "";

    // -------- TIMER --------
    Timer timer;

    // ===================== CONSTRUCTOR =====================
    GamePanel() {
        setBackground(Color.GRAY);
        timer = new Timer(16, this);
        timer.start();

        addKeyListener(this);
        setFocusable(true);
        requestFocusInWindow();
    }

    // ===================== GAME LOOP =====================
    @Override
    public void actionPerformed(ActionEvent e) {
        if (!gameOver) {
            movePaddles();
            moveBall();
            checkCollisions();
            checkScore();
        }
        repaint();
    }

    // ===================== MOVE PADDLES =====================
    void movePaddles() {
        // Player 1 movement
        if (p1Up   && p1Y > 0)  p1Y -= PADDLE_SPEED;
        if (p1Down && p1Y < HEIGHT - PADDLE_HEIGHT) p1Y += PADDLE_SPEED;

        // Player 2 movement
        if (p2Up   && p2Y > 0)              p2Y -= PADDLE_SPEED;
        if (p2Down && p2Y < HEIGHT - PADDLE_HEIGHT) p2Y += PADDLE_SPEED;
    }

    // ===================== MOVE BALL =====================
    void moveBall() {
        ballX += ballVX;
        ballY += ballVY;
    }

    // ===================== CHECK COLLISIONS =====================
    void checkCollisions() {

        // ball hits top wall
        if (ballY <= 0) {
            ballY = 0;
            ballVY = -ballVY;
        }

        // ball hits bottom wall
        if (ballY >= HEIGHT - BALL_SIZE) {
            ballY = HEIGHT - BALL_SIZE;
            ballVY = -ballVY;
        }

        // ball hits Player 1 paddle (left)
        if (ballX <= p1X + PADDLE_WIDTH &&
            ballX >= p1X &&
            ballY + BALL_SIZE >= p1Y &&
            ballY <= p1Y + PADDLE_HEIGHT) {
            ballX = p1X + PADDLE_WIDTH; // prevent sticking
            ballVX = -ballVX;
        }

        // ball hits Player 2 paddle (right)
        if (ballX + BALL_SIZE >= p2X &&
            ballX <= p2X + PADDLE_WIDTH &&
            ballY + BALL_SIZE >= p2Y &&
            ballY <= p2Y + PADDLE_HEIGHT) {
            ballX = p2X - BALL_SIZE; // prevent sticking
            ballVX = -ballVX;
        }
    }

    // ===================== CHECK SCORE =====================
    void checkScore() {

        // ball crosses LEFT edge → Player 2 scores
        if (ballX <= 0) {
            score2++;
            resetBall();
        }

        // ball crosses RIGHT edge → Player 1 scores
        if (ballX >= WIDTH) {
            score1++;
            resetBall();
        }

        // check winner
        if (score1 >= TARGET_SCORE) {
            gameOver = true;
            winner = "Player 1";
            timer.stop();
        }
        if (score2 >= TARGET_SCORE) {
            gameOver = true;
            winner = "Player 2";
            timer.stop();
        }
    }

    // ===================== RESET BALL =====================
    void resetBall() {
        ballX = WIDTH  / 2 - BALL_SIZE / 2;
        ballY = HEIGHT / 2 - BALL_SIZE / 2;
        ballVX = -ballVX; // alternate direction after each point
    }

    // ===================== RESTART GAME =====================
    void restartGame() {
        score1 = 0;
        score2 = 0;
        gameOver = false;
        winner = "";
        p1Y = HEIGHT / 2 - PADDLE_HEIGHT / 2;
        p2Y = HEIGHT / 2 - PADDLE_HEIGHT / 2;
        ballX = WIDTH  / 2 - BALL_SIZE / 2;
        ballY = HEIGHT / 2 - BALL_SIZE / 2;
        ballVX = 4;
        ballVY = 3;
        timer.start();
    }

    // ===================== DRAW EVERYTHING =====================
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // smooth rendering
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                             RenderingHints.VALUE_ANTIALIAS_ON);

        // black background
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, WIDTH, HEIGHT);

        // center dashed line
        g.setColor(Color.GRAY);
        for (int i = 0; i < HEIGHT; i += 30) {
            g.fillRect(WIDTH / 2 - 2, i, 4, 15);
        }

        // draw Player 1 paddle (LEFT)
        g.setColor(Color.WHITE);
        g.fillRect(p1X, p1Y, PADDLE_WIDTH, PADDLE_HEIGHT);

        // draw Player 2 paddle (RIGHT)
        g.fillRect(p2X, p2Y, PADDLE_WIDTH, PADDLE_HEIGHT);

        // draw ball
        g.setColor(Color.WHITE);
        g.fillOval(ballX, ballY, BALL_SIZE, BALL_SIZE);

        // draw scores
        g.setFont(new Font("Arial", Font.BOLD, 50));
        g.setColor(Color.WHITE);
        g.drawString(String.valueOf(score1), WIDTH / 2 - 80, 60);
        g.drawString(String.valueOf(score2), WIDTH / 2 + 40, 60);

        // draw player labels
        g.setFont(new Font("Arial", Font.PLAIN, 14));
        g.setColor(Color.GRAY);
        g.drawString("Player 1", 20, 20);
        g.drawString("Player 2", WIDTH - 85, 20);

        // draw controls hint
        g.setFont(new Font("Arial", Font.PLAIN, 12));
        g.setColor(Color.DARK_GRAY);
        g.drawString("W/S", 30, HEIGHT - 10);
        g.drawString("↑/↓", WIDTH - 50, HEIGHT - 10);

        // draw game over screen
        if (gameOver) {
            drawGameOver(g);
        }
    }

    // ===================== GAME OVER SCREEN =====================
    void drawGameOver(Graphics g) {

        // semi transparent dark overlay
        g.setColor(new Color(0, 0, 0, 180));
        g.fillRect(0, 0, WIDTH, HEIGHT);

        // winner text
        g.setColor(Color.YELLOW);
        g.setFont(new Font("Arial", Font.BOLD, 50));
        g.drawString(winner + " Wins!", WIDTH / 2 - 150, HEIGHT / 2 - 40);

        // final score
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 30));
        g.drawString(score1 + "  :  " + score2, WIDTH / 2 - 60, HEIGHT / 2 + 20);

        // restart instruction
        g.setFont(new Font("Arial", Font.PLAIN, 20));
        g.setColor(Color.LIGHT_GRAY);
        g.drawString("Press R to Restart  |  Press ESC to Quit",
                      WIDTH / 2 - 190, HEIGHT / 2 + 70);
    }

    // ===================== KEY PRESSED =====================
    @Override
    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();

        // Player 1 controls
        if (key == KeyEvent.VK_W) p1Up   = true;
        if (key == KeyEvent.VK_S) p1Down = true;

        // Player 2 controls
        if (key == KeyEvent.VK_UP)   p2Up   = true;
        if (key == KeyEvent.VK_DOWN) p2Down = true;

        // restart
        if (key == KeyEvent.VK_R && gameOver) restartGame();

        // quit
        if (key == KeyEvent.VK_ESCAPE) System.exit(0);
    }

    // ===================== KEY RELEASED =====================
    @Override
    public void keyReleased(KeyEvent e) {
        int key = e.getKeyCode();

        if (key == KeyEvent.VK_W) p1Up   = false;
        if (key == KeyEvent.VK_S) p1Down = false;

        if (key == KeyEvent.VK_UP)   p2Up   = false;
        if (key == KeyEvent.VK_DOWN) p2Down = false;
    }

    @Override
    public void keyTyped(KeyEvent e) { }
}

// ===================== MAIN CLASS =====================
public class PongGame2 {
    public static void main(String[] args) {
        JFrame window = new JFrame();
        window.setTitle("Pong Game 🏓");
        window.setSize(800, 600);
        window.setResizable(false);
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setLocationRelativeTo(null);

        GamePanel panel = new GamePanel();
        window.add(panel);

        window.setVisible(true);
    }
}
