import java.util.ArrayList;

public class Player {
    private Hand hand;

    private static Deck deck = new Deck();

    private final String name;


    /**
     *  Contractor for the Player Class, initialize the player's name, deck, and hand (giving them the standard 7 cards to start)
     *  @param name - a String to represent the name that the user gives that player
     */
    public Player(String name){
        this.name = name;
        this.hand = new Hand();
        addCardToHand(7); // Initialize 7 cards for each player

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
    public Card playCard(int handIndex){

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
    public void addCardToHand(int numCards){
        for(int i=0; i <numCards; i++){
            Card newCard = deck.takeCard();
            this.hand.addCard(newCard);
        }
    }

    /**
     * Method toString(), returns the player's hand of cards.
     * @return - a String containing the player's hand
     */

    public String toString(){
        return "Your Cards: \n" + this.hand;
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
     * @param index
     * @return returns a Card in the Hand at the index.
     */
    public Card getCard(int index){
        return this.hand.getCards().get(index);
    }





}
