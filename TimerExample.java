import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Timer;
import java.awt.Graphics;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.KeyListener;
import java.awt.event.KeyEvent;

class GamePanel extends JPanel implements ActionListener, KeyListener {

    // ball position
    int ballX = 390;
    int ballY = 290;

    // ball velocity (how much it moves per frame)
    int ballVelocityX = 3;
    int ballVelocityY = 3;

    // paddle position
    int paddleY = 250;

    // timer
    Timer timer;

    // constructor
    GamePanel() {
        timer = new Timer(16, this); // fire every 16ms, notify "this"
        timer.start();               // start the game loop!

        addKeyListener(this);
        setFocusable(true);
        requestFocusInWindow();
    }

    // THIS RUNS EVERY 16ms AUTOMATICALLY!
    @Override
    public void actionPerformed(ActionEvent e) {

        // 1. move the ball
        ballX += ballVelocityX;
        ballY += ballVelocityY;

        // 2. bounce off top and bottom walls
        if (ballY <= 0 || ballY >= 580) {
            ballVelocityY = -ballVelocityY; // reverse Y direction
        }

        // 3. bounce off right wall
        if (ballX >= 780) {
            ballVelocityX = -ballVelocityX; // reverse X direction
        }

        // 4. bounce off paddle (left side)
        if (ballX <= 65 && ballY >= paddleY && ballY <= paddleY + 80) {
            ballVelocityX = -ballVelocityX; // reverse X direction
        }

        // 5. ball goes past left wall = missed!
        if (ballX <= 0) {
            ballX = 390; // reset ball to center
            ballY = 290;
        }

        // 6. redraw screen
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // black background
        g.setColor(Color.green);
        g.fillRect(0, 0, 800, 600);

        // draw paddle
        g.setColor(Color.gray);
        g.fillRect(50, paddleY, 15, 80);

        // draw ball
        g.setColor(Color.blue);
        g.fillOval(ballX, ballY, 20, 20);

        // draw center dashed line
        g.setColor(Color.black);
        for (int i = 0; i < 600; i += 30) {
            g.fillRect(395, i, 5, 15); // small rectangles as dashes
        }

        // draw instructions
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 14));
        g.drawString("W = Up   S = Down", 20, 30);
    }

    // keyboard controls for paddle
    @Override
    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();

        if (key == KeyEvent.VK_W) {
            if (paddleY > 0) paddleY -= 15;        // move up
        }
        if (key == KeyEvent.VK_S) {
            if (paddleY < 520) paddleY += 15;      // move down
        }
    }

    @Override
    public void keyReleased(KeyEvent e) { }  // empty

    @Override
    public void keyTyped(KeyEvent e) { }     // empty
}

public class TimerExample {
    public static void main(String[] args) {
        JFrame window = new JFrame();
        window.setTitle("Moving Ball Example");
        window.setSize(800, 600);
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setLocationRelativeTo(null);

        GamePanel panel = new GamePanel();
        window.add(panel);

        window.setVisible(true);
    }
}
