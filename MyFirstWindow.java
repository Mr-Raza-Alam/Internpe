import javax.swing.JFrame;

public class MyFirstWindow {
    public static void main(String[] args) {
        JFrame window = new JFrame();         // create window
        window.setTitle("My First Window");   // set title
        window.setSize(800, 600);             // width=800, height=600
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // close properly
        window.setLocationRelativeTo(null);   // open at center of screen
        window.setVisible(true);              // make it visible
    }
}