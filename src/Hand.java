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
            message.append(cards.get(i)).append(" ");
        }
        return message.toString();
    }


    /**
     * returns the String representation of the Hand class XML format
     * @param numTabs the number of tabs the main opening tag should start with
     * @return the String representation of Hand Class
     */
    public String toXML(int numTabs) {

        String shorterTab = "";
        String longerTab = "\t";

        for (int i=0; i < numTabs; i++){
             shorterTab += "\t";
        }
        longerTab += shorterTab;

        StringBuilder xml = new StringBuilder(shorterTab + "<Hand> \n" + longerTab);
        for (Card card : cards) {
            xml.append(card.toXML(numTabs + 1));
        }
        xml.append("</hand> \n");
        return xml.toString();
    }

    /**
     * Gets the list of Card objects in the hand.
     *
     * @return an ArrayList of Card objects in the hand
     */
    public ArrayList<Card> getCards(){
        return this.cards;
    }

    /**
     * Checks hand if the cards has a matching colour/wild or not.
     *
     * @param colour
     * @return returns true if the colour exists in the hand or the hand has a wild, false otherwise.
     */
    public boolean colourInHand(Card.Colour colour){
        for (Card card: cards){
            if (card.getColour() == colour){
                return true;
            }
        }
        return false;
    }

    /**
     * Method getScore returns the score of the player's hand based on Uno game wiki scoring system.
     * Scoring:
     *      Number card are counted as their face value.
     *      Action cards are counted as 20 points
     *      Wild and Wild Draw Four cards are counted as 50 points
     * @return the total score of the hand
     */
    public int getScore(){
        int score =0;
        for(Card card: cards){
            if( card.getRank().ordinal() < 9){ //If a number card 1->9
                score += card.getRank().ordinal() + 1;
            } else if (card.getRank().ordinal() >=9 && card.getRank().ordinal() < 12) { // If Action Card
                score += 20;
            } else if (card.getRank().ordinal() > 11){ //If a Wild or Wild Draw Four
                score +=50;
            }
        }
        return score;

    }

    public static void main(String[] args){



    }

}
