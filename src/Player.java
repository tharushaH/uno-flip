import java.util.ArrayList;

public class Player {
    private Hand hand;

    private Deck deck;

    private final String name;


    public Player(String name){

        this.name = name;
        hand = new Hand();

        addCardToHand(7); // Initialize 7 cards for each player


    }

    public int getHandSize(){
        return hand.size();
    }

    public Card playCard(int handIndex){
        return null;
    }

    public void addCardToHand(int numCards){
    }

    public void printHand(){
    }

    public String toString(){
        return null;
    }

    public String getName(){
        return null;
    }
}
