import java.util.*;
public class SP_SC {
    public static void main(String[] args) {
        System.out.println("\n Welcome to 2-Players Game");
        System.out.println("===========================================================\n");
        System.out.println("\t\t----Game(Stone Paper Scissor) is Live-----\n");
        SPS();
    }

    static void SPS(){
     Scanner read = new Scanner(System.in);
     System.out.print("\nPlayer-1, please enter your name : ");
     String p1 = read.nextLine();
     System.out.print("\nPlayer-2, please enter your name : ");
     String p2 = read.nextLine();

     String[] players = {p1,p2};

     Map<String,List<String>> gameData = new HashMap<>();
     gameData.put("STONE",new ArrayList<>());
     gameData.put("PAPER",new ArrayList<>());
     gameData.put("SCISSOR",new ArrayList<>());

     for(String p:players){
        boolean choiceValid = false;

        while(!choiceValid){ // run the loop untill the spelling is correct i.e element card is valid
            System.out.print("\n"+p+" ,please select your card among (STONE,PAPER,& SCISSOR): ");
            String choice = read.next().toUpperCase();
           
            if(gameData.containsKey(choice)){
                gameData.get(choice).add(p);
                choiceValid = true;
            }else System.out.println("Invalid card selection,  please correct the spelling and try again.\n");
        }
           System.out.println("\n");
           for(int i = 0; i<5; i++) System.out.print(":");
           System.out.println("\n");
     }

     System.out.println("\n Wait for the Result.........\n");
     try{
       Thread.sleep(3000); // this is for 3sec i.e = 3000 milisec.
     }catch(InterruptedException e){
           System.out.println("Timer is Interrupted!");
     }     
     winner(gameData);
     read.close();
    }

    static void winner(Map<String,List<String>> dict){
        
        List<String> stonePlayers = dict.get("STONE");
        List<String> paperPlayers = dict.get("PAPER");
        List<String> scissorPlayers = dict.get("SCISSOR");
   
        // condition-1 : Base condition.i.e Tie Situation
        if(stonePlayers.size() >1 ||paperPlayers.size() >1 ||scissorPlayers.size() >1){
            System.out.println("Ooops! Tie, Both the players select the same card.Please try again later\n");
            System.out.println("\t\t-----Game is Over!-------\n");
            return;
        }

        System.out.println("\n>>>>> The winner is ........<<<<<\n");
        // condition-2 
        if(stonePlayers.size() == 1 && scissorPlayers.size() == 1){
            String winnerStone = stonePlayers.get(0);
           System.out.println("\nStone Crushes the Scissor. ");
           System.out.println("Congratulation! "+ winnerStone+" , you have won the game!\n");
        } // condition -3 
        else  if(stonePlayers.size() == 1 && paperPlayers.size() == 1){
            String winnerPaper = paperPlayers.get(0);
           System.out.println("\nPaper wrapped the stone. ");
           System.out.println("Congratulation! "+ winnerPaper+" , you have won the game!\n");
        } else{ // condition-4
           String winnerScissor = scissorPlayers.get(0);
           System.out.println("\nScissor cuts the paper. ");
           System.out.println("Congratulation! "+ winnerScissor+" , you have won the game!\n");
        }
            System.out.println("\t\t-----Game is Over!-------\n");
        
    }
}
