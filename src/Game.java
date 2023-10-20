import java.util.ArrayList;
import java.util.Scanner;
public class Game {
    private ArrayList<Player> players;
    private boolean turnDirection; //true is clockwise(1->2->3->4), false is counterclockwise(1->4->3->2)
    private int currentTurn; // 0 is player 1, 1 is player 2, 2 is player 3, 3 is player 4
    private int nextPlayerIndex; //index of next player
    private Deck deck;
    private Card.Colour currentColour;

    private Card.Rank currentRank;
    private Scanner userInput;
    private int numPlayers;
    private int chosenCardIndex;

    private Card topCard;

    private Card.Colour colourSetByWild;

    private ArrayList<TurnSequence> turnSeqs;

    private TurnSequence turnSequence;
    private Hand playerhand = new Hand();


    public Game(){
        players = new ArrayList<Player>(); //empty player list
        turnDirection = true; //initialize to clockwise
        currentTurn = 0; // start from player 1
        nextPlayerIndex = 1; // next player is player 2
        deck = new Deck(); //create a new deck
        currentColour = null; //set colour to null
        currentRank = null; //set rank to null
        userInput = new Scanner(System.in); //scanner used for user input
        numPlayers = 0; // initialize to 0
        chosenCardIndex = -1;
        turnSeqs = new ArrayList<turnSequence>();
        turnSeqs.add(new Number());
        turnSeqs.add(new Number());
        turnSeqs.add(new Number());
        turnSeqs.add(new Number());
        turnSeqs.add(new Number());
        turnSeqs.add(new Number());
        turnSeqs.add(new Number());
        turnSeqs.add(new Number());
        turnSeqs.add(new Number());
        turnSeqs.add(new DrawOne());
        turnSeqs.add(new Reverse());
        turnSeqs.add(new Skip());
        turnSeqs.add(new Wild());
        turnSeqs.add(new WildDrawTwo());


    }

    public Card.Colour getCurrentColour() {
        return currentColour;
    }

    public Card.Rank getCurrentRank() {
        return currentRank;
    }

    public void setCurrentRank(Card.Rank currentRank) {
        this.currentRank = currentRank;
    }

    public int getCurrentTurn() {
        return currentTurn;
    }

    public void setCurrentTurn(int currentTurn) {
        this.currentTurn = currentTurn;
    }

    public void setTopCard(Card topCard) {
        this.topCard = topCard;
    }

    public Card.Colour getColourSetByWild() {
        return colourSetByWild;
    }
    public Player getCurrentPlayer(){
        return players.get(currentTurn);
    }
    public void flipTurnDirection(){
        turnDirection = !turnDirection;
    }

    public int getNextTurn(){
        return nextPlayerIndex;
    }
    public void playGame(){
        System.out.println("Welcome to UNO");
        //get number of players
        while(numPlayers < 2 || numPlayers >4) {
            try {
                System.out.println("Enter number of players(2-4): ");
                String input = userInput.nextLine();
                numPlayers = Integer.parseInt(input);
                if(numPlayers < 2 || numPlayers > 4){
                    System.out.println("Invalid number of players");
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid integer input");
            }

        }
        for(int i=0;i<numPlayers;i++){
            System.out.println("Enter name for Player " + (i + 1)+": ");
            String name = userInput.nextLine();
            Player p = new Player(name); //create player
            addPlayer(p); //add player to arraylist


        }
        System.out.println("Game will now begin");
        //draw the first card from deck
        topCard = new Card(Card.Rank.EIGHT, Card.Colour.RED); //deck.takeCard()
        currentColour = topCard.getColour();
        currentRank = topCard.getRank();

        while(true){
            System.out.println("Top card:"+topCard.toString());
            System.out.println(getCurrentPlayer().getName() + "'s turn: ");
            //display player's hand using the printHand()
            getCurrentPlayer().toString();
            //use the player's chosen index to play their turn
            playTurn();
            //check if they have 0 cards


            //if the player is not a winner then move to the next player
            nextTurn();







        }

    }
    private void addPlayer(Player player){
        player.addCardToHand(7); // add 7 cards to hand might not need this bc Player()
        players.add(player);
    }

    private void playTurn(){
        //player chose to draw a card
        boolean turnFinished = false;
        while(!turnFinished){
            try{
                System.out.println("Enter card index to play or 0 to draw a card");
                chosenCardIndex = Integer.parseInt(userInput.nextLine());
                if(chosenCardIndex < 0 || chosenCardIndex > 7){//players.get(currentTurn).getHandSize()
                    System.out.println("Invalid index");
                }
                else{
                    if(chosenCardIndex == 0){
                        turnSeqs.get(indexfordrawone).executeSequence();
                        break;
                    }
                    if(turnSequence.isValid(getCurrentPlayer().getCard(chosenCardIndex-1))){
                        System.out.println("Played: "+getCurrentPlayer().getCard(chosenCardIndex-1).toString());
                        getCurrentPlayer().playCard(getCurrentPlayer().getCard(chosenCardIndex-1));
                        if(isWinner(getCurrentPlayer())){
                            System.out.println(getCurrentPlayer().getName()+" has won the game!");
                            break;
                        }
                        int index = getCurrentPlayer().getCard(chosenCardIndex-1).getRank().ordinal();
                        turnSeqs.get(index).executeSequence(getCurrentPlayer().getCard(chosenCardIndex-1));

                    }

                }

            }catch (NumberFormatException e) {
                System.out.println("Invalid integer input");
            }

        }

    }
    public Card.Colour getColourSelectedByWild(){
        boolean colourSet = false;
        while(!colourSet){
            System.out.println("Choose a colour(RED,BLUE,YELLOW,GREEN): ");
            String input = userInput.nextLine().toUpperCase();
            for(Card.Colour c:Card.Colour.values()){;
                if (c.toString().equals(Card.Colour.WILD.toString())){
                    continue;
                }
                if(input.toString().equals(c.toString())){
                    return c;
                }
            }
            if(!colourSet) {
                System.out.println("Invalid colour");
            }

        }




    }








    private boolean isWinner(Player player){
        return player.getHandSize()==0;
    }

    public void nextTurn(){
        //clockwise
        if(turnDirection){
            currentTurn = (currentTurn+1) % numPlayers;
            nextPlayerIndex = currentTurn + 1;
        }
        //counterclockwise
        else{
            if(currentTurn == 0){
                currentTurn = numPlayers-1;
            }
            else{
                currentTurn -= 1;
            }
            nextPlayerIndex = currentTurn - 1;
        }
    }

    public void skipTurn(){
        nextTurn();
        nextTurn();
    }

    public void drawNCards(int n,int index){
        players.get(index).addCardToHand(n);
        if(index==currentTurn){
            System.out.println(players.get(index).getName()+" has drawn a card: ");
        }
        else{
            System.out.println(players.get(index).getName()+" has to draw "+n+" cards due to "+topCard.toString());
        }
    }

    public void reverse(){
        //CW -> CCW
        if(turnDirection){
            turnDirection = false;
            System.out.println("Going counterclockwise");
        }
        //CCW -> CW
        else{
            turnDirection = true;
            System.out.println("Going clockwise");

        }
    }

    public void setCurrentColour(Card.Colour colour){
        currentColour = colour;
        System.out.println("The colour is now "+currentColour.toString());
    }

    public static void main(String[] args) {
        Game game = new Game();
        game.playGame();

    }

}
