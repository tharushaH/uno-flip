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

    //Constants used to indicate the current status
    public static final String  CHALLENGE_STATUS_MESSAGE  = "THE NEXT PLAYER HAS THE OPTION TO CHALLENGE";
    public static final String STANDARD_STATUS = " ";
    public static final String CHALLENGE_STATUS_INNOCENT = "INNOCENT: NEXT PLAYER DRAWS 4 CARDS";
    public static final String CHALLENGE_STATUS_GUILTY = "GUILTY:YOU DRAW 2 CARDS";


    private boolean turnFinished; //true if user has played/drawn a card, else false
    private boolean skipTurn; //true if skipping the next person, else false
    private boolean challenge; //true if next player wants to challenge, false if they do not
    private boolean dontAsk;
    private boolean turnDirection; //true is clockwise(1->2->3->4), false is counterclockwise(1->4->3->2)
    private int numPlayers; //number of players in the game
    private int chosenCardIndex; //user inputted card index
    private int currentTurn; // 0 is player 1, 1 is player 2, 2 is player 3, 3 is player 4
    private int nextPlayerIndex; //index of next player
    private String status; //indicate the status for which the view will update to
    private Deck deck; //deck that will be used for the game
    private Card.Colour currentColour; //current colour of the game
    private Card.Rank currentRank; //current rank of the game
    private Card topCard;//top card that is in play
    private List<UnoFlipView> views;  // list of views
    private ArrayList<TurnSequence> turnSeqs; // arraylist of turn sequences
    private ArrayList<Player> players; //arraylist of players


    /**
     * Constructs a new game of Uno by initializing fields with default settings.
     */
    public UnoFlipModel(){
        this.players = new ArrayList<Player>(); //empty player list
        this.turnDirection = true; //initialize to clockwise
        this.currentTurn = 0; // start from player 1
        this.nextPlayerIndex = 1; // next player is player 2
        this.deck = new Deck(); //create a new deck
        this.currentColour = null; //set colour to null
        this.currentRank = null; //set rank to null
        this.numPlayers = 0; // initialize to 0
        this.chosenCardIndex = -1; //initialize to -1
        this.turnSeqs = new ArrayList<TurnSequence>(); //arraylist for turn sequences
        this.skipTurn = false; // initialize to false
        this.dontAsk = false;  // initialize to false
        for(int i =0;i<=8;i++){
            this.turnSeqs.add(new Number(this)); //Number
        }
        this.turnSeqs.add(new DrawOne(this)); //Draw_One
        this.turnSeqs.add(new Reverse(this)); //Reverse
        this.turnSeqs.add(new Skip(this)); //Skip
        this.turnSeqs.add(new Wild(this)); //Wild
        this.turnSeqs.add(new WildDrawTwo(this)); //Wild Draw Two
        this.turnSeqs.add(new SelfDrawOne(this)); //Self Draw One
        this.views = new ArrayList<UnoFlipView>(); //arrau list of views
    }


    /**
     * Returns the number of players, used for testing only
     * will only set the nunber of players if its between 2-4 players
     * @param numPlayers The number of players
     * @return the number of players that of the game, if invalid will return 0
     */
    public int setNumPlayers(int numPlayers) {

        if(!(numPlayers < 2 || numPlayers > 4)) {  //2-4 players
            this.numPlayers = numPlayers;        //Set the number of players going to be playing
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
     * Adds a player to the arraylist of players.
     * @param player The player to be added
     */
    public void addPlayer(Player player){
        this.players.add(player); // adding player to arraylist of players
    }

    /**
     * Method setUpInitialTopCard is meant to be called by the UnoFlipController to initialize the top card at the start of the game.
     */
    public void setUpInitialTopCard(){

        this.topCard = deck.takeCard();    //draw the first card from deck
        this.turnFinished = false;        //initialize false to allow first player to play/draw a card

        //If topCard selected is a WILD_DRAW_2
        while(this.topCard.getRank().ordinal() == Card.RANK_WILD_DRAW_2){
            this.deck.putCard(this.topCard);      //return card back to deck and get a new one
            this.topCard = this.deck.takeCard();  //draw another card from deck
        }

        //if first card drawn form dec is an action card
        if(this.topCard.getRank().ordinal() > Card.RANK_NUMBER_CARDS) {
            //only execute sequence if action card
            this.turnSeqs.get(this.topCard.getRank().ordinal()).executeSequence(this.topCard);
        }
        //if number don't do anything
        else{
            this.currentColour = this.topCard.getColour();
            this.currentRank = this.topCard.getRank();
            this.status = STANDARD_STATUS;
            notifyViews(); // Notifying view here since it is the last step in the initialization of the game,
        }
    }


    /**
     * Method notifyViews is meant to notify subscribers view about any changes that will affect the view of the UnoFlip Game
     */
    public void notifyViews(){

        //Makse sure there are views in the view arraylist to send UnoFlipEvents to.
        if(!this.views.isEmpty()) {

            //if WILD_DRAW_2 card and next player declines to challenge
            if (this.topCard.isWild() && !this.status.equals(CHALLENGE_STATUS_INNOCENT) && !this.status.equals(CHALLENGE_STATUS_GUILTY) && !this.dontAsk){

                this.status = this.currentColour.toString(); // set current colour as the status

                //notify views
                for( UnoFlipView view: this.views ) {
                    view.handleUnoFlipStatusUpdate( new UnoFlipEvent(this, getCurrentPlayer().getName(), this.topCard.toString(), getCurrentPlayer().toString(),this.status,(this.currentRank == Card.Rank.WILD || this.currentRank == Card.Rank.WILD_DRAW_2)));
                }

            }
            //if a non WILD_DRAW_2 card.
            else{
                //notify views
                for( UnoFlipView view: this.views ) {
                    view.handleUnoFlipStatusUpdate( new UnoFlipEvent(this, getCurrentPlayer().getName(), this.topCard.toString(), getCurrentPlayer().toString(),this.status ,this.currentRank == Card.Rank.WILD ));
                }
            }

        }
    }


    /**
     * PlayTurn method is used to handle game logic request sent by UnoFlipController for when a card is placed by the player
     * or when the player draws a card.
     * @param btnIndex - the index of the cards that is being played, -1 if player draws a card
     */
    public void playTurn(int btnIndex){        // add a
        //player chose to draw a card

        if (!turnFinished) { // If turn is false ( player has not played/ drawn a card)
            chosenCardIndex = btnIndex;

            if (chosenCardIndex == -1) { // SELF DRAW ONE
                if (validSelfDrawOne()){
                    turnSeqs.get(14).executeSequence(null);
                    status = " ";
                    notifyViews();
                    turnFinished = true;
                } else{
                    status = "YOU HAVE PLAYABLE CARD";
                    notifyViews();
                }
                return;
            }
            int index = getCurrentPlayer().getCard(chosenCardIndex).getRank().ordinal();

            if (index == 13){
                turnSeqs.get(index).executeSequence(getCurrentPlayer().playCard(chosenCardIndex));
                turnFinished = true;
            } else if (turnSeqs.get(index).isValid(getCurrentPlayer().getCard(chosenCardIndex))) { //if valid card
                Card playCard = getCurrentPlayer().playCard(chosenCardIndex);
                //Check if winner
                if (isWinner(getCurrentPlayer())) {
                    return;
                }
                turnSeqs.get(index).executeSequence(playCard);

                status = " ";
                //notify view
                notifyViews();
                turnFinished = true;

            } else { // INVALID CARD OR WILD DRAW 2
                //VIEW WILL CREATE A JOPTIONPANE FOR THIS MESSAGE
                status = "THE CARD YOU PLACED DOES NOT MATCH THE TOP CARD. TRY AGAIN";
                notifyViews();
            }
        } else {
            status = "YOUR TURN IS FINSISHED, PRESS NEXT PLAYER";
            notifyViews();

        }
    }


    /**
     * Check if the player has no cards remaining
     * If player has no reamining cards, will update the players score
     * @param player The player that is checked
     * @return True if the player has no cards, false otherwise
     */
    private boolean isWinner(Player player){
        if ( player.getHandSize() == 0 ) {
            getCurrentPlayer().setPlayerScore(getWinnerScore());
            status = "WINNER:" + getCurrentPlayer().getName() + " Has WON !";
            notifyViews();
            return true;

        } else {
            return false;
        }
    }

    /**
     * Draw the amount of cards(n) based on which player(index) will be receiving them
     * @param n The amount of cards to be added to the hand of the player
     * @param index The index of the player that will be receiving cards
     */
    public void drawNCards(int n,int index){
        players.get(index).addCardToHand(n);
        if(index==currentTurn){
            status = players.get(index).getName()+" has drawn a card:"+players.get(index).getCard(players.get(index).getHandSize()-1);

        }
        else{
            status =players.get(index).getName()+" has to draw "+n+" card(s) due to "+topCard;
        }
    }

    /**
     * Go to the turn of the next player based on turn direction
     */
    public void nextTurn() {

        if (turnFinished) { //If the turn is finished, allow player to press next player
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
            if (skipTurn){
                if (turnDirection) { //0->1->2->3
                    currentTurn = (currentTurn + 1) % numPlayers;
                    nextPlayerIndex = (currentTurn + 1) % numPlayers;
                }
                //counterclockwise
                else { //0->3->2->1
                    currentTurn = (currentTurn - 1 + numPlayers) % numPlayers;
                    nextPlayerIndex = (currentTurn - 1 + numPlayers) % numPlayers;
                }
                skipTurn = false;
            }
            status = " ";
            notifyViews();
            turnFinished = false; // reset for next player
        } else { // if player tries to skip turn
            status = "CANNOT SKIP A TURN, EITHER PLAY A CARD FROM THE HAND OR DRAW FROM THE DECK";
            notifyViews();
        }

    }

    /**
     * Returns the score of the winner
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
     * Return the boolean to see if the self draw one is playable.
     * @return return true if valid, otherwise false.
     */
    private boolean validSelfDrawOne(){
        for (int i = 0; i < this.getCurrentPlayer().getHandSize();i++){
            if (this.getCurrentPlayer().getCard(i).getRank() == this.getCurrentRank() || this.getCurrentPlayer().getCard(i).getColour() == this.getCurrentColour()){
                return false;
            }
        }
        return true;
    }

    /**
     * Skip the turn of the next player
     */
    public void skipTurn(){
        skipTurn = true;
    }

    /**
     * Flips the direction of the game.
     */
    public void flipTurnDirection(){
        turnDirection = !turnDirection;
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
     * @return The current colour
     */
    public Card.Colour getCurrentColour() {
        return currentColour;
    }

    /**
     * Gets the current rank of the game.
     * @return The current rank
     */
    public Card.Rank getCurrentRank() {
        return currentRank;
    }

    /**
     * Gets the index of the player whose turn it is.
     * @return The index of the current player.
     */
    public int getCurrentTurn() {
        return currentTurn;
    }

    /**
     * Gets the current player.
     * @return The current player.
     */
    public Player getCurrentPlayer(){
        return players.get(currentTurn);
    }

    /**
     * Gets the index of the next player.
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
     * Returns the string representation of current status
     * @return string representation of current status
     */
    public String getStatus() {
        return status;
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
     * Return the ArrayList of sequences, for testing.
     * @return return a list of the sequences.
     */
    public ArrayList<TurnSequence> getTurnSeqs() {
        return turnSeqs;
    }

    /**
     * Gets the status of the challenge, whether the next player wants to challenge
     * @return true if next player wants to challenge, false if next player does not want to challenge
     */
    public boolean getChallenge(){
        return  challenge;
    }

    /**
     * Get boolean for dontAsk
     * @return true if model should not ask, false otherwise
     */
    public boolean getDontAsk() {
        return dontAsk;
    }

    /**
     * Setting the status
     * @param status - the new status
     */
    public void setStatus(String status ){
        this.status = status;
    }

    /**
     * Sets the current rank of the game.
     * @param currentRank The rank to be set
     */
    public void setCurrentRank(Card.Rank currentRank) {
        this.currentRank = currentRank;
    }

    /**
     * Sets the top card of the game.
     * @param topCard The card to be set as the top card.
     */
    public void setTopCard(Card topCard) {
        this.topCard = topCard;
    }

    /**
     * Set the current colour of the game
     * @param colour The colour to be set as the current colour
     */
    public void setCurrentColour(Card.Colour colour){
        currentColour = colour;
    }

    /**
     * Sets the challenge status for if the next player wants to challenge
     * @param challenge - next player's decision on challenging
     */
    public void setChallenge(boolean challenge){
        this.challenge = challenge;
    }

    /**
     * Set boolean for dontAsk
     * @param dontAsk set the ask permission
     */
    public void setDontAsk(boolean dontAsk) {
        this.dontAsk = dontAsk;
    }

}
