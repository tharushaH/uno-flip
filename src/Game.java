import java.util.ArrayList;
import java.util.Scanner;
/**
 * The Game class represent a game of Uno Flip. This class initializes
 * and manages a game of Uno Flip by managing player turns, the deck
 * and game logic based on what cards were played.
 *
 * Date: 2023-10-20
 * @author  Amilesh Nanthakumaran
 */
public class Game {
    private ArrayList<Player> players; //arraylist of players
    private boolean turnDirection; //true is clockwise(1->2->3->4), false is counterclockwise(1->4->3->2)
    private int currentTurn; // 0 is player 1, 1 is player 2, 2 is player 3, 3 is player 4
    private int nextPlayerIndex; //index of next player
    private Deck deck; //deck that will be used for the game
    private Card.Colour currentColour; //current colour of the game

    private Card.Rank currentRank; //current rank of the game
    private Scanner userInput; //scanner object for user input
    private int numPlayers; //number of players in the game
    private int chosenCardIndex; //user inputted card index

    private Card topCard;//top card that is in play

    private Card.Colour colourSetByWild; //colour chosen by the user

    private ArrayList<TurnSequence> turnSeqs; // arraylist of turn sequences




    /**
     * Constructs a new game of Uno by initializing fields with default settings.
     */
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
        chosenCardIndex = -1; //initialize to -1
        turnSeqs = new ArrayList<TurnSequence>(); //arraylist for turn sequences
        for(int i =0;i<=8;i++){
            turnSeqs.add(new Number(this)); //Number
        }
        turnSeqs.add(new DrawOne(this)); //Draw_One
        turnSeqs.add(new Reverse(this)); //Reverse
        turnSeqs.add(new Skip(this)); //Skip
        turnSeqs.add(new Wild(this)); //Wild
        turnSeqs.add(new WildDrawTwo(this)); //Wild Draw Two
        turnSeqs.add(new SelfDrawOne(this)); //Self Draw One



    }

    /**
     * Gets the current colour of the game.
     *
     * @return The current colour
     */
    public Card.Colour getCurrentColour() {
        return currentColour;
    }

    /**
     * Gets the current rank of the game.
     *
     * @return The current rank
     */
    public Card.Rank getCurrentRank() {
        return currentRank;
    }

    /**
     * Sets the current rank of the game.
     *
     * @param currentRank The rank to be set
     */
    public void setCurrentRank(Card.Rank currentRank) {
        this.currentRank = currentRank;
    }

    /**
     * Gets the index of the player whose turn it is.
     *
     * @return The inde of the current player.
     */
    public int getCurrentTurn() {
        return currentTurn;
    }

    /**
     * Sets the top card of the game.
     *
     * @param topCard The card to be set as the top card.
     */
    public void setTopCard(Card topCard) {
        this.topCard = topCard;
    }
    /**
     * Gets the current player.
     *
     * @return The current player.
     */
    public Player getCurrentPlayer(){
        return players.get(currentTurn);
    }
    /**
     * Flips the direction of the game.
     */
    public void flipTurnDirection(){
        turnDirection = !turnDirection;
    }
    /**
     * Gets the index of the next player.
     *
     * @return The index of the next player.
     */
    public int getNextTurn(){
        return nextPlayerIndex;
    }
    /**
     * Starts and manages the Uno game including getting user input.
     */
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
                else{
                    System.out.println("Number of players is: "+numPlayers);
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
        topCard = deck.takeCard();
        while(topCard.getRank().ordinal()==13){
            //return card back to deck and get a new one
            deck.putCard(topCard);
            topCard = deck.takeCard();
        }
        if(topCard.getRank().ordinal()>8){
            //only execute sequence if action card, if number don't do anything
            turnSeqs.get(topCard.getRank().ordinal()).executeSequence(topCard);
        }
        currentColour = topCard.getColour();
        currentRank = topCard.getRank();


        while(true){
            System.out.println("Top card:"+topCard.toString());
            System.out.println(getCurrentPlayer().getName() + "'s turn: ");
            //display player's hand using the printHand()
            System.out.println(getCurrentPlayer().toString());
            //use the player's chosen index to play their turn
            playTurn();
            //check if they have 0 cards


            //if the player is not a winner then move to the next player
            if(isWinner(getCurrentPlayer())){
                System.out.println(getCurrentPlayer().getName()+" has won the game!");
                break;
            }








        }

    }

    /**
     * Adds a player to the game.
     *
     * @param player The player to be added
     */
    private void addPlayer(Player player){
        players.add(player);
    }

    /**
     * Manages a player's turn by validating their card placement and handling the card.
     */
    private void playTurn(){
        //player chose to draw a card
        boolean turnFinished = false;
        while(!turnFinished){
            try{
                System.out.println("Enter card index to play or 0 to draw a card");
                chosenCardIndex = Integer.parseInt(userInput.nextLine());
                if(chosenCardIndex < 0 || chosenCardIndex > players.get(currentTurn).getHandSize()){
                    System.out.println("Invalid index");
                }
                else{
                    if(chosenCardIndex == 0){
                        turnSeqs.get(14).executeSequence(null);
                        break;
                    }

                    int index = getCurrentPlayer().getCard(chosenCardIndex-1).getRank().ordinal();
                    if(turnSeqs.get(index).isValid(getCurrentPlayer().getCard(chosenCardIndex-1))){
                        Card playCard = getCurrentPlayer().playCard(chosenCardIndex-1);
                        System.out.println("Played: "+playCard.toString());

                        if(isWinner(getCurrentPlayer())){
                            break;
                        }

                        turnSeqs.get(index).executeSequence(playCard);
                        turnFinished=true;

                    }
                    else{
                        System.out.println("You tried to play "+getCurrentPlayer().getCard(chosenCardIndex-1)+" but that card does not match the top card. Try again.");

                    }

                }

            }catch (NumberFormatException e) {
                System.out.println("Invalid integer input");
            }

        }

    }

    /**
     * Gets colour from player to handle the wild cards
     *
     * @return The colour that was selected
     */
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
                    System.out.println("The colour is now "+c);
                    return c;
                }
            }
            if(!colourSet) {
                System.out.println("Invalid colour");
            }

        }
        return null;



    }

    /**
     * Check if the player has no cards remaining
     *
     * @param player The player that is checked
     * @return True if the player has no cards, false otherwise
     */
    private boolean isWinner(Player player){
        return player.getHandSize()==0;
    }

    /**
     * Go to the turn of the next player based on turn direction
     */
    public void nextTurn(){
        //clockwise
        if(turnDirection){ //0->1
            currentTurn = (currentTurn+1) % numPlayers; //next person playing
            nextPlayerIndex = (currentTurn+1) % numPlayers; // for giving cards
        }
        //counterclockwise
        else{ //0->3->2->1   a=0 b=1
            currentTurn = (currentTurn-1 + numPlayers)%numPlayers;
            nextPlayerIndex = (currentTurn-1+numPlayers)%numPlayers;

            }


        }


    /**
     * Skip the turn of the next player
     */
    public void skipTurn(){
        nextTurn();
        nextTurn();
    }

    /**
     * Draw the amount of cards(n) based on which player(index) will be receiving them
     * @param n The amount of cards to be added to the hand of the player
     * @param index The index of the player that will be receiving cards
     */
    public void drawNCards(int n,int index){
        players.get(index).addCardToHand(n);
        if(index==currentTurn){
            System.out.println(players.get(index).getName()+" has drawn a card:"+players.get(index).getCard(players.get(index).getHandSize()-1));
        }
        else{
            System.out.println(players.get(index).getName()+" has to draw "+n+" card(s) due to "+topCard.toString());
        }
    }


    /**
     * Set the current colour of the game
     * @param colour The colour to be set as the current colour
     */
    public void setCurrentColour(Card.Colour colour){
        currentColour = colour;

    }

    public static void main(String[] args) {
        Game game = new Game();
        game.playGame();

    }

}
