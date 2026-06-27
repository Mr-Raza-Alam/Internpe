import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.List;

public class TicTacToe {

    public static void main(String[] args) {
        // Ask for player names
        String name1 = JOptionPane.showInputDialog(null, "Enter name for Player 1:", "Player 1");
        String name2 = JOptionPane.showInputDialog(null, "Enter name for Player 2:", "Player 2");

        // Safety: if user cancels the dialog, use default names
        if (name1 == null || name1.trim().isEmpty()) name1 = "Player 1";
        if (name2 == null || name2.trim().isEmpty()) name2 = "Player 2";

        // Symbol Selection
        Object[] symOptions = {"X", "O"};
        int symChoice = JOptionPane.showOptionDialog(null, 
            name1 + ", choose your symbol:", 
            "Symbol Selection",
            JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, 
            null, symOptions, symOptions[0]);

        char sym1 = (symChoice == 1) ? 'O' : 'X';
        char sym2 = (sym1 == 'X') ? 'O' : 'X';

        // Turn Order Selection
        Object[] turnOptions = {name1, name2, "Random"};
        int turnChoice = JOptionPane.showOptionDialog(null, 
            "Who should make the first move?", 
            "Turn Order",
            JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, 
            null, turnOptions, turnOptions[0]);

        char startingSym;
        if (turnChoice == 0) {
            startingSym = sym1;
        } else if (turnChoice == 1) {
            startingSym = sym2;
        } else {
            // Random or closed dialog (default to random)
            startingSym = (new Random().nextBoolean()) ? sym1 : sym2;
        }

        // Launch the game window
        new GameWindow(name1, name2, sym1, sym2, startingSym);
    }
}

class GameWindow extends JFrame {

    GameWindow(String name1, String name2, char sym1, char sym2, char startingSym) {
        setTitle("Tic Tac Toe  —  " + name1 + " (" + sym1 + ")  vs  " + name2 + " (" + sym2 + ")");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);

        // GamePanel is where all drawing and logic lives
        GamePanel panel = new GamePanel(name1, name2, sym1, sym2, startingSym);
        add(panel);
        pack(); // auto-size window to fit panel
        setLocationRelativeTo(null); // center on screen
        setVisible(true);
    }
}
// ─────────────────────────────────────────────────────────────
//  GamePanel — the heart of the game
//  Handles: drawing, mouse clicks, game logic

class GamePanel extends JPanel implements MouseListener {

    // ── Window and cell size constants ──────────────────────
    static final int WINDOW_SIZE = 510;   // total panel size (px)
    static final int STATUS_BAR  = 60;    // top bar height for status text
    static final int BOARD_SIZE  = 450;   // 3x3 grid area (below status bar)
    static final int CELL_SIZE   = 150;   // each cell = 150 x 150 px

    // ── Player names and symbols ─────────────────────────────
    String name1;
    String name2;
    char sym1;
    char sym2;
    char startingSym;

    char[] board;                            // 9 cells: ' ', 'X', or 'O'
    Map<Character, List<Integer>> dict;      // X → [positions], O → [positions]
    int countX, countO;                      // move counts

    // ── Game state ──────────────────────────────────────────
    char    currSym;   // whose turn: 'X' or 'O'
    boolean gameOver;
    String  statusMessage;   // text shown in top bar
    int[]   winningCombo;    // stores the 3 winning indexes (for highlighting)

    // ── Colors (easy to change) ─────────────────────────────
    Color COLOR_BG          = new Color(28,  28,  40);   // dark background
    Color COLOR_GRID        = new Color(80,  80, 110);   // grid lines
    Color COLOR_X           = new Color(94, 158, 255);   // blue  for X
    Color COLOR_O           = new Color(255, 99, 99);    // red   for O
    Color COLOR_WIN_CELL    = new Color(50,  80,  50);   // green highlight for winner
    Color COLOR_STATUS_BG   = new Color(18,  18,  30);   // status bar background
    Color COLOR_TEXT        = new Color(220, 220, 235);  // general text


    // ── Constructor ─────────────────────────────────────────
    GamePanel(String name1, String name2, char sym1, char sym2, char startingSym) {
        this.name1 = name1;
        this.name2 = name2;
        this.sym1 = sym1;
        this.sym2 = sym2;
        this.startingSym = startingSym;

        setPreferredSize(new Dimension(WINDOW_SIZE, WINDOW_SIZE + STATUS_BAR));
        addMouseListener(this);   // listen for mouse clicks

        // ── Add a Restart button at the bottom ──
        setLayout(null);          // we'll position everything manually
        JButton restartBtn = new JButton("Restart");
        restartBtn.setBounds(185, WINDOW_SIZE + STATUS_BAR - 45, 140, 32);
        restartBtn.setBackground(new Color(60, 60, 90));
        restartBtn.setForeground(Color.WHITE);
        restartBtn.setFocusPainted(false);
        restartBtn.setBorderPainted(false);
        restartBtn.setFont(new Font("Arial", Font.BOLD, 13));
        restartBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        restartBtn.addActionListener(e -> resetGame());
        add(restartBtn);

        resetGame();   // initialize all data structures and start fresh
    }
    // ─────────────────────────────────────────────────────────
    //  resetGame() — initializes all data structures
    //  Called at start AND when Restart button is pressed
    void resetGame() {

        // 1. Board: 9 empty cells
        board = new char[9];
        Arrays.fill(board, ' ');

        // 2. Map: X and O each get an empty ArrayList
        dict = new HashMap<>();
        dict.put('X', new ArrayList<>());
        dict.put('O', new ArrayList<>());

        // 3. Counts reset to 0
        countX = 0;
        countO = 0;

        // 4. Set the starting player
        currSym = startingSym;

        // 5. Game is not over
        gameOver     = false;
        winningCombo = null;

        // 6. Status message at top
        String startName = (startingSym == sym1) ? name1 : name2;
        statusMessage = startName + "'s turn  (" + startingSym + ")";

        repaint();   // redraw the panel
    }
    // ─────────────────────────────────────────────────────────
    //  paintComponent() — draws EVERYTHING on screen
    //  Java calls this automatically whenever repaint() is called
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2 = (Graphics2D) g;
        // Makes text and shapes look smooth (anti-aliasing)
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        drawStatusBar(g2);    // step 1: top bar with player/status text
        drawBackground(g2);   // step 2: dark board background
        drawWinHighlight(g2); // step 3: green cells if game is won
        drawGrid(g2);         // step 4: the 3x3 grid lines
        drawSymbols(g2);      // step 5: X's and O's in the cells
    }


    // ── DRAW: status bar at the very top ────────────────────
    void drawStatusBar(Graphics2D g) {
        g.setColor(COLOR_STATUS_BG);
        g.fillRect(0, 0, WINDOW_SIZE, STATUS_BAR);

        g.setColor(COLOR_TEXT);
        g.setFont(new Font("Arial", Font.BOLD, 18));

        // Center the status text in the bar
        FontMetrics fm = g.getFontMetrics();
        int textX = (WINDOW_SIZE - fm.stringWidth(statusMessage)) / 2;
        int textY = STATUS_BAR / 2 + fm.getAscent() / 2 - 4;
        g.drawString(statusMessage, textX, textY);
    }


    // ── DRAW: dark background behind the board ───────────────
    void drawBackground(Graphics2D g) {
        g.setColor(COLOR_BG);
        // Board starts at y = STATUS_BAR (below the top bar)
        g.fillRect(0, STATUS_BAR, WINDOW_SIZE, BOARD_SIZE);
    }


    // ── DRAW: green highlight on winning cells ───────────────
    void drawWinHighlight(Graphics2D g) {
        if (winningCombo == null) return;   // no winner yet, skip

        g.setColor(COLOR_WIN_CELL);
        for (int index : winningCombo) {
            int col = index % 3;            // column 0,1,2
            int row = index / 3;            // row    0,1,2
            int x   = col * CELL_SIZE;
            int y   = row * CELL_SIZE + STATUS_BAR;
            g.fillRect(x + 1, y + 1, CELL_SIZE - 2, CELL_SIZE - 2);
        }
    }


    // ── DRAW: the 3x3 grid (2 vertical + 2 horizontal lines) ─
    void drawGrid(Graphics2D g) {
        g.setColor(COLOR_GRID);
        g.setStroke(new BasicStroke(3));   // line thickness = 3px

        // 2 vertical lines (at x=150 and x=300)
        for (int col = 1; col <= 2; col++) {
            int x = col * CELL_SIZE;
            g.drawLine(x, STATUS_BAR, x, STATUS_BAR + BOARD_SIZE);
        }

        // 2 horizontal lines (at y=150 and y=300, offset by STATUS_BAR)
        for (int row = 1; row <= 2; row++) {
            int y = row * CELL_SIZE + STATUS_BAR;
            g.drawLine(0, y, WINDOW_SIZE, y);
        }
    }


    // ── DRAW: X and O symbols inside their cells ─────────────
    void drawSymbols(Graphics2D g) {
        g.setStroke(new BasicStroke(8, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));

        for (int i = 0; i < 9; i++) {
            if (board[i] == ' ') continue;   // empty cell, skip

            int col = i % 3;
            int row = i / 3;

            // Top-left corner of this cell
            int cellX = col * CELL_SIZE;
            int cellY = row * CELL_SIZE + STATUS_BAR;

            // Padding so symbol doesn't touch the grid lines
            int pad = 30;

            if (board[i] == 'X') {
                drawX(g, cellX + pad, cellY + pad, CELL_SIZE - 2 * pad);
            } else if (board[i] == 'O') {
                drawO(g, cellX + pad, cellY + pad, CELL_SIZE - 2 * pad);
            }
        }
    }


    // ── DRAW: a single X (two diagonal lines) ────────────────
    void drawX(Graphics2D g, int x, int y, int size) {
        g.setColor(COLOR_X);
        g.drawLine(x,y,x + size, y + size);  // top-left to bottom-right
        g.drawLine(x + size, y,        x,        y + size);  // top-right to bottom-left
    }


    // ── DRAW: a single O (circle) ────────────────────────────
    void drawO(Graphics2D g, int x, int y, int size) {
        g.setColor(COLOR_O);
        g.drawOval(x, y, size, size);
    }
    // ─────────────────────────────────────────────────────────
    //  mouseClicked() — called every time user clicks the panel
    @Override
    public void mouseClicked(MouseEvent e) {
        
        if (gameOver) return; // Ignore clicks if game is already over

        // ── STEP 1: figure out which cell was clicked ───
        int mouseX = e.getX();
        int mouseY = e.getY();

        // Ignore clicks on the status bar (above the board)
        if (mouseY < STATUS_BAR) return;

        int col = mouseX / CELL_SIZE;               // 0, 1, or 2
        int row = (mouseY - STATUS_BAR) / CELL_SIZE; // 0, 1, or 2

        if (col > 2 || row > 2) return; // Safety: ignore clicks outside the 3x3 area

        int pos = row * 3 + col;   // convert (row,col) → single index 0–8

        // ── STEP 2: validate — is the cell empty? ────────────
        if (board[pos] != ' ') return;   // cell taken, do nothing

        // ── STEP 3: place the mark ───────────────────────────
        board[pos] = currSym;

        // ── STEP 4: update Map and count────────
        dict.get(currSym).add(pos);

        if (currSym == 'X') countX++;  else countO++;

        // ── STEP 5: check for winner ─────────────────────────
        int currCount = (currSym == 'X') ? countX : countO;
        String currName = (currSym == sym1) ? name1 : name2;

        if (currCount >= 3) {
            winningCombo = winningCriteria(dict, currSym);

            if (winningCombo != null) {
                statusMessage = "🏆  " + currName + " wins!";
                gameOver = true;
                repaint();
                return;
            }
        }

        // ── STEP 6: check for draw ────────────────────────────
        if (countX + countO == 9) {
            statusMessage = "It's a Draw!  Well played both!";
            gameOver = true;
            repaint();
            return;
        }

        // ── STEP 7: switch player ─────────────────────────────
        currSym = (currSym == 'X') ? 'O' : 'X';
        String nextName = (currSym == sym1) ? name1 : name2;
        statusMessage = nextName + "'s turn  (" + currSym + ")";

        repaint();   // redraw everything with updated state
    }


    int[] winningCriteria(Map<Character, List<Integer>> dict, char sym) {

        // All 8 possible winning combinations
        int[][] box = {
            {0, 1, 2},   // row 1
            {3, 4, 5},   // row 2
            {6, 7, 8},   // row 3
            {0, 3, 6},   // col 1
            {1, 4, 7},   // col 2
            {2, 5, 8},   // col 3
            {0, 4, 8},   // diagonal1
            {2, 4, 6}    // diagonal2
        };

        // Get this player's claimed positions from the Map
        List<Integer> positions = dict.get(sym);

        // Check each combo — if player has all 3 positions → winner
        for (int[] combo : box) {
            if (positions.contains(combo[0]) &&
                positions.contains(combo[1]) &&
                positions.contains(combo[2])) {
                return combo;   // return the winning combo for highlighting
            }
        }
        return null;   // no winning combo found yet
    }
    // ── Required by MouseListener (we only need mouseClicked) ─
    @Override public void mousePressed(MouseEvent e)  {}
    @Override public void mouseReleased(MouseEvent e) {}
    @Override public void mouseEntered(MouseEvent e)  {}
    @Override public void mouseExited(MouseEvent e)   {}
}
