import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
/**
 * The Game class represent a game of Uno Flip. Uno Flip can be played with 2-4 players.
 * This class initializes and manages a game of Uno Flip by managing player turns, displaying
 * the player's cards, validating card placement, scoring and game logic based on what cards were played.
 *
 *
 * @author  Amilesh Nanthakumaran
 * Date: 2023-10-20
 */
public class UnoFlipModel {
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

    private List<UnoFlipView> views;

    private String status;

    private Boolean turnFinished;

    /**
     * Constructs a new game of Uno by initializing fields with default settings.
     */
    public UnoFlipModel(){
        players = new ArrayList<Player>(); //empty player list
        turnDirection = true; //initialize to clockwise
        currentTurn = 0; // start from player 1
        nextPlayerIndex = 1; // next player is player 2
        deck = new Deck(); //create a new deck
        currentColour = null; //set colour to null
        currentRank = null; //set rank to null
        //userInput = new Scanner(System.in); //scanner used for user input
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


        this.views = new ArrayList<UnoFlipView>();
        this.status = null;
        this.turnFinished = false;

    }


    /**
     * Method addUnoFlipView adds a view to the view list
     * @param view - the view that will be added to the list
     */
    public void addUnoFlipView(UnoFlipView view){
        this.views.add(view);
    }

    /**
     * Method removeUnoFlipView removes view from the view list
     * @param view - the view that will be removed from the list
     */
    public void removeUnoFlipView(UnoFlipView view){
        this.views.remove(view);
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
     * @return The index of the current player.
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
     * Returns an ArrayList of players in the current game,used for testing only
     * @return The ArrayList of players
     */
    public ArrayList<Player> getPlayers() {
        return players;
    }

    /**
     * Returns the turn direction,used for testing only
     * @return The turn direction
     */
    public boolean getTurnDirection() {
        return turnDirection;
    }

    /**
     * Returns the number of players,used for testing only
     * @return The number of players
     */
    public int getNumPlayers() {
        return numPlayers;
    }

    /**
     * Returns the chosen card index of the user,used for testing only
     * @return The card index chosen by the user
     */
    public int getChosenCardIndex(){
        return chosenCardIndex;
    }

    /**
     * Returns the top card in play,used for testing only
     * @return The top card
     */
    public Card getTopCard(){
        return topCard;
    }

    /**
     * Returns the number of players, used for testing only
     * will only set the nunber of players if its between 2-4 players
     * @param numPlayers The number of players
     * @return the number of players that of the game, if invalid will return 0
     */
    public int setNumPlayers(int numPlayers) {

        if(!(numPlayers < 2 || numPlayers > 4)) {  //2-4 players
            this.numPlayers = numPlayers;
            return this.numPlayers;
        } else {                                //Invalid number of players
            return 0;
        }
    }


    /**
     * Method addPlayers is meant to be activated by the UnoFlipController to initialize a player in the UnoFlip game
     * @param playerName - the name of the player that will be initialized
     */
    public void createPlayer(String playerName){

        Player p = new Player(playerName); //create player
        addPlayer(p); //add player to arraylist
    }

    /**
     * Method setUpInitialTopCard is meant to be called by the UnoFlipController to initialize the top card at the start of the game.
     */
    public void setUpInitialTopCard(){
        System.out.println("in set up initial top card");
        //draw the first card from deck
        topCard = deck.takeCard();

        //If topcard selected is a WILD_DRAW_2
        while(topCard.getRank().ordinal()==13){
            //return card back to deck and get a new one
            deck.putCard(topCard);
            topCard = deck.takeCard();
        }

        if(topCard.getRank().ordinal()>8) {
            //only execute sequence if action card, if number don't do anything
            turnSeqs.get(topCard.getRank().ordinal()).executeSequence(topCard);
        }
        else{
            currentColour = topCard.getColour();
            currentRank = topCard.getRank();
            status = "STANDARD";
            notifyViews(); // Notifying view here since it is the last step in the initialization of the game,
        }
    }


    /**
     * Method notifyViews is meant to notify subscribers view about any changes that will effect the view of the UnoFlip Game
     *
     */
    public void notifyViews(){
        if(!views.isEmpty()) {
            //send UnoFlipEvent to view.
            for( UnoFlipView view: views ) {
                view.handleUnoFlipStatusUpdate( new UnoFlipEvent(this, getCurrentPlayer().getName(), topCard.toString(), getCurrentPlayer().toString(), status ));
            }

        }
    }


    /**
     * Adds a player to the game.
     *
     * @param player The player to be added
     */
    public void addPlayer(Player player){
        players.add(player);
    }

    /**
     * PlayTurn method is used to handle game logic request sent by UnoFlipController for when a card is placed by the player
     * or when the player draws a card.
     * @param btnIndex - the index of the cards that is being played, -1 if player draws a card
     *
     */
    public void playTurn(int btnIndex){        // add a
        //player chose to draw a card

                chosenCardIndex = btnIndex;

                if(!turnFinished){ // IF Turn Finished is false (player has not played card or drawn card)

                    if(chosenCardIndex == -1){ // SELF DRAW ONE
                        turnSeqs.get(14).executeSequence(null);
                        status = "STANDARD";
                        notifyViews();
                        turnFinished = true;
                        return;
                    }
                    int index = getCurrentPlayer().getCard(chosenCardIndex).getRank().ordinal();

                    if(turnSeqs.get(index).isValid(getCurrentPlayer().getCard(chosenCardIndex))){ //if valid card
                        Card playCard = getCurrentPlayer().playCard(chosenCardIndex);

                        //Check if winner
                        if(isWinner(getCurrentPlayer())){
                            return;
                        }
                        turnSeqs.get(index).executeSequence(playCard);

                        //notify view
                        status = "STANDARD";
                        notifyViews();
                    }
                    else{ // INVALID CARD OR WILD DRAW 2

                        //WILD DRAW TWO
                        if(index== Card.Rank.WILD_DRAW_2.ordinal()){
                            status = "WILD DRAW 2";
                            notifyViews();
                        }
                        else { // INVALID CARD
                            status = "INVALID CARD PLACED";
                            notifyViews();
                        }
                    }
                    turnFinished = true;
                } else {
                    status = "TURN FINISHED";
                    notifyViews();
                    System.out.println("YOUR TURN IS DONE, PRESS NEXT PLAYER!");

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




            //System.out.println("Choose a colour(RED,BLUE,YELLOW,GREEN): ");
            //String input = userInput.nextLine().toUpperCase();
            for(Card.Colour c:Card.Colour.values()){
                if (c.toString().equals(Card.Colour.WILD.toString())){
                    continue;
                }
                if(1== 0){//input.toString().equals(c.toString())){
                   // System.out.println("The colour is now "+c);
                    return c;
                }
            }
            if(!colourSet) {
               // System.out.println("Invalid colour");
            }

        }
        return null;

    }

    /**
     * Check if the player has no cards remaining
     * If player has no reamining cards, will update the players score
     *
     * @param player The player that is checked
     * @return True if the player has no cards, false otherwise
     */
    private boolean isWinner(Player player){

        if ( player.getHandSize() == 0 ) {
            getCurrentPlayer().setPlayerScore(getWinnerScore());
            return true;
        } else {
            return false;
        }
    }

    /**
     * Go to the turn of the next player based on turn direction
     */
    public void nextTurn() {
        if (turnFinished) { // If the player has played a card or drawn one, change player when next turn is pressed

            //clockwise
            if (turnDirection) { //0->1->2->3
                currentTurn = (currentTurn + 1) % numPlayers;
                nextPlayerIndex = (currentTurn + 1) % numPlayers;
            }
            //counterclockwise
            else { //0->3->2->1
                currentTurn = (currentTurn - 1 + numPlayers) % numPlayers;
                nextPlayerIndex = (currentTurn - 1 + numPlayers) % numPlayers;
            }
            status = "STANDARD";
            notifyViews();

            turnFinished = false; //reset for next player
        } else {
            status = "TURN SKIP ERROR";
            notifyViews();
            System.out.println("CANNOT SKIP A TURN, EITHER PLAY A CARD FROM HAND OR DRAW FROM DECK!");
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
           // System.out.println(players.get(index).getName()+" has drawn a card:"+players.get(index).getCard(players.get(index).getHandSize()-1));
        }
        else{
            // System.out.println(players.get(index).getName()+" has to draw "+n+" card(s) due to "+topCard);
        }
    }


    /**
     * Set the current colour of the game
     * @param colour The colour to be set as the current colour
     */
    public void setCurrentColour(Card.Colour colour){
        currentColour = colour;

    }

    /**
     * Returns the score of the winner
     *
     * @return The score of the winner
     */
    private int getWinnerScore(){
        int winnerScore = 0;
        if(topCard.getRank().ordinal() == 8){
            drawNCards(1,nextPlayerIndex); //handle if draw one was last card

        }else if(topCard.getRank().ordinal() == 13){
            //wild draw two is on top
            drawNCards(2,nextPlayerIndex); //handle if wild draw two was last card
        }

        for(Player p: players){
            winnerScore += p.getHandScore();
        }
        return winnerScore;
    }

    /**
     * Return the ArrayList of sequences, for testing.
     *
     * @return return a list of the sequences.
     */
    public ArrayList<TurnSequence> getTurnSeqs() {
        return turnSeqs;
    }

    public static void main(String[] args) {
        UnoFlipModel unoFlipModel = new UnoFlipModel();
        //unoFlipModel.playGame();

    }

}
