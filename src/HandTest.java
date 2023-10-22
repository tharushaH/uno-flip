import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.util.ArrayList;
import static org.junit.Assert.*;

public class HandTest {

    private Hand hand;
    private Card testCard1;
    private Card testCard2;

    @Before
    public void setUp() {
        hand = new Hand();
        testCard1 = new Card(Card.Rank.ONE, Card.Colour.RED);
        testCard2 = new Card(Card.Rank.WILD, Card.Colour.WILD);
    }



    @Test
    public void testAddCard() {
        hand.addCard(testCard1);
        assertEquals(1, hand.size());
        hand.addCard(testCard2);
        assertEquals(2, hand.size());
        hand.popCard(1);
        assertEquals(1, hand.size());
        hand.addCard(testCard2);
        assertEquals(2, hand.size());
    }

    @Test
    public void testPopCard() {
        Card poppedCard;

        hand.addCard(testCard1);
        hand.addCard(testCard2);

        poppedCard = hand.popCard(1);
        assertEquals(testCard2, poppedCard);
        assertEquals(1, hand.size());

        assertThrows(IndexOutOfBoundsException.class,() -> hand.popCard(99));


        poppedCard = hand.popCard(0);
        assertEquals(testCard1, poppedCard);
        assertEquals(0, hand.size());

        assertThrows(IndexOutOfBoundsException.class,() -> hand.popCard(0));
    }

    @Test
    public void testSize() {
        assertEquals(0, hand.size());

        hand.addCard(testCard1);
        assertEquals(1, hand.size());

        hand.popCard(0);
        assertEquals(0, hand.size());
    }

    @Test
    public void testToString() {
        // Assuming toString() is implemented in the Hand class
        assertEquals("", hand.toString());

        hand.addCard(testCard1);
        assertEquals("1. RED ONE\n", hand.toString());
        hand.addCard(testCard2);
        assertEquals("1. RED ONE\n2. WILD CARD\n", hand.toString());
        hand.popCard(0);
        assertEquals("1. WILD CARD\n", hand.toString());

    }

    @Test
    public void testGetCards() {
        hand.addCard(testCard1);
        ArrayList<Card> cards = hand.getCards();
        assertEquals(1, cards.size());
        assertEquals(testCard1, cards.get(0));
    }

    @Test
    public void testColourInHand(){
        hand.addCard(testCard1);
        hand.addCard(testCard2);
        assertTrue(hand.colourInHand(Card.Colour.RED));
        assertFalse(hand.colourInHand(Card.Colour.BLUE));
        assertFalse(hand.colourInHand(Card.Colour.YELLOW));
        assertFalse(hand.colourInHand(Card.Colour.GREEN));

        hand.popCard(0);
        assertFalse(hand.colourInHand(Card.Colour.RED));
        assertFalse(hand.colourInHand(Card.Colour.BLUE));
        assertFalse(hand.colourInHand(Card.Colour.YELLOW));
        assertFalse(hand.colourInHand(Card.Colour.GREEN));

    }

    @Test
    public void  testGetScore(){

        //No cards in hand currently, score should be 0
        assertEquals(0, hand.getScore());

        //add two cards to the hand
        hand.addCard(testCard1); // RED ONE
        hand.addCard(testCard2); // WILD

        assertEquals(51, hand.getScore());

        //Remove card, so there is only one card in hand (Wild card in hand)
        hand.popCard(0);

        assertEquals(50, hand.getScore());
    }
}
