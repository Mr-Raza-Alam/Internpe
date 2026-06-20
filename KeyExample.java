import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.Graphics;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.KeyListener;
import java.awt.event.KeyEvent;

class GamePanel extends JPanel implements KeyListener {

    // paddle Y position (starts at center)
    int paddleY = 250;
    String message = "Press W to go UP, S to go DOWN";

    // constructor
    GamePanel() {
        addKeyListener(this);    // register this panel as key listener
        setFocusable(true);      // allow panel to receive key events
        requestFocusInWindow();  // grab focus immediately
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // black background
        g.setColor(Color.GRAY);
        g.fillRect(0, 0, 800, 600);

        // draw paddle at current Y position
        g.setColor(Color.black);
        g.fillRect(50, paddleY, 15, 80);  // paddle on left side

        // draw message
        g.setFont(new Font("Arial", Font.BOLD, 18));
        g.drawString(message, 200, 50);
    }

    // called when key is pressed DOWN
    @Override
    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();  // get which key was pressed

        if (key == KeyEvent.VK_W) {
            paddleY -= 15;         // move paddle UP (Y decreases!)
            message = "Moving UP!";
        }
        if (key == KeyEvent.VK_S) {
            paddleY += 15;         // move paddle DOWN (Y increases!)
            message = "Moving DOWN!";
        }
        repaint(); // redraw screen after movement
    }

    // called when key is RELEASED
    @Override
    public void keyReleased(KeyEvent e) {
        message = "Press W to go UP, S to go DOWN";
        repaint();
    }

    // called when key is TYPED (not needed for games)
    @Override
    public void keyTyped(KeyEvent e) {
        // leave empty for now
    }
}

public class KeyExample {
    public static void main(String[] args) {
        JFrame window = new JFrame();
        window.setTitle("KeyListener Example");
        window.setSize(800, 600);
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setLocationRelativeTo(null);

        GamePanel panel = new GamePanel();
        window.add(panel);

        window.setVisible(true);
    }
}
