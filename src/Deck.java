/**
 * Deck models the deck of the card game Uno Flip!
 * @author Hubert Dang
 */

import java.util.*;

public class Deck {
    private Stack<Card> deck;
    private Stack<Card> discard;
    private static final int MAX_SIZE = 104;    // should be 112, but not including the 8 flip cards yet

    /**
     * Create a shuffled deck with all the Uno Flip! cards.
     */
    public Deck() {
        deck = new Stack<Card>();
        discard = new Stack<Card>();

        // get array of card colours and ranks
        Card.Colour[] colours = Card.Colour.values();
        Card.Rank[] ranks = Card.Rank.values();

        // add all non-wild cards to deck (2 for each colour)
        for (Card.Colour c: colours) {
            if (c != Card.Colour.WILD) {
                for (Card.Rank r : ranks) {
                    if (r != Card.Rank.WILD_DRAW_2 && r != Card.Rank.WILD) {
                        deck.push(new Card(r, c));
                        deck.push(new Card(r, c));
                    }
                }
            }
        }

        // add 4 wild cards to deck
        for (int i = 0; i < 4; i++) {
            deck.push(new Card(Card.Rank.WILD, Card.Colour.WILD));
        }

        // add 4 wild draw-two cards to deck
        for (int i = 0; i < 4; i++) {
            deck.push(new Card(Card.Rank.WILD_DRAW_2, Card.Colour.WILD));
        }

        Collections.shuffle(deck);
    }


    /**
     * Take a card from the deck.
     * @return The card at the top of the deck.
     */
    public Card takeCard() {
        Card cardToTake = deck.pop();
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
        if (card.isWild()) {
            // need to check for 4 cards because there are 4 of each wild
            if (frequency(card) > 4) {
                throw new IllegalArgumentException("The card object passed already has its max amount in the deck.");
            }
        } else {
            // need to check for 2 cards because there are 2 of each non-wild
            if (frequency(card) > 2) {
                throw new IllegalArgumentException("The card object passed already has its max amount in the deck.");
            }
        }
        discard.push(card);
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
     * Gets the number of occurences of card in the deck (including discard).
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
        return count;
    }
}
