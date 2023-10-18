import java.util.ArrayList;

public class Player {
    private Hand hand;

    private Deck deck;

    private final String name;


    /**
     *  Contractor for the Player Class, initialize the player's name, deck, and hand (giving them the standard 7 cards to start)
     *  @param name - a String to represent the name that the user gives that player
     */
    public Player(String name){
        this.name = name;
        hand = new Hand();
        deck = new Deck();
        addCardToHand(7); // Initialize 7 cards for each player

    }

    /**
     * Class getHandSize prints the current hand size of the player
     */
    public int getHandSize(){
        return hand.size();
    }

    /**
     * Class playCard, plays and removes a card from the players hand specified by the handIndex,
     * Cards that was removed will be put back into the deck's discard pile.
     * @param handIndex - Index of the card which the player wants to play
     * @return - the cards that was just played
     */
    public Card playCard(int handIndex){
        Card cardPopped = hand.popCard(handIndex);
        deck.putCard(cardPopped);
        return cardPopped;
    }

    public void addCardToHand(int numCards){
        for(int i=0; i <numCards; i++){
            Card newCard = deck.takeCard();
            hand.addCard(newCard);
        }
    }


    public String toString(){
        return "Your Cards: \n" + hand.toString();
    }

    public String getName(){
        return name;
    }
}
