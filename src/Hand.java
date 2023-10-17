import java.awt.*;
import java.util.ArrayList;

public class Hand {
    private ArrayList<Card> cards;

    public Hand() {
        this.cards = new ArrayList<Card>();
    }

    public void addCard(Card card) {
        this.cards.add(card);
    }

    public Card popCard(int index) {
        if (index < 0 || index >= cards.size())
            throw new IndexOutOfBoundsException("ERROR: Out of index.");

        return this.cards.remove(index);
    }

    public int size() {
        return this.cards.size();
    }

    @Override
    public String toString() {
        StringBuilder message = new StringBuilder();
        for (int i = 0; i < cards.size(); i++){
            message.append(i + 1).append(". ").append(cards.get(i)).append("\n");
        }
        return message.toString();
    }

    public ArrayList<Card> getCards(){
        return this.cards;
    }
}
