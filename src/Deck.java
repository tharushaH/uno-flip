import java.util.Stack;

public class Deck {
    private Stack<Card> deck;
    private Stack<Card> discard;
    private static final int MAX_SIZE = 108;

    public Deck() {}

    public Card takeCard() {return null;}

    public void putCard(Card card) {
    }

    public int size() {
        return deck.size();
    }

    private void resetDeck(){}
}
