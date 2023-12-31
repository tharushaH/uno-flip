import java.io.*;
import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.List;

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
    private boolean turnDirection; //true is clockwise(1->2->3->4), false is counterclockwise(1->4->3->2)
    private int numPlayers;
    private int chosenCardIndex;
    private int currentTurn; // 0-indexed (ex. 0 is player 1, 1 is player 2, 2 is player 3, 3 is player 4)
    private int nextPlayerIndex;
    private String status; //indicate the status for which the view will update to
    private Deck deck;
    private Card.Colour currentColour;
    private Card.Colour previousColour;
    private Card.Rank currentRank;
    private Card.Rank previousRank;
    private Card topCard;
    private List<UnoFlipView> views;
    private ArrayList<TurnSequence> turnSeqs;
    private ArrayList<Player> players;
    private ArrayList<String> playerScores;
    private Boolean isWinner;

    public static final int NUM_STARTING_CARDS = 7;
    public static final int DRAW_ONE_BUTTON = -1;

    //Constants used for Turn sequence
    public static final int TURN_SEQ_SELF_DRAW_ONE = 18;
    public static final int TURN_SEQ_SKIP_EVERYONE = 15;
    public static final int TURN_SEQ_DRAW_FIVE = 14;
    public static final int TURN_SEQ_FLIP = 17;

    //Constants used to indicate the current status
    public static final String STATUS_CHALLENGE_MESSAGE  = "THE NEXT PLAYER HAS THE OPTION TO CHALLENGE";
    public static final String STATUS_STANDARD = " ";
    public static final String STATUS_LIGHT_CHALLENGE_INNOCENT = "INNOCENT: NEXT PLAYER DRAWS 4 CARDS"; // for wild draw 2
    public static final String STATUS_LIGHT_CHALLENGE_GUILTY = "GUILTY: YOU DRAW 2 CARDS";   // for wild draw 2
    public static final String STATUS_DARK_CHALLENGE_INNOCENT = "INNOCENT: NEXT PLAYER DRAWS AN ADDITIONAL 2 CARDS"; // for wild draw colour
    public static final String STATUS_DARK_CHALLENGE_GUILTY = "GUILTY: YOU DRAW CARDS UNTIL DRAWING YOUR CHOSEN COLOUR";   // for wild draw colour
    public static final String STATUS_PLAYABLE_CARD = "YOU HAVE PLAYABLE CARD";
    public static final String DRAW_CARD = "YOU HAVE DRAWN A CARD";
    public static final String STATUS_INVALID_CARD_BEING_PLACED = "THE CARD YOU PLACED DOES NOT MATCH THE TOP CARD. TRY AGAIN";
    public static final String STATUS_PLAYER_SKIPPING_TURN = "CANNOT SKIP A TURN, EITHER PLAY A CARD FROM THE HAND OR DRAW FROM THE DECK";
    public static final String STATUS_TURN_FINISHED = "YOUR TURN IS FINISHED, PRESS NEXT PLAYER";
    public static final String STATUS_DONE = "done";
    public static final String AI_DRAW_CARD = "\nAI HAS DRAWN CARD";
    public static final String AI_PLAYED_CARD = "\nAI HAS PLAYED CARD: ";
    public static final String CURRENT_STATE_MODEL_DATA = "CurrentStateModelData.xml";
    public static final String CURRENT_STATE_MODEL_DECK = "CurrentStateModelDeck.xml";
    public static final String CURRENT_STATE_MODEL_PLAYERS = "CurrentStateModelPlayers.xml";
    public static final String PAST_STATE_MODEL_DATA = "PastStateModelData.xml";
    public static final String PAST_STATE_MODEL_DECK = "PastStateModelDeck.xml";
    public static final String PAST_STATE_MODEL_PLAYERS = "PastStateModelPlayers.xml";
    public static final String TEMP_STATE_MODEL_DATA = "TempStateModelData.xml";
    public static final String TEMP_STATE_MODEL_DECK = "TempStateModelDeck.xml";
    public static final String TEMP_STATE_MODEL_PLAYERS = "TempStateModelPlayers.xml";
    public static final String XML_MODEL_DATA_FLAG = "ModelData";
    public static final String XML_MODEL_DECK_FLAG = "ModelDeck";
    public static final String XML_MODEL_PLAYERS_FLAG = "ModelPlayers";

    /**
     * Constructs a new game of Uno Flip by initializing fields with default settings.
     */
    public UnoFlipModel(){
        this.players = new ArrayList<Player>();
        this.turnSeqs = new ArrayList<TurnSequence>(); // list of game sequences based on the different card ranks played
        this.views = new ArrayList<UnoFlipView>();
        this.playerScores = new ArrayList<String>();
        this.turnDirection = true; //initialize to clockwise
        this.skipEveryone = false;
        this.currentTurn = 0;
        this.nextPlayerIndex = currentTurn +1;
        this.deck = new Deck();
        this.deck.initStartingDeck(); // need to initialize with starting cards
        this.currentColour = Card.Colour.NULL;
        this.currentRank = Card.Rank.NULL;
        this.numPlayers = 0;
        this.chosenCardIndex = -2; // initialize to -2 to indicate that it has not been set to a valid index yet
        this.skipTurn = false;
        this.turnFinished = false;    //initialize false to ensure first player can play/draw a card
        this.status = STATUS_STANDARD;
        this.isWinner = false;

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

        this.turnSeqs.add(new DrawFive(this));
        this.turnSeqs.add(new SkipEveryone(this));
        this.turnSeqs.add(new WildDrawColour(this));
        this.turnSeqs.add(new Flip(this));

        this.turnSeqs.add(new SelfDrawOne(this));
    }


    /**
     * Sets the number of players
     * @param numPlayers The number of players (between 2-4)
     * @throws IllegalArgumentException if the provided number of players is not within the valid range of 2-4 players
     */
    public void setNumPlayers(int numPlayers) {

        if( numPlayers < 2 || numPlayers > 12){
            throw new IllegalArgumentException("Number of players must be between 2-12");
        }
        this.numPlayers = numPlayers;
    }

    /**
     * Creates player and adds them to the game
     * @param playerName - the name of the player that will be created and added to the players list
     * @return new player created
     */
    public Player createPlayer(String playerName){
        Player player = new Player(playerName);
        player.addCardToHand(NUM_STARTING_CARDS, this.deck); // Initialize 7 cards for each player

        return player;
    }

    /**
     * Creates AI and adds them to the game
     *
     * @return new AI created
     */
    public AI createAIPlayer(){
        AI ai = new AI();
        ai.addCardToHand(NUM_STARTING_CARDS, this.deck);
        return ai;
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
        setUpInitialPlayerScore();
        notifyViews();
    }

    /**
     * set up Player scores at the start of the game or when a saved game is loaded.
     */
    public void setUpInitialPlayerScore(){
        for( int i=0; i < numPlayers ; i ++){
            playerScores.add(players.get(i).getName() + "'s score: " + players.get(i).getPlayerScore());

        }

    }



    /**
     *  Notify views subscribed to that model about the game state changes
     */
    public void notifyViews(){
        //make sure there are views in the view arraylist to send UnoFlipEvents to
        if(!this.views.isEmpty()){

            boolean isWildDraw = this.topCard.isWild() && !this.status.equals(STATUS_LIGHT_CHALLENGE_INNOCENT) &&
                    !this.status.equals(STATUS_LIGHT_CHALLENGE_GUILTY) && !this.status.equals(STATUS_DARK_CHALLENGE_INNOCENT)
                    && !this.status.equals(STATUS_DARK_CHALLENGE_GUILTY);

            String statusToUpdate;

            // If the current top card is a Wild Draw 2 and the next player declines to challenge
            if (isWildDraw && !isWinner) {
                statusToUpdate = this.currentColour.toString(); //set status as the current colour chosen by the player (ex: RED)
            } else {
                statusToUpdate = this.status;
            }

            //Sends events to the view to update based on different game situations
            for (UnoFlipView view : this.views) {
                view.handleUnoFlipStatusUpdate(new UnoFlipEvent(this, getCurrentPlayer().getName(), this.topCard.toString(), getCurrentPlayer().toString(), statusToUpdate, this.players.get(currentTurn) instanceof AI,  this.turnFinished, this.currentColour, this.playerScores));
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
            if (getCurrentPlayer().getCard(this.chosenCardIndex).isWild()){
                this.turnSeqs.get(rank).executeSequence(getCurrentPlayer().playCard(this.chosenCardIndex, this.deck));
                this.turnFinished = true;
                //check if winner
                if (isWinner(getCurrentPlayer())) {
                    return;
                }

            } else if (this.turnSeqs.get(rank).isValid(getCurrentPlayer().getCard(this.chosenCardIndex))) {
                Card playCard = getCurrentPlayer().playCard(this.chosenCardIndex, this.deck);
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
     * playAITurn method is used to handle game logic request sent by UnoFlipController to play a card or
     * draw a card for the AI player.
     */
    public void playAITurn(){
        AI aiPlayer = (AI) this.players.get(currentTurn);
        int chosenAICardIndex = aiPlayer.playAICard(this.currentColour, this.currentRank);
        if(chosenAICardIndex == DRAW_ONE_BUTTON){
            this.turnSeqs.get(TURN_SEQ_SELF_DRAW_ONE).executeSequence(null); // null is passed since no card is being played in this sequence, instead player will draw card from deck
            this.status = AI_DRAW_CARD;
        } else {
            int rank = getCurrentPlayer().getCard(chosenAICardIndex).getRank().ordinal();

            if (getCurrentPlayer().getCard(chosenAICardIndex).isWild()){
                Card playCard = getCurrentPlayer().playCard(chosenAICardIndex, this.deck);
                this.turnSeqs.get(rank).executeSequence(playCard);
                this.status = AI_PLAYED_CARD + playCard.toString();
                //check if winner
                if (isWinner(getCurrentPlayer())) {
                    return;
                }
            } else{
                Card playCard = getCurrentPlayer().playCard(chosenAICardIndex, this.deck);

                //check if winner
                if (isWinner(getCurrentPlayer())) {
                    return;
                }

                this.turnSeqs.get(rank).executeSequence(playCard);
                this.status = AI_PLAYED_CARD + "\n" + playCard.toString();
            }
        }
        this.turnFinished = true;
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
            player.setPlayerScore(getWinnerScore());
            this.status = "WINNER:" + getCurrentPlayer().getName() + " HAS WON !"; // (EX. "WINNER: Player 1 HAS WON!")
            this.isWinner = true;
            updatePlayerScores();
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
        this.players.get(playerIndex).addCardToHand(n, this.deck);
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
     * Checks if a player is guilty/innocent after playing a wild draw 2 or
     * wild draw colour card.
     *
     * @return true if the player is guilty, false if innocent
     */
    public boolean challenge(){
        if (getTopCard().getRank() == Card.Rank.WILD_DRAW_2) {
            return !isWildDrawTwoValid();
        } else {
            return !isWildDrawColourValid();
        }
    }

    /**
     * Draws cards for the current player according to which challenge
     * they were found guilty (wild draw 2 or wild draw colour)
     */
    public void guiltyConsequences() {
        if (getCurrentRank() == Card.Rank.WILD_DRAW_2) {
            drawNCards(2, getCurrentTurn());
            status = STATUS_LIGHT_CHALLENGE_GUILTY;
        } else {
            // guilty for wild draw colour
            drawCardUntilColour(getCurrentColour(), currentTurn);
            status = STATUS_DARK_CHALLENGE_GUILTY;
        }
        notifyViews();
    }

    /**
     * Draws cards for the next player according to which challenge
     * the current player was found innocent (wild draw 2 or wild draw colour)
     */
    public void innocentConsequences() {
        if (getCurrentRank() == Card.Rank.WILD_DRAW_2) {
            this.drawNCards(4, this.getNextTurn());
            status = STATUS_LIGHT_CHALLENGE_INNOCENT;
        } else {
            // innocent for wild draw colour
            drawCardUntilColour(getCurrentColour(), nextPlayerIndex);
            drawNCards(2, nextPlayerIndex);
            status = STATUS_DARK_CHALLENGE_INNOCENT;
        }
        notifyViews();
    }

    /**
     * Checks to see if there is a playable card in hand before allowing player to check if player is guilty or not
     * @return return true if valid time to play wild draw 2, otherwise false.
     */
    private boolean isWildDrawTwoValid(){
        for (int i = 0; i < this.getCurrentPlayer().getHandSize();i++){
            if (this.getCurrentPlayer().getCard(i).getRank() == getPreviousRank() || this.getCurrentPlayer().getCard(i).getColour() == getPreviousColour()){
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
     * Returns if playing the wild draw colour card was valid or not.
     * @return true if valid, fales otherwise
     */
    private boolean isWildDrawColourValid() {
        return !(getCurrentPlayer().colourInHand(getPreviousColour()));
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
     * Update each player's score
     */
    public void updatePlayerScores(){
        for( int i=0; i < numPlayers ; i ++){
            playerScores.set(i,players.get(i).getName() + "'s score: " + players.get(i).getPlayerScore());
        }
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
     * Sets the turn direction of the game to clockwise or counter-clockwise.
     * @param turnDirection true to set clockwise, false to set counter-clockwise.
     */
    public void setTurnDirection(boolean turnDirection) {
        this.turnDirection = turnDirection;
    }

    /**
     * Sets the chosen card index.
     * @param chosenCardIndex The index of the chosen card.
     */
    public void setChosenCardIndex(int chosenCardIndex) {
        this.chosenCardIndex = chosenCardIndex;
    }

    /**
     * Sets the current turn.
     * @param currentTurn The index of the current turn.
     */
    public void setCurrentTurn(int currentTurn) {
        this.currentTurn = currentTurn;
    }

    /**
     * Sets the index of the next player.
     * @param nextPlayerIndex The index of the next player.
     */
    public void setNextPlayerIndex(int nextPlayerIndex) {
        this.nextPlayerIndex = nextPlayerIndex;
    }

    /**
     * Sets isWinner to indicate if there is a winner in this game.
     * @param winner true if there is a winner, false otherwise
     */
    public void setWinner(Boolean winner) {
        isWinner = winner;
    }

    /**
     * Sets the game's deck
     * @param deck The game's deck
     */
    public void setDeck(Deck deck) {
        this.deck = deck;
    }

    /**
     * Sets the game's players
     * @param players The game's players
     */
    public void setPlayers(ArrayList<Player> players) {
        this.players = players;
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
     * Return a boolean of if the next player is an AI or not
     *
     * @return true if the next player is an AI, false otherwise
     */
    public boolean isNextPlayerAI(){
        int nextIndex;
        if (this.turnDirection) {
            nextIndex = (this.currentTurn + 1) % this.numPlayers;
            //counterclockwise (ex. 0->3->2->1)
        } else {
            nextIndex = (this.currentTurn - 1 + this.numPlayers) % this.numPlayers;
        }

        return this.getPlayers().get(nextIndex) instanceof AI;
    }

    /**
     * Returns boolean that represents if current player is AI or not
     *
     * @return true if current player is AI, otherwise false
     */
    public boolean isCurrentPlayerAI(){
       return this.getPlayers().get(currentTurn) instanceof AI;
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
     * Returns the current side of the Cards (light or dark)
     * @return true if it's light side, false if it's dark side
     */
    public boolean getCardSide() {
        return Card.getSide();
    }

    /**
     * Flips the current side of the cards in the game.
     */
    public void flipCardSide() {
        Card.flipSide();
    }

    /**
     * Set value of skipTurn to true to indicate that the next player should be skipped
     */
    public void setSkipTurnFlag(){
        this.skipTurn = true;
    }

    /**
     * Sets the value of skipTurn to true or false.
     * @param skipTurn Boolean to set skipTurn to.
     */
    public void setSkipTurn(boolean skipTurn) {
        this.skipTurn = skipTurn;
    }

    /**
     * Gets the value of the skipTurn field, which indicates if the next player's turn is skipped
     * @return true if the flag is set, false otherwise
     */
    public boolean getSkipTurnFlag() {
        return skipTurn;
    }

    /**
     * Return the ArrayList of sequences, for testing.
     * @return return a list of the sequences.
     */
    public ArrayList<TurnSequence> getTurnSeqs() {
        return this.turnSeqs;
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
        currentColour = colour;
        notifyViews();
    }

    /**
     * Sets the previous colour of the game.
     * @param colour Colour to hold
     */
    public void setPreviousColour(Card.Colour colour) {
        previousColour = colour;
    }

    /**
     * Gets the previosu colour.
     * @return The hold colour.
     */
    public Card.Colour getPreviousColour() {
        return previousColour;
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

    /**
     * Sets the value of skipEveryone to true or false.
     * @param skipEveryone The boolean to set skipEveryone to
     */
    public void setSkipEveryone(boolean skipEveryone) {
        this.skipEveryone = skipEveryone;
    }

    /**
     * Returns the current status of the game, specified by constants defined as fields
     * in this class.
     * @return Current status of the game
     */
    public String getStatus() {
        return status;
    }

    /**
     * Keeps drawing cards for a player until they draw a card of the chosen colour.
     * @param chosenColour The chosen colour
     * @param playerIndex
     */
    public void drawCardUntilColour(Card.Colour chosenColour, int playerIndex) {
        boolean colourDrawn = false;    // indicates if the current colour has been drawn yet
        do {
            drawNCards(1, playerIndex);
            Card cardDrawn = players.get(playerIndex).getCard(players.get(playerIndex).getHandSize()-1);    // the card just drawn is their last card
            if (cardDrawn.getColour() == chosenColour) {
                colourDrawn = true;
            }
        } while (!colourDrawn); // keep drawing until you get the chosen colour
    }

    /**
     * Returns the previous rank of the game.
     * @return The previous rank.
     */
    public Card.Rank getPreviousRank() {
        return previousRank;
    }

    /**
     * Sets the previous rank of the game.
     * @param previousRank the updated previous rank.
     */
    public void setPreviousRank(Card.Rank previousRank) {
        this.previousRank = previousRank;
    }

    /**
     * Returns the boolean representation of the games winner
     *
     * @return true if the game has a winner, otherwise false
     */
    public boolean isWinner(){
        return this.isWinner;
    }

    public boolean getSkipTurn(){
        return this.skipTurn;
    }
    public boolean getSkipEveryone(){
        return this.skipEveryone;
    }
    public boolean getIsWinner(){
        return this.isWinner;
    }

    public void setIsWinner(boolean isWinner){
        this.isWinner = isWinner;
    }

    public Deck getDeck() {
        return deck;
    }

    /**
     * Returns an XML representation of the players in model.
     * @return xml representation of the players in model.
     */
    public String modelPlayersToXML(){
        StringBuilder xml = new StringBuilder("<players>");

        for(Player p : players){
            try{
                // check if AI with downcasting
                AI ai = (AI) p;
                xml.append(ai.toXML(1));

            } catch(Exception ex){
                // player object
                xml.append("\n"+p.toXML(1));
            }

        }
        xml.append("\n </players>");
        return  xml.toString();
    }

    /**
     * Returns an XML representation of the deck in model.
     * @return xml representation of the deck in model.
     */
    public String modelDeckToXML(){
        return deck.toXML(0);
    }

    /**
     * Returns an XML representation of the data in model.
     * @return xml representation of the data in model.
     */
    public String modelDataToXML(){
        StringBuilder xml = new StringBuilder("<UnoFlipModel>");
        xml.append("\n\t <turnFinished>"+this.getTurnFinished()+"</turnFinished>");
        xml.append("\n\t <skipTurn>"+this.getSkipTurn()+"</skipTurn>");
        xml.append("\n\t <skipEveryone>"+this.getSkipEveryone()+"</skipEveryone>");
        xml.append("\n\t <turnDirection>"+this.getTurnDirection()+"</turnDirection>");
        xml.append("\n\t <numPlayer>"+this.getNumPlayers()+"</numPlayer>");
        xml.append("\n\t <chosenCardIndex>"+this.getChosenCardIndex()+"</chosenCardIndex>");
        xml.append("\n\t <currentTurn>"+this.getCurrentTurn()+"</currentTurn>");
        xml.append("\n\t <nextPlayerIndex>"+this.getNextTurn()+"</nextPlayerIndex>");
        xml.append("\n\t <status>"+this.getStatus()+"</status>");
        xml.append("\n\t <currentColour>"+this.getCurrentColour()+"</currentColour>");
        xml.append("\n\t <previousColour>"+this.getPreviousColour()+"</previousColour>");
        xml.append("\n\t <currentRank>"+this.getCurrentRank()+"</currentRank>");
        xml.append("\n\t <previousRank>"+this.getPreviousRank()+"</previousRank>");
        xml.append("\n\t <topCard>");
        xml.append("\n"+this.topCard.toXML(2));
        xml.append("\n\t </topCard>");
        xml.append("\n\t <isWinner>"+this.getIsWinner()+"</isWinner>");
        xml.append("\n</UnoFlipModel>");
        return xml.toString();
    }

    /**
    * Undo players last turn 
    */
    public void undoRedoTurn(){
        // Save current state to temp files
        exportToXMLFile(UnoFlipModel.TEMP_STATE_MODEL_DATA, UnoFlipModel.XML_MODEL_DATA_FLAG);
        exportToXMLFile(UnoFlipModel.TEMP_STATE_MODEL_DECK, UnoFlipModel.XML_MODEL_DECK_FLAG);
        exportToXMLFile(UnoFlipModel.TEMP_STATE_MODEL_PLAYERS, UnoFlipModel.XML_MODEL_PLAYERS_FLAG);

        // Import previous state
        importFromXMLFile( PAST_STATE_MODEL_DATA,  PAST_STATE_MODEL_PLAYERS, PAST_STATE_MODEL_DECK);

        // Update previous state with temp
        swapTempPrevXML(UnoFlipModel.TEMP_STATE_MODEL_DATA, UnoFlipModel.PAST_STATE_MODEL_DATA);
        swapTempPrevXML(UnoFlipModel.TEMP_STATE_MODEL_DECK, UnoFlipModel.PAST_STATE_MODEL_DECK);
        swapTempPrevXML(UnoFlipModel.TEMP_STATE_MODEL_PLAYERS, UnoFlipModel.PAST_STATE_MODEL_PLAYERS);

        notifyViews();
    }

    /**
     * Restarts the game by setting the game state to the initial game state
     * while preserving each player's score.
     */
    public void restartGame() {
        this.turnDirection = true; //initialize to clockwise
        this.skipEveryone = false;
        this.currentTurn = 0;
        this.nextPlayerIndex = currentTurn +1;
        this.deck = new Deck();
        this.deck.initStartingDeck(); // need to initialize with starting cards
        this.currentColour = Card.Colour.NULL;
        this.currentRank = Card.Rank.NULL;
        this.chosenCardIndex = -2; // initialize to -2 to indicate that it has not been set to a valid index yet
        this.skipTurn = false;
        this.turnFinished = false;    //initialize false to ensure first player can play/draw a card
        this.status = STATUS_STANDARD;
        this.isWinner = false;

        setUpInitialTopCard();

        for (Player p: players) {
            p.emptyHand();
            p.addCardToHand(NUM_STARTING_CARDS, deck);
        }
        notifyViews();
    }

    public void loadGame(){
        importFromXMLFile(CURRENT_STATE_MODEL_DATA,CURRENT_STATE_MODEL_PLAYERS, CURRENT_STATE_MODEL_DECK);

        notifyViews();
    }

    public void savePrev(){
        exportToXMLFile(UnoFlipModel.PAST_STATE_MODEL_DATA, UnoFlipModel.XML_MODEL_DATA_FLAG);
        exportToXMLFile(UnoFlipModel.PAST_STATE_MODEL_DECK, UnoFlipModel.XML_MODEL_DECK_FLAG);
        exportToXMLFile(UnoFlipModel.PAST_STATE_MODEL_PLAYERS, UnoFlipModel.XML_MODEL_PLAYERS_FLAG);
    }




    public void saveGame(){
        exportToXMLFile(UnoFlipModel.CURRENT_STATE_MODEL_DATA, UnoFlipModel.XML_MODEL_DATA_FLAG);
        exportToXMLFile(UnoFlipModel.CURRENT_STATE_MODEL_DECK, UnoFlipModel.XML_MODEL_DECK_FLAG);
        exportToXMLFile(UnoFlipModel.CURRENT_STATE_MODEL_PLAYERS, UnoFlipModel.XML_MODEL_PLAYERS_FLAG);
    }

    public void importFromXMLFile(String dataFileName, String playersFileName, String deckFileName){
        ModelDataParser parser = new ModelDataParser();
        try{
            UnoFlipModel temp = parser.readXMLModelDataFile(dataFileName,playersFileName, deckFileName);
            this.setTurnFinished(temp.getTurnFinished());
            this.setSkipTurn(temp.getSkipTurn());
            this.setSkipEveryone(temp.getSkipEveryone());
            this.setTurnDirection(temp.getTurnDirection());
            this.setNumPlayers(temp.getNumPlayers());
            this.setChosenCardIndex(temp.getChosenCardIndex());
            this.setCurrentTurn(temp.getCurrentTurn());
            this.setNextPlayerIndex(temp.getNextTurn());
            this.setStatus(temp.getStatus());
            this.setCurrentColour(temp.getCurrentColour());
            this.setPreviousColour(temp.getPreviousColour());
            this.setCurrentRank(temp.getCurrentRank());
            this.setPreviousRank(temp.getPreviousRank());
            this.setTopCard(temp.getTopCard());
            this.setIsWinner(temp.getIsWinner());
            this.players = temp.getPlayers();
            this.deck = temp.getDeck();
            TurnSequence.setUnoFlipModel(this);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Exports the game XML to File
     * @param fileName name of the file the game XMl will be exported too
     */
    public void exportToXMLFile(String fileName, String xmlFlag){
        try {
            PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(fileName)));
            switch (xmlFlag){
                case XML_MODEL_DATA_FLAG:
                    out.write(this.modelDataToXML());
                    out.close();
                    break;
                case XML_MODEL_DECK_FLAG:
                    out.write(this.modelDeckToXML());
                    out.close();
                    break;
                case XML_MODEL_PLAYERS_FLAG:
                    out.write(this.modelPlayersToXML());
                    out.close();
                    break;
            }

        } catch (IOException e) {
            e.getMessage();
        }
    }

    /**
     * Swap the contents of the temp files to the previous files.
     */
    private void swapTempPrevXML(String sourcePath, String destPath){
        try {

            // Use BufferedReader to read from the source file
            try (BufferedReader reader = new BufferedReader(new FileReader(sourcePath))) {
                // Use BufferedWriter to write to the destination file
                try (BufferedWriter writer = new BufferedWriter(new FileWriter(destPath))) {
                    // Read each line from the source and write it to the destination
                    String line;
                    while ((line = reader.readLine()) != null) {
                        writer.write(line);
                        writer.newLine(); // Add a newline after each line (adjust if needed)
                    }
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
