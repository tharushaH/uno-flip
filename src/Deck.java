/**
 * Deck models the deck of the card game Uno Flip!
 * @author Hubert Dang
 */

import java.util.*;

/**
 * The Deck class represents the deck of cards used in the game of Uno Flip.
 * The class provides methods for drawing cards, discarding cards, and resting the deck when needed.
 * The class draws cards form the deck stack, which can be used by the player during the game.
 * The class discards cards into the discard stack, handling various rules for card limits.
 * Class keeps track of the frequency of the specific cards in the deck, including the main deck, discard pile and cards in play.
 *
 * @author Hubert Dang
 * Date: 2023-10-18
 */
public class Deck {
    private Stack<Card> deck;   // tracking deck to draw from
    private Stack<Card> discard;    // tracking discard pile
    private ArrayList<Card> inPlay; // keep track of cards not in deck to draw from or discard
    private static final int MAX_SIZE = 112;
    private static final int NUM_CARDS_PER_COLOUR = 2;
    private static final int NUM_CARDS_PER_WILD = 4;    // there are 4 of each wild card

    /**
     * Create a shuffled deck with all the Uno Flip! cards.
     */
    public Deck() {
        deck = new Stack<Card>();
        discard = new Stack<Card>();
        inPlay = new ArrayList<Card>();
    }

    /**
     * Inits the deck to its starting state (full).
     */
    public void initStartingDeck() {
        // add all the non-wild cards of each light/dark pairing to deck
        addNonWildCardsToDeck(Card.Colour.BLUE, Card.Colour.PINK);
        addNonWildCardsToDeck(Card.Colour.GREEN, Card.Colour.TEAL);
        addNonWildCardsToDeck(Card.Colour.RED, Card.Colour.ORANGE);
        addNonWildCardsToDeck(Card.Colour.YELLOW, Card.Colour.PURPLE);

        // add 4 wild cards to deck
        for (int i = 0; i < 4; i++) {
            deck.push(new Card(Card.Rank.WILD, Card.Colour.WILD, Card.Rank.WILD, Card.Colour.WILD_DARK));
        }

        // add 4 wild draw-two/wild-draw-colour cards to deck
        for (int i = 0; i < 4; i++) {
            deck.push(new Card(Card.Rank.WILD_DRAW_2, Card.Colour.WILD, Card.Rank.WILD_DRAW_COLOUR, Card.Colour.WILD_DARK));
        }

        Collections.shuffle(deck);
    }


    /**
     * Take a card from the deck.
     * @return The card at the top of the deck.
     */
    public Card takeCard() {
        Card cardToTake = deck.pop();
        inPlay.add(cardToTake);
        if (size() == 0) {
            resetDeck();
        }
        return cardToTake;
    }


    /**
     * Put a card into the deck's discard pile.
     * @param card The card to put into the deck's discard pile.
     */
    public void putCard(Card card) {
        if (card == null) {
            throw new NullPointerException();
        }
        if (deck.size() == MAX_SIZE) {
            throw new IllegalStateException("The deck already contains all cards, cannot add more.");
        }

        inPlay.remove(card);
        discard.push(card);

        if (card.isWild()) {
            // need to check for 4 cards because there are 4 of each wild
            if (frequency(card) > NUM_CARDS_PER_WILD) {
                discard.pop();
                throw new IllegalArgumentException("The card object passed already has its max amount in the deck.");
            }
        } else {
            // need to check for 2 cards because there are 2 of each non-wild
            if (frequency(card) > NUM_CARDS_PER_COLOUR) {
                discard.pop();
                throw new IllegalArgumentException("The card object passed already has its max amount in the deck.");
            }
        }
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
        numTabs +=1; //increment numTabs to pass to the double nested tags

        StringBuilder xml = new StringBuilder(shorterTab +"<Deck>\n" + longerTab + "<deck>\n");
        for (Card c : deck) {
            xml.append(c.toXML( numTabs + 1)).append("\n");
        }

        xml.append(longerTab + "</deck>\n" + longerTab + "<discard>\n");
        for (Card c : discard) {
            xml.append(c.toXML(numTabs + 1)).append("\n");

        }
        xml.append(longerTab + "</discard>\n" + longerTab + "<inPlay>\n");
        for (Card c : inPlay) {
            xml.append(c.toXML(numTabs + 1)).append("\n");
        }
        xml.append(longerTab + "</inPlay>\n" + shorterTab + "</Deck>");

        return xml.toString();
    }



    /**
     * Return the size of the deck to draw from (not including discard)
     * @return size of the deck to draw from
     */
    public int size() {
        return deck.size();
    }


    /**
     * Resets the deck by shuffling discard and making it the new deck to draw from, and making a new, empty discard.
     * Cards being held by players will not be a part of the new deck to draw from.
     */
    private void resetDeck() {
        Collections.shuffle(discard);
        deck = discard;
        discard = new Stack<Card>();
    }


    /**
     * Gets the number of occurences of card in the deck (including discard) and in play.
     * @param card The card to get the frequency of
     * @return The frequency of the card
     */
    private int frequency(Card card) {
        int count = 0;

        for (Card c: deck) {
            if (c.equals(card)) {
                count++;
            }
        }

        for (Card c: discard) {
            if (c.equals(card)) {
                count++;
            }
        }

        for (Card c: inPlay) {
            if (c.equals((card))) {
                count++;
            }
        }
        return count;
    }


    /**
     * Adds all the non-coloured cards of a colour pair (light and dark pair)
     * @param lightColour The card's light side colour
     * @param darkColour The card's dark side colour
     */
    private void addNonWildCardsToDeck(Card.Colour lightColour, Card.Colour darkColour) {
        for (int i = 0; i < NUM_CARDS_PER_COLOUR; i++) {
            deck.push(new Card(Card.Rank.ONE, lightColour, Card.Rank.ONE, darkColour));
            deck.push(new Card(Card.Rank.TWO, lightColour, Card.Rank.TWO, darkColour));
            deck.push(new Card(Card.Rank.THREE, lightColour, Card.Rank.THREE, darkColour));
            deck.push(new Card(Card.Rank.FOUR, lightColour, Card.Rank.FOUR, darkColour));
            deck.push(new Card(Card.Rank.FIVE, lightColour, Card.Rank.FIVE, darkColour));
            deck.push(new Card(Card.Rank.SIX, lightColour, Card.Rank.SIX, darkColour));
            deck.push(new Card(Card.Rank.SEVEN, lightColour, Card.Rank.SEVEN, darkColour));
            deck.push(new Card(Card.Rank.EIGHT, lightColour, Card.Rank.EIGHT, darkColour));
            deck.push(new Card(Card.Rank.NINE, lightColour, Card.Rank.NINE, darkColour));
            deck.push(new Card(Card.Rank.DRAW_ONE, lightColour, Card.Rank.DRAW_FIVE, darkColour));
            deck.push(new Card(Card.Rank.REVERSE, lightColour, Card.Rank.REVERSE, darkColour));
            deck.push(new Card(Card.Rank.SKIP, lightColour, Card.Rank.SKIP_EVERYONE, darkColour));
            deck.push(new Card(Card.Rank.FLIP, lightColour, Card.Rank.FLIP, darkColour));
        }
    }
}
