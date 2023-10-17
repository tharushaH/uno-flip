import java.util.ArrayList;

public class Player {
    private Hand hand;

    private Deck deck;

    private final String name;


    public Player(String name){
        this.name = name;
    }

    public boolean hasZeroCards(){
        return true;
    }

    public Card playCard(int handIndex){
        return null;
    }

    public void addCardToHand(int numCards){
    }

    public void printHand(){
    }

    //public Hand getHand(){ return null;}

    public String toString(){
        return null;
    }

    public String getName(){
        return null;
    }
}
