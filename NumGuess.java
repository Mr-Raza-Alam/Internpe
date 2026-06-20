import java.util.Scanner;
import java.util.Random;
public class NumGuess { 
    
    static void guessNum(){
       Scanner read = new Scanner(System.in);
       String play = "yes";// this will tell us ,playing status of the player
        System.out.println("\t\t ----Game is Live----\n");

        while(play.equals("yes")){

            int guess = -1; // -1 means ,play donot start play the game yet.
            int attempt = 0; // this will count,no.of attempts does a player will take to guess a number.
            Random rand = new Random();
            int randNum = rand.nextInt(100); // it will generate an integer random no. b/w 1 to 100.

            while(guess != randNum){
                System.out.print("Guess a Number b/w 1 to 100. : ");
                guess = read.nextInt();
                attempt++;

                if(guess == randNum){ // this is the winning condition
                    System.out.println("\nAwesome! You have won the game.!!\n");
                    System.out.println("\t-> Hey ,you just tooks "+attempt+" attempts to guess the number.\n");
                    System.out.print("Would you like to play again? say Yes or No : ");
                    play = read.next().toLowerCase();
                } else if(guess < randNum) System.out.println("\t  -> Hint : you are near to the target.! Try guessing.\n ");
                else System.out.println("\t  -> Hint : you are far away from the target.! Try guessing.\n ");
            }
        }
        System.out.println("\t\t ---- Game is Over ----\n");
        read.close();
    }
    public static void main(String[] args) {
        System.out.println("Welcome to Number Guessing Tutorial.\n");
        guessNum();
    }
}
