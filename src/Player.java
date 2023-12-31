import javax.swing.text.html.StyleSheet;
import java.util.ArrayList;

/**
 * The Player class represents the players objects within the Uno Flip game.
 * The class manages the player's hand by adding cards, playing cards, checking the size of the hand and content of the hand/
 * The class keeps track of the player's score by allowing the addition of points at the end of each round.
 * The class also provides information about the player to the game class, such as the player's name, current score, as current hand.
 * Additionally, the player class interacts with a static Deck object to draw cards during the game.
 *
 * @author Ranveer Dhaliwal
 * Date: 2023-10-18
 */
public class Player {
    protected Hand hand;
    protected String name;

    protected int playerScore;


    /**
     *  Contractor for the Player Class, initialize the player's name, deck, and hand (giving them the standard 7 cards to start)
     *  @param name - a String to represent the name that the user gives that player
     */
    public Player(String name){
        this.name = name;
        this.hand = new Hand();
        this.playerScore = 0;

    }

    /**
     * Method getHandSize prints the current hand size of the player
     * @return - the number of cards in the player's hand
     */
    public int getHandSize(){
        return this.hand.size();
    }

    /**
     * Method playCard, plays and removes a card from the player's hand specified by handIndex.
     * Cards that was removed will be put back into the deck's discard pile.
     * @param handIndex - Index of the card which the player wants to play
     * @return - the cards that was just played
     */
    public Card playCard(int handIndex, Deck deck){

        //Make sure that the user selects a valid index in the hand
        if ( (handIndex < this.getHandSize()) && (handIndex >= 0) ){
            Card cardPopped = this.hand.popCard(handIndex);
            deck.putCard(cardPopped);
            return cardPopped;
        } else {
            return null;
        }

    }

    /**
     *  Method addCardToHand, adds a specified number of cards to the player's hand specified by numCards.
     *  Cards will be taken from the static deck and placed into the hand of the player
     * @param numCards - The number of cards that the user will add to their deck
     */
    public void addCardToHand(int numCards, Deck deck){
        for(int i=0; i <numCards; i++){
            Card newCard = deck.takeCard();
            this.hand.addCard(newCard);
        }
    }

    /**
     * Adds a card to the player's hand.
     * @param card The card to add.
     */
    public void addCardToHand(Card card) {
        if (card != null) {
            hand.addCard(card);
        }
    }

    /**
     * Empties the player's hand.
     */
    public void emptyHand() {
        hand.empty();
    }

    /**
     * Method toString(), returns the player's hand of cards.
     * @return - a String containing the player's hand
     */

    public String toString(){
        return this.hand.toString();
    }

    /**
     * Method getName returns the name of the player
     * @return - the name of the player
     */
    public String getName(){
        return this.name;
    }

    /**
     * Method colourInHand shows whether a specilc colour of cards appears in the player's hand
     * @param colour - the colour that is being checking for in the hand
     * @return - a boolean describing whether the colour appears in the player's hand
     */
    public boolean colourInHand(Card.Colour colour){
        return this.hand.colourInHand(colour);
    }

    /**
     * Returns the card at the given index.
     * @param index - the index of the card in the hand
     * @return returns a Card in the Hand at the index.
     */
    public Card getCard(int index){
        return this.hand.getCards().get(index);
    }


    /**
     * Method getHandScore returns the amount of points a player's hand is worth
     * @return the value of the player's hand
     */
    public int getHandScore(){
        return hand.getScore();
    }


    /**
     * Method setPlayerScore sets or adds to the player score
     * @param calculatedScore - score the player has won that round
     */
    public void setPlayerScore(int calculatedScore){
        this.playerScore += calculatedScore;
    }

    /**
     * Method getPlayerScore returns the player's score
     * @return the score of the player
     */
    public int getPlayerScore(){
        return this.playerScore;
    }

    /**
     * Returns the player's hand.
     * @return The player's hand.
     */
    public Hand getHand() {
        return hand;
    }

    /**
     * Returns an XML representation of the Player as a String.
     * @param numTabs The number of tabs the main opening tag should start with.
     * @return XML representation of the Player
     */
    public String toXML(int numTabs) {
        String baseTabLength = "";    // any tabs within the return String will append to this base tab length

        // add tabs to the base tab length
        for (int i = 0; i < numTabs; i++) {
            baseTabLength += "\t";
        }

        return baseTabLength + "<Player>\n" +
                getHand().toXML(numTabs + 1) + "\n"+ baseTabLength+"\t" +
                "<name>" + getName() + "</name>\n" + baseTabLength + "\t" +
                "<playerScore>" + getPlayerScore() + "</playerScore>\n" + baseTabLength +
                "</Player>";
    }

    /**
     * Sets the player's name.
     * @param name The name to set.
     */
    public void setName(String name) {
        this.name = name;
    }
}




