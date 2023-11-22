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

    private boolean turnFinished;
    private boolean skipTurn;
    private boolean skipEveryone;
    private boolean challenge; //true if next player wants to challenge a wild draw 2, false if they do not
    private boolean turnDirection; //true is clockwise(1->2->3->4), false is counterclockwise(1->4->3->2)
    private int numPlayers;
    private int chosenCardIndex;
    private int currentTurn; // 0-indexed (ex. 0 is player 1, 1 is player 2, 2 is player 3, 3 is player 4)
    private int nextPlayerIndex;
    private String status; //indicate the status for which the view will update to
    private Deck deck;
    private Card.Colour currentColour;
    private Card.Rank currentRank;
    private Card topCard;
    private List<UnoFlipView> views;
    private ArrayList<TurnSequence> turnSeqs;
    private ArrayList<Player> players;

    public static final int DRAW_ONE_BUTTON = -1;

    //Constants used for Turn sequence
    public static final int TURN_SEQ_SELF_DRAW_ONE = 14;
    public static final int TURN_SEQ_DRAW_FIVE = 15;


    public static final int TURN_SEQ_FLIP = 15;

    //Constants used to indicate the current status
    public static final String STATUS_CHALLENGE_MESSAGE  = "THE NEXT PLAYER HAS THE OPTION TO CHALLENGE";
    public static final String STATUS_STANDARD = " ";
    public static final String STATUS_CHALLENGE_INNOCENT = "INNOCENT: NEXT PLAYER DRAWS 4 CARDS";
    public static final String STATUS_CHALLENGE_GUILTY = "GUILTY:YOU DRAW 2 CARDS";
    public static final String STATUS_PLAYABLE_CARD = "YOU HAVE PLAYABLE CARD";
    public static final String DRAW_CARD = "YOU HAVE DRAWN A CARD";
    public static final String STATUS_INVALID_CARD_BEING_PLACED = "THE CARD YOU PLACED DOES NOT MATCH THE TOP CARD. TRY AGAIN";
    public static final String STATUS_PLAYER_SKIPPING_TURN = "CANNOT SKIP A TURN, EITHER PLAY A CARD FROM THE HAND OR DRAW FROM THE DECK";
    public static final String STATUS_TURN_FINISHED = "YOUR TURN IS FINISHED, PRESS NEXT PLAYER";
    public static final String STATUS_DONE = "done";
    
    /**
     * Constructs a new game of Uno Flip by initializing fields with default settings.
     */
    public UnoFlipModel(){
        this.players = new ArrayList<Player>();
        this.turnSeqs = new ArrayList<TurnSequence>(); // list of game sequences based on the different card ranks played
        this.views = new ArrayList<UnoFlipView>();
        this.turnDirection = true; //initialize to clockwise
        this.skipEveryone = false;
        this.currentTurn = 0;
        this.nextPlayerIndex = currentTurn +1;
        this.deck = new Deck();
        this.currentColour = null;
        this.currentRank = null;
        this.numPlayers = 0;
        this.chosenCardIndex = -2; // initialize to -2 to indicate that it has not been set to a valid index yet
        this.skipTurn = false;
        this.turnFinished = false;    //initialize false to ensure first player can play/draw a card
        this.status = STATUS_STANDARD;

        //adding the same turn sequence 9 times because the first 9 ranks (all number cards) play out the same way
        for(int i =0;i<=8;i++){
            this.turnSeqs.add(new Number(this)); //Number
        }

        //adding the turn sequence for the action cards into turnSeqs Arraylist
        this.turnSeqs.add(new DrawOne(this));
        this.turnSeqs.add(new Reverse(this));
        this.turnSeqs.add(new Skip(this));
        this.turnSeqs.add(new Wild(this));
        this.turnSeqs.add(new WildDrawTwo(this));
        this.turnSeqs.add(new SelfDrawOne(this));

        this.turnSeqs.add(new DrawFive(this));
        this.turnSeqs.add(new SkipEveryone(this)); //must be index 16

        this.turnSeqs.add(new Flip(this));

    }


    /**
     * Sets the number of players
     * @param numPlayers The number of players (between 2-4)
     * @throws IllegalArgumentException if the provided number of players is not within the valid range of 2-4 players
     */
    public void setNumPlayers(int numPlayers) {

        if( numPlayers < 2 || numPlayers > 4){
            throw new IllegalArgumentException("Number of players must be between 2-4");
        }
        this.numPlayers = numPlayers;
    }

    /**
     * Creates player and adds them to the game
     * @param playerName - the name of the player that will be created and added to the players list
     */
    public Player createPlayer(String playerName){
        return new Player(playerName);
    }

    /**
     * Adds a player to the arraylist of players.
     * @param player The player to be added
     */
    public void addPlayer(Player player){
        this.players.add(player); // adding player to arraylist of players
    }

    /**
     * Initialize the top card at the start of the game.
     */
    public void setUpInitialTopCard(){

        this.topCard = deck.takeCard();

        //rules don't allow wild draw 2 to be first card, keep drawing until a different card is drawn
        while(this.topCard.getRank().ordinal() == Card.RANK_WILD_DRAW_2 || this.topCard.getRank().ordinal() == Card.Rank.WILD.ordinal()){
            this.deck.putCard(this.topCard);
            this.topCard = this.deck.takeCard();  //redraw the topCard
        }
        //if first card drawn from deck is an action card (non-number card)
        if(this.topCard.getRank().ordinal() > Card.RANK_NUMBER_CARDS) {
            this.turnSeqs.get(this.topCard.getRank().ordinal()).executeSequence(this.topCard); //execute sequence if action card

        } else{
            //number card drawn
            this.currentColour = this.topCard.getColour();
            this.currentRank = this.topCard.getRank();
            this.status = STATUS_STANDARD;

        }
        notifyViews();
    }



    /**
     *  Notify views subscribed to that model about the game state changes
     */
    public void notifyViews(){

        //make sure there are views in the view arraylist to send UnoFlipEvents to
        if(!this.views.isEmpty()){

            boolean isWildDraw = this.topCard.isWild() && !this.status.equals(STATUS_CHALLENGE_INNOCENT) && !this.status.equals(STATUS_CHALLENGE_GUILTY);



            String statusToUpdate;

            // If the current top card is a Wild Draw 2 and the next player declines to challenge
            if (isWildDraw) {
                statusToUpdate = this.currentColour.toString(); //set status as the current colour chosen by the player (ex: RED)

            } else {
                statusToUpdate = this.status;
            }

            //Sends events to the view to update based on different game situations
            for (UnoFlipView view : this.views) {
                view.handleUnoFlipStatusUpdate(new UnoFlipEvent(this, getCurrentPlayer().getName(), this.topCard.toString(), getCurrentPlayer().toString(), statusToUpdate, this.turnFinished));
            }
        }
        this.status = STATUS_STANDARD;
    }


    /**
     * PlayTurn method is used to handle game logic request sent by UnoFlipController for when a card is placed by the player
     * or when the player draws a card.
     * @param cardIndex - the index of the cards that is being played, -1 if player draws a card
     */
    public void playTurn(int cardIndex){

        //if player has not played/drawn a card yet, allow player to play a card or draw from the deck
        if (!this.turnFinished) {
            this.chosenCardIndex = cardIndex;

            //if player draws a card from the deck
            if (this.chosenCardIndex == DRAW_ONE_BUTTON) {

                if (validSelfDrawOne()){
                    this.turnSeqs.get(TURN_SEQ_SELF_DRAW_ONE).executeSequence(null); // null is passed since no card is being played in this sequence, instead player will draw card from deck
                    this.status = DRAW_CARD;
                    this.turnFinished = true;

                //player has a playable card, player still has to complete their turn
                } else{
                    this.status = STATUS_PLAYABLE_CARD;
                }
                notifyViews();
                return;
            }

            int rank = getCurrentPlayer().getCard(this.chosenCardIndex).getRank().ordinal();

            //if the card wanting to be placed is a Wild Draw 2 or Wild
            if (rank == Card.RANK_WILD_DRAW_2 || rank == Card.Rank.WILD.ordinal()){
                this.turnSeqs.get(rank).executeSequence(getCurrentPlayer().playCard(this.chosenCardIndex));
                this.turnFinished = true;

            } else if (this.turnSeqs.get(rank).isValid(getCurrentPlayer().getCard(this.chosenCardIndex))) {
                Card playCard = getCurrentPlayer().playCard(this.chosenCardIndex);

                //check if winner
                if (isWinner(getCurrentPlayer())) {
                    return;
                }

                this.turnSeqs.get(rank).executeSequence(playCard);
                this.status = STATUS_STANDARD;

                this.turnFinished = true;

            //if an invalid card
            } else {
                this.status = STATUS_INVALID_CARD_BEING_PLACED;

            }
        } else {
           this.status = STATUS_TURN_FINISHED;

        }
        notifyViews();
    }


    /**
     * Check if the player has no cards remaining
     * If player has no remaining cards, will update the players score
     * @param player The player that is checked
     * @return True if the player has no cards, false otherwise
     */
    private boolean isWinner(Player player){
        if (player.getHandSize() == 0) {
            getCurrentPlayer().setPlayerScore(getWinnerScore());
            this.status = "WINNER:" + getCurrentPlayer().getName() + " HAS WON !"; // (EX. "WINNER: Player 1 HAS WON!")
            notifyViews();
            return true;

        } else {
            return false;
        }
    }

    /**
     * Draw n cards for the player
     * @param n The amount of cards to be added to the hand of the player
     * @param playerIndex The index of the player that will be receiving cards
     */
    public void drawNCards(int n,int playerIndex){
        this.players.get(playerIndex).addCardToHand(n);
    }

    /**
     * Go to the turn of the next player based on turn direction
     */
    public void nextTurn() {

        if (this.turnFinished) {
            int numPasses=1; //going to next player

            if(skipTurn){
                numPasses =2; //skipping the next player
            }

            if(skipEveryone){
                numPasses = numPlayers; //skip all players

            }

            //change the current player's turn based on the numPasses
            for( int i =0; i < numPasses ; i++){
                if (this.turnDirection) {
                    this.currentTurn = (this.currentTurn + 1) % this.numPlayers;
                    this.nextPlayerIndex = (this.currentTurn + 1) % this.numPlayers;

                    //counterclockwise (ex. 0->3->2->1)
                } else {
                    this.currentTurn = (this.currentTurn - 1 + this.numPlayers) % this.numPlayers;
                    this.nextPlayerIndex = (this.currentTurn - 1 + this.numPlayers) % this.numPlayers;
                }
            }

            this.status = STATUS_STANDARD;
            clearActionCardFlags();

        }else {
            this.status = STATUS_PLAYER_SKIPPING_TURN;

        }

        notifyViews();

    }

    /**
     * challenge method to be used for wild draw 2 method to implement challenge feature.
     * Checks to see if the next player has decided to challenge or not.
     * If the next player challenges, they are either 'guilty' or 'innocent'
     * If the next player does not challenge, game continues as normal
     */
    public void challenge(){
        if(challenge) { // If next player challenges
            if(isWildDrawTwoValid()) { // IF WILD DRAW 2 is valid
                this.drawNCards(4, this.getNextTurn());
                status = STATUS_CHALLENGE_INNOCENT;
            } else {                    // If WILD DRAW 2 is not valid
                this.drawNCards(2, this.getCurrentTurn());
                status = STATUS_CHALLENGE_GUILTY;
            }
        } else {
            this.drawNCards(2, this.getNextTurn());
            this.setStatus(UnoFlipModel.STATUS_DONE);
        }
        notifyViews();
    }

    /**
     * Checks to see if there is a playable card in hand before allowing player to check if player is guilty or not
     * @return return true if valid time to play wild draw 2, otherwise false.
     */
    private boolean isWildDrawTwoValid(){
        for (int i = 0; i < this.getCurrentPlayer().getHandSize();i++){
            if (this.getCurrentPlayer().getCard(i).getRank() == this.getCurrentRank() || this.getCurrentPlayer().getCard(i).getColour() == this.getCurrentColour()){
                // If the hand contains an action card, playing wild draw 2 is still valid
                if(this.getCurrentPlayer().getCard(i).getRank() == Card.Rank.REVERSE || this.getCurrentPlayer().getCard(i).getRank() == Card.Rank.SKIP || this.getCurrentPlayer().getCard(i).getRank() == Card.Rank.DRAW_ONE){
                    continue;
                }
                return false;
            }
        }
        return true;
    }

    /**
     * Returns the score of the winner, score is generated based on the cards opponents are left holding
     * @return The score of the winner
     */
    private int getWinnerScore(){
        int winnerScore = 0;

        /*handling if the last card played is a draw card (ex. RED_DRAW_ONE or WILD_DRAW_2)
          cards must still be given to next players before winner's score is calculated */
        if(this.topCard.getRank().ordinal() == Card.RANK_DRAW_ONE){
            drawNCards(1,this.nextPlayerIndex);
        } else if(this.topCard.getRank().ordinal() == Card.RANK_WILD_DRAW_2){
            drawNCards(2,this.nextPlayerIndex);
        }

        for(Player p: this.players){
            winnerScore += p.getHandScore();
        }
        return winnerScore;
    }

    /**
     * Checks to see if there is a playable card in hand before allowing player to draw a card from the deck
     * @return return true if valid to draw a card from deck, otherwise false.
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
     * Flips the turn direction of the game.
     */
    public void flipTurnDirection(){
        this.turnDirection = !this.turnDirection;
    }

    /**
     * Clear action card flags
     */
    public void clearActionCardFlags(){
        this.skipTurn = false;
        this.skipEveryone = false;
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
     * @return The current colour
     */
    public Card.Colour getCurrentColour() {
        return this.currentColour;
    }

    /**
     * Gets the current rank of the game.
     * @return The current rank
     */
    public Card.Rank getCurrentRank() {
        return this.currentRank;
    }

    /**
     * Gets the index of the player whose turn it is.
     * @return The index of the current player.
     */
    public int getCurrentTurn() {
        return this.currentTurn;
    }

    /**
     * Gets the current player.
     * @return The current player.
     */
    public Player getCurrentPlayer(){
        return this.players.get(this.currentTurn);
    }

    /**
     * Gets the index of the next player.
     * @return The index of the next player.
     */
    public int getNextTurn(){
        return this.nextPlayerIndex;
    }

    /**
     * Returns an ArrayList of players in the current game,used for testing only
     * @return The ArrayList of players
     */
    public ArrayList<Player> getPlayers() {
        return this.players;
    }

    /**
     * Returns the turn direction,used for testing only
     * @return The turn direction
     */
    public boolean getTurnDirection() {
        return this.turnDirection;
    }

    /**
     * Returns the number of players,used for testing only
     * @return The number of players
     */
    public int getNumPlayers() {
        return this.numPlayers;
    }

    /**
     * Returns the chosen card index of the user,used for testing only
     * @return The card index chosen by the user
     */
    public int getChosenCardIndex(){
        return this.chosenCardIndex;
    }

    /**
     * Returns the top card in play,used for testing only
     * @return The top card
     */
    public Card getTopCard(){
        return this.topCard;
    }

    /**
     * Set value of skipTurn to true to indicate that the next player should be skipped
     */
    public void setSkipTurnFlag(){
        this.skipTurn = true;
    }

    /**
     * Return the ArrayList of sequences, for testing.
     * @return return a list of the sequences.
     */
    public ArrayList<TurnSequence> getTurnSeqs() {
        return this.turnSeqs;
    }

    /**
     * Gets the status of the challenge, whether the next player wants to challenge
     * @return true if next player wants to challenge, false if next player does not want to challenge
     */
    public boolean getChallenge(){
        return this.challenge;
    }

    /**
     * Gets the status of the skipEveryone, whether the all players are going to be skipped
     * @return true if the skipping all players, false if not
     */
    public boolean getSkipEveryoneFlag(){
        return this.skipEveryone;
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
        this.currentColour = colour;
        notifyViews();
    }

    /**
     * Sets the challenge status for if the next player wants to challenge
     * @param challenge - next player's decision on challenging
     */
    public void setChallengeFlag(boolean challenge){
        this.challenge = challenge;
        notifyViews();
    }

    /**
     * set the status of the turnFinished variable - used for testing
     * @param turnFinished state of the players turn
     */
    public void setTurnFinished(boolean turnFinished){
        this.turnFinished = turnFinished;
    }

    /**
     * Return the boolean representation of if the players turn is finished
     *
     * @return returns true if players turn is finished, otherwise false
     */
    public boolean getTurnFinished(){
        return this.turnFinished;
    }

    /**
     * Set value of skipEveryone to true to indicate that the all players should be skipped, and current player can go again
     */
    public void setSkipEveryoneFlag(){
        this.skipEveryone = true;
    }

}
