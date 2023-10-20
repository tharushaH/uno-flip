import java.awt.*;
import java.util.ArrayList;

/**
 * The Hand class represents a collection of Card objects held by a player
 * in a card game. It provides methods for adding cards to the hand, removing
 * cards from the hand, getting the size of the hand, and generating a string
 * representation of the hand.
 *
 * Author: Tharusha Herath
 * Date: 2023-10-18
 */
public class Hand {
    private ArrayList<Card> cards;

    /**
     * Constructs an empty Hand object. Initializes an empty arraylist of cards.
     */
    public Hand() {
        this.cards = new ArrayList<Card>();
    }

    /**
     * Adds a Card object to the hand.
     *
     * @param card the Card object to be added to the hand
     */
    public void addCard(Card card) {
        this.cards.add(card);
    }

    /**
     * Removes and returns the Card object at the specified index from the hand.
     *
     * @param index the index of the Card object to be removed
     * @return the Card object removed from the hand
     * @throws IndexOutOfBoundsException if the index is out of range
     */
    public Card popCard(int index) {
        if (index < 0 || index >= cards.size())
            throw new IndexOutOfBoundsException("ERROR: Out of index.");

        return this.cards.remove(index);
    }

    /**
     * Returns the number of cards in the hand.
     *
     * @return the number of cards in the hand
     */
    public int size() {
        return this.cards.size();
    }

    /**
     * Returns a string representation of the hand, including the index and
     * string representation of each card in the hand.
     *
     * @return a string representation of the hand
     */
    @Override
    public String toString() {
        StringBuilder message = new StringBuilder();
        for (int i = 0; i < cards.size(); i++){
            message.append(i + 1).append(". ").append(cards.get(i)).append("\n");
        }
        return message.toString();
    }

    /**
     * Gets the list of Card objects in the hand.
     *
     * @return an ArrayList of Card objects in the hand
     */
    public ArrayList<Card> getCards(){
        return this.cards;
    }

    public boolean colourInHand(Card.Colour colour){
        return false;
    }

}
