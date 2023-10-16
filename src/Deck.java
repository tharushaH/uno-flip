import java.util.Stack;

public class Deck {
    private Stack<Card> deck;
    private Stack<Card> discard;

    public Deck() {}

    public Card takeCard(int numCards) {return null;}

    public void putCard(Card card){}

    public int size() {
        return deck.size();
    }

    private void resetDeck(){}
}
