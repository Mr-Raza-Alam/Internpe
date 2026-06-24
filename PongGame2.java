import javax.swing.JFrame; // 
import javax.swing.JPanel;//
import javax.swing.Timer;//
import java.awt.Graphics; // 
import java.awt.Graphics2D;
import java.awt.Color;
import java.awt.Font;
import java.awt.RenderingHints;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.KeyListener;
import java.awt.event.KeyEvent;
import java.util.Scanner;

// ---- GAME PANEL ----
class GamePanel extends JPanel implements ActionListener, KeyListener {

    // ---- SCREEN ----
    static final int WIDTH  = 800;
    static final int HEIGHT = 600;

    // -------- PADDLE --------
    static final int PADDLE_WIDTH  = 15;
    static final int PADDLE_HEIGHT = 80;
    static final int PADDLE_SPEED  = 6;

    // Player 1 (LEFT paddle)
    int p1X = 30;
    int p1Y = HEIGHT / 2 - PADDLE_HEIGHT / 2;

    // Player 2 (RIGHT paddle)
    int p2X = WIDTH - 30 - PADDLE_WIDTH;
    int p2Y = HEIGHT / 2 - PADDLE_HEIGHT / 2;

    // -------- MOVEMENT FLAGS --------
    boolean p1Up, p1Down, p2Up, p2Down;

    // -------- BALL --------
    static final int BALL_SIZE = 20;
    int ballX = WIDTH  / 2 - BALL_SIZE / 2;
    int ballY = HEIGHT / 2 - BALL_SIZE / 2;

    // FIX 1: use double for velocity (more precise speed control)
    double ballVX = 5.0;
    double ballVY = 4.0;

    // FIX 1: speed increases after every hit
    static final double SPEED_INCREMENT = 0.4; // increases by 0.4 every hit
    static final double MAX_SPEED       = 18.0; // maximum speed limit

    // -------- SCORE --------
    int score1 = 0;
    int score2 = 0;
    static final int TARGET_SCORE = 5;

    // FIX 3: Player names from terminal
    String p1Name;
    String p2Name;

    // -------- GAME STATE --------
    boolean gameOver = false;
    String winner = "";

    Timer timer;

    GamePanel(String p1N, String p2N) { // constructor
        this.p1Name = p1N;
        this.p2Name = p2N;

        setBackground(Color.BLACK);
        timer = new Timer(16, this);
        timer.start();

        addKeyListener(this);
        setFocusable(true);
        requestFocusInWindow();
    }

    // --- Game Loop  logic ---

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

// ---- MOVE PADDLES ----
    void movePaddles() {
        // Player 1
        if (p1Up) {
            p1Y -= PADDLE_SPEED;
            if (p1Y < 0) p1Y = 0; // hard stop at top edge
        }
        if (p1Down) {
            p1Y += PADDLE_SPEED;
            if (p1Y > HEIGHT - PADDLE_HEIGHT) p1Y = HEIGHT - PADDLE_HEIGHT; // hard stop at bottom
        }

        // Player 2
        if (p2Up) {
            p2Y -= PADDLE_SPEED;
            if (p2Y < 0) p2Y = 0;
        }
        if (p2Down) {
            p2Y += PADDLE_SPEED;
            if (p2Y > HEIGHT - PADDLE_HEIGHT) p2Y = HEIGHT - PADDLE_HEIGHT;
        }
    }

    // --  MOVE BALL --
    void moveBall() {
        ballX += (int) ballVX;
        ballY += (int) ballVY;
    }

    // -- CHECK COLLISIONS --
    void checkCollisions() {

        // ball hits TOP wall
        if (ballY <= 0) {
            ballY = 0;
            ballVY = Math.abs(ballVY); // force downward direction
        }

        // ball hits BOTTOM wall
        if (ballY >= HEIGHT - BALL_SIZE) {
            ballY = HEIGHT - BALL_SIZE;
            ballVY = -Math.abs(ballVY); // force upward direction
        }

        // ball hits Player 1 paddle (LEFT)
        if (ballX <= p1X + PADDLE_WIDTH &&
            ballX >= p1X &&
            ballY + BALL_SIZE >= p1Y &&
            ballY <= p1Y + PADDLE_HEIGHT) {

            // push ball outside paddle
            ballX = p1X + PADDLE_WIDTH;

            double hitPosition = (ballY + BALL_SIZE / 2.0 - p1Y) / PADDLE_HEIGHT;


            double normalizedHit = hitPosition - 0.5; // range: -0.5 to +0.5

            // FIX 1: increase speed after every hit
            double currentSpeed = Math.sqrt(ballVX * ballVX + ballVY * ballVY);
            double newSpeed = Math.min(currentSpeed + SPEED_INCREMENT, MAX_SPEED);

            // apply new velocity with angle
            ballVX =  Math.abs(newSpeed * Math.cos(normalizedHit));  // always go RIGHT
            ballVY =  newSpeed * Math.sin(normalizedHit) * 2;        // angle effect
        }

        // ball hits Player 2 paddle (RIGHT)
        if (ballX + BALL_SIZE >= p2X &&
            ballX <= p2X + PADDLE_WIDTH &&
            ballY + BALL_SIZE >= p2Y &&
            ballY <= p2Y + PADDLE_HEIGHT) {

            // push ball outside paddle
            ballX = p2X - BALL_SIZE;

            double hitPosition   = (ballY + BALL_SIZE / 2.0 - p2Y) / PADDLE_HEIGHT;
            double normalizedHit = hitPosition - 0.5;

            double currentSpeed = Math.sqrt(ballVX * ballVX + ballVY * ballVY);
            double newSpeed = Math.min(currentSpeed + SPEED_INCREMENT, MAX_SPEED);

            // apply new velocity — ball goes LEFT this time
            ballVX = -Math.abs(newSpeed * Math.cos(normalizedHit)); // always go LEFT
            ballVY =  newSpeed * Math.sin(normalizedHit) * 2;       // angle effect
        }
    }

    void checkScore() {
        if (ballX <= 0) {
            score2++;
            resetBall();
        }
        if (ballX >= WIDTH) {
            score1++;
            resetBall();
        }
        if (score1 >= TARGET_SCORE) {
            gameOver = true;
            winner = p1Name;  // use actual name!
            timer.stop();
        }
        if (score2 >= TARGET_SCORE) {
            gameOver = true;
            winner = p2Name;  // use actual name!
            timer.stop();
        }
    }

    // ---- RESET BALL ----
    void resetBall() {
        ballX = WIDTH  / 2 - BALL_SIZE / 2;
        ballY = HEIGHT / 2 - BALL_SIZE / 2;

        // FIX 1: reset speed to initial after each point
        ballVX = -Math.signum(ballVX) * 5.0; // keep direction, reset speed
        ballVY = 4.0;
    }

    // ---- RESTART GAME ----
    void restartGame() {
        score1   = 0;
        score2   = 0;
        gameOver = false;
        winner   = "";
        p1Y      = HEIGHT / 2 - PADDLE_HEIGHT / 2;
        p2Y      = HEIGHT / 2 - PADDLE_HEIGHT / 2;
        ballX    = WIDTH  / 2 - BALL_SIZE / 2;
        ballY    = HEIGHT / 2 - BALL_SIZE / 2;
        ballVX   = 5.0;
        ballVY   = 4.0;
        timer.start();
    }

    // --- DRAW EVERYTHING ----
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

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

        // Player 1 paddle
        g.setColor(Color.CYAN);   // FIX: different colors for each player!
        g.fillRect(p1X, p1Y, PADDLE_WIDTH, PADDLE_HEIGHT);

        // Player 2 paddle
        g.setColor(Color.ORANGE);
        g.fillRect(p2X, p2Y, PADDLE_WIDTH, PADDLE_HEIGHT);

        // ball
        g.setColor(Color.WHITE);
        g.fillOval(ballX, ballY, BALL_SIZE, BALL_SIZE);

        // scores
        g.setFont(new Font("Arial", Font.BOLD, 50));
        g.setColor(Color.CYAN);
        g.drawString(String.valueOf(score1), WIDTH / 2 - 80, 60);
        g.setColor(Color.ORANGE);
        g.drawString(String.valueOf(score2), WIDTH / 2 + 40, 60);

        // FIX 3: show actual player names
        g.setFont(new Font("Arial", Font.BOLD, 16));
        g.setColor(Color.CYAN);
        g.drawString(p1Name, 20, 20);
        g.setColor(Color.ORANGE);
        g.drawString(p2Name, WIDTH - 20 - p2Name.length() * 10, 20);

        // controls hint
        g.setFont(new Font("Arial", Font.PLAIN, 12));
        g.setColor(Color.DARK_GRAY);
        g.drawString("W/S", 30, HEIGHT - 10);
        g.drawString("↑/↓", WIDTH - 50, HEIGHT - 10);

        // current ball speed indicator
        double currentSpeed = Math.sqrt(ballVX * ballVX + ballVY * ballVY);
        g.setColor(Color.GRAY);
        g.setFont(new Font("Arial", Font.PLAIN, 12));
        g.drawString("Speed: " + String.format("%.1f", currentSpeed),
                      WIDTH / 2 - 30, HEIGHT - 10);

        if (gameOver) drawGameOver(g);
    }
    // ---- GAME OVER SCREEN ----
    void drawGameOver(Graphics g) {
        g.setColor(new Color(0, 0, 0, 180));
        g.fillRect(0, 0, WIDTH, HEIGHT);

        g.setColor(Color.YELLOW);
        g.setFont(new Font("Arial", Font.BOLD, 50));
        g.drawString(winner + " Wins!", WIDTH / 2 - 160, HEIGHT / 2 - 40);

        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 30));
        g.drawString(p1Name + "  " + score1 + "  :  " + score2 + "  " + p2Name,
                      WIDTH / 2 - 180, HEIGHT / 2 + 20);

        g.setFont(new Font("Arial", Font.PLAIN, 20));
        g.setColor(Color.LIGHT_GRAY);
        g.drawString("Press R to Restart  |  Press ESC to Quit",
                      WIDTH / 2 - 190, HEIGHT / 2 + 70);
    }

    // ---- KEY PRESSED ----
    @Override
    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();

        if (key == KeyEvent.VK_W)      p1Up    = true;
        if (key == KeyEvent.VK_S)      p1Down  = true;
        if (key == KeyEvent.VK_UP)     p2Up    = true;
        if (key == KeyEvent.VK_DOWN)   p2Down  = true;

        if (key == KeyEvent.VK_R && gameOver) restartGame();
        if (key == KeyEvent.VK_ESCAPE)        System.exit(0);
    }

    // ---- KEY RELEASED ----
    @Override
    public void keyReleased(KeyEvent e) {
        int key = e.getKeyCode();

        if (key == KeyEvent.VK_W)    p1Up    = false;
        if (key == KeyEvent.VK_S)    p1Down  = false;
        if (key == KeyEvent.VK_UP)   p2Up    = false;
        if (key == KeyEvent.VK_DOWN) p2Down  = false;
    }

    @Override
    public void keyTyped(KeyEvent e) { }
}

// -- MAIN CLASS --
public class PongGame2  {
    public static void main(String[] args) {

        Scanner read = new Scanner(System.in);
        System.out.println("\n========================================");
        System.out.println("\t--Welcome to Pong Game!--");
        System.out.println("========================================\n");
        System.out.print("Enter Player 1 name (LEFT  | Controls: W/S) : ");
        String p1N = read.nextLine().trim();
        System.out.print("Enter Player 2 name (RIGHT | Controls: \\u2191/\\u2193) : ");
        String p2N = read.nextLine().trim();
        read.close();

        // create window
        JFrame window = new JFrame();
        window.setTitle("Pong Game 🏓  |  " + p1N + "  vs  " + p2N);
        window.setSize(800, 600);
        window.setResizable(false);
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setLocationRelativeTo(null);

        GamePanel panel = new GamePanel(p1N, p2N);
        window.add(panel);
        window.setVisible(true); 
    }
}