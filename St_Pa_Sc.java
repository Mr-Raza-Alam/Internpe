import java.util.*;

public class St_Pa_Sc {
    public static void main(String[] args) {
        // Start the game
        SPS();
    }

    public static void SPS() {
        Scanner scanner = new Scanner(System.in);
        
        System.out.println("======================================");
        System.out.println("  Welcome to Stone Paper Scissor!     ");
        System.out.println("======================================");
        
        // 1. Get Player Names
        System.out.print("Enter Player 1's name: ");
        String player1 = scanner.nextLine();
        
        System.out.print("Enter Player 2's name: ");
        String player2 = scanner.nextLine();
        
        // 2. Setup the Dictionary
        // Key: Element (STONE, PAPER, SCISSOR)
        // Value: ArrayList of player names who picked that element
        Map<String, List<String>> gameData = new HashMap<>();
        gameData.put("STONE", new ArrayList<>());
        gameData.put("PAPER", new ArrayList<>());
        gameData.put("SCISSOR", new ArrayList<>());
        
        // Array to make looping over players easier
        String[] players = {player1, player2};
        
        // 3. Get Inputs from both players
        for (String player : players) {
            boolean validChoice = false;
            
            // Loop until they enter a correct spelling
            while (!validChoice) {
                System.out.print(player + ", select your a card (Stone, Paper, or Scissor): ");
                String choice = scanner.nextLine().toUpperCase(); // Ignore case
                
                // If it's a valid key in our dictionary
                if (gameData.containsKey(choice)) {
                    // Add player to the ArrayList of their chosen element
                    gameData.get(choice).add(player);
                    validChoice = true;
                } else System.out.println("Invalid card selection. Please check your spelling and try again.");
                
            }
            
            // "Hide" the input from the next player so they don't peek
            System.out.println("\n");
            for (int i = 0; i < 5; i++) System.out.print(":");
            
        }
        
        // 4. Suspense Timer (5 Seconds)
        System.out.println("Wait a while ,Evaluating the winner...");
        try {
            Thread.sleep(3000); // 3000 milliseconds = 3 seconds
        } catch (InterruptedException e) {
            System.out.println("Timer interrupted!");
        }
        
        // 5. Determine Winner
        winningCriteria(gameData);
        
        scanner.close();
    }
    
    // Your winning criteria function that accepts the dictionary
    public static void winningCriteria(Map<String, List<String>> dictionary) {
        
        // Extract the ArrayLists for easy reading
        List<String> stonePlayers = dictionary.get("STONE");
        List<String> paperPlayers = dictionary.get("PAPER");
        List<String> scissorPlayers = dictionary.get("SCISSOR");
        
        // CONDITION 1: Tie Situation
        // If any ArrayList length is > 1, both players picked the exact same thing
        if (stonePlayers.size() > 1 || paperPlayers.size() > 1 || scissorPlayers.size() > 1) {
            System.out.println("\n>>> Result: It's a TIE! Both players chose the same element! <<<\n");
            return; // Exit the function early
        }
        
        // If we get past the tie check, we know exactly two lists have length == 1, 
        // and one list has length == 0.
        
        System.out.println("\n>>> And the winner is... <<<\n");
        
        // CONDITION 2: Stone vs Scissor
        if (stonePlayers.size() == 1 && scissorPlayers.size() == 1) {
            String winner = stonePlayers.get(0); // Extract player name from the winning list
            System.out.println("STONE blunts SCISSOR.");
            System.out.println("Congratulations " + winner + " ,you have won the game!\n");
        } 
        // CONDITION 3: Paper vs Stone
        else if (paperPlayers.size() == 1 && stonePlayers.size() == 1) {
            String winner = paperPlayers.get(0); // Extract player name from the winning list
            System.out.println("PAPER wraps STONE.");
            System.out.println("Congratulations " + winner + " ,you have won the game!\n");
        } 
        // CONDITION 4: Scissor vs Paper
        else if (scissorPlayers.size() == 1 && paperPlayers.size() == 1) {
            String winner = scissorPlayers.get(0); // Extract player name from the winning list
            System.out.println("SCISSOR cuts PAPER.");
            System.out.println("Congratulations " + winner + " ,you have won the game!\n");
        }
    }
}
