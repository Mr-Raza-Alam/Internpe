import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.Graphics;
import java.awt.Color;

// Step 1: Create your drawing panel
class GamePanel extends JPanel {

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);  // always call this first!

        // Set background to black
        g.setColor(Color.BLUE);
        g.fillRect(0, 0, 800, 600); // fill entire panel with black

        // Draw a white rectangle (like a paddle)
        g.setColor(Color.green);
        g.fillRect(50, 250, 15, 80); // x, y, width, height

        // Draw a white circle (like a ball)
        g.fillOval(390, 290, 20, 20); // x, y, width, height

        // Draw some text
        g.drawString("Pong Game!", 360, 50); // text, x, y
    }
}

// Step 2: Put the panel inside JFrame
public class MyDrawing {
    public static void main(String[] args) {
        JFrame window = new JFrame();
        window.setTitle("Pong Game");
        window.setSize(800, 600);
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setLocationRelativeTo(null);

        GamePanel panel = new GamePanel(); // create panel
        window.add(panel);                 // add panel to window

        window.setVisible(true);
    }
}
