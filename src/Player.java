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

    public void addCardToHand(int numCards, Card card){
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
