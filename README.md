# 🚀 Java Mini Projects Collection (InternPe)

Welcome to the **Java Mini Projects Collection**! This repository serves as a progressive portfolio of tasks and mini-projects developed during my **6-week Java Programming Internship at InternPe** (started June 8th). 

It contains a variety of beginner-to-intermediate level Java applications and games, and will be updated continuously as the internship progresses. It serves as a great learning resource for mastering **Core Java** and **GUI Development** using Java Swing and AWT.

---

## 📂 Projects Included

### 1. ❌⭕ Tic Tac Toe Game (`TicTacToe.java`)
A 2-player graphical Tic-Tac-Toe game built with Java Swing.
- **Features:**
  - Player vs Player mode with customizable names.
  - Interactive 3x3 grid with mouse click detection.
  - Automatic win/draw detection with green highlighted winning combinations.
  - "Restart" button for continuous gameplay.

### 2. 🏓 Pong Game (`PongGame.java` & `PongGame2.java`)
A fully functional 2-player GUI Pong game built with Java Swing and AWT.
- **Features:** 
  - Player vs Player mode with custom names.
  - Smooth paddle movement and ball physics (increasing speed, varying angles).
  - Score tracking and a dedicated "Game Over" screen.
- **Controls:** 
  - **Player 1 (Left):** `W` (Up), `S` (Down)
  - **Player 2 (Right):** `↑` (Up), `↓` (Down)
  - **Restart:** `R` | **Quit:** `ESC`

### 3. 🔢 Number Guessing Game (`NumGuess.java`)
A fun, console-based game where the computer generates a random number between 1 and 100, and you have to guess it!
- **Features:**
  - Provides hints ("near to the target" or "far away from the target") to help you guess.
  - Tracks the number of attempts taken to win.
  - Option to replay the game continuously.

### 4. ✂️ Stone Paper Scissor (`SP_SC.java` & `St_Pa_Sc.java`)
A classic 2-player console game of Rock-Paper-Scissors (Stone-Paper-Scissor).
- **Features:**
  - Takes inputs from two players (hidden via spacing).
  - Suspenseful 3-second evaluation timer.
  - Complete winning logic and tie-breakers.

### 5. 🛠️ Java Swing & GUI Learning Examples
If you are new to Java GUI development, these smaller examples break down the concepts used to build the full Pong Game:
- **`MyFirstWindow.java`**: Learn how to create a basic blank window (`JFrame`).
- **`MyDrawing.java`**: Learn how to draw shapes (rectangles, ovals) and text using `Graphics` and `JPanel`.
- **`KeyExample.java`**: Learn how to implement `KeyListener` to move objects (like a paddle) using keyboard inputs.
- **`TimerExample.java`**: Learn how to create a Game Loop using `javax.swing.Timer` to animate a continuously moving and bouncing ball.

---

## 🛠️ Technologies Used
- **Language:** Java (JDK 8 or higher recommended)
- **Libraries:** Standard Java libraries (`java.util.*`), Java Swing (`javax.swing.*`), Java AWT (`java.awt.*`)

---

## 🚀 How to Run the Projects

1. **Clone the repository:**
   ```bash
   git clone <repository-url>
   cd <repository-directory>
   ```

2. **Compile the Java files:**
   Open your terminal/command prompt and compile any file using the `javac` command.
   ```bash
   javac PongGame.java
   ```

3. **Run the compiled class:**
   Run the program using the `java` command.
   ```bash
   java PongGame
   ```
*(Replace `PongGame` with the name of any other file you wish to run).*

---

## 🤝 Contributing
Feel free to fork this repository and add your own Java mini-projects or improve the existing ones. Pull requests are always welcome!

## 📜 License
This project is open-source and available for educational purposes. Feel free to use the code to learn and build your own awesome Java applications! Happy Coding! 💻✨
