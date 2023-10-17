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
        assertFalse(hand.isEmpty());
        assertEquals(1, hand.getCards().size());
        hand.addCard(testCard2);
        assertEquals(2, hand.getCards().size());
    }

    @Test
    public void testPopCard() {
        Card poppedCard;

        hand.addCard(testCard1);
        hand.addCard(testCard2);

        poppedCard = hand.popCard(1);
        assertEquals(testCard2, poppedCard);
        assertThrows(IndexOutOfBoundsException.class,() -> hand.popCard(99));
        assertFalse(hand.isEmpty());

        poppedCard = hand.popCard(0);
        assertEquals(testCard2, poppedCard);
        assertTrue(hand.isEmpty());
        assertThrows(IndexOutOfBoundsException.class,() -> hand.popCard(0));

    }

    @Test
    public void testIsEmpty() {
        assertTrue(hand.isEmpty());

        hand.addCard(testCard1);
        assertFalse(hand.isEmpty());

        hand.popCard(0);
        assertTrue(hand.isEmpty());
    }

    @Test
    public void testToString() {
        // Assuming toString() is implemented in the Hand class
        assertEquals("", hand.toString());

        hand.addCard(testCard1);
        assertEquals("1. RED ONE", hand.toString());
        hand.addCard(testCard2);
        assertEquals("1. RED ONE\n2. WILD CARD", hand.toString());
    }

    @Test
    public void testGetCards() {
        hand.addCard(testCard1);
        ArrayList<Card> cards = hand.getCards();
        assertEquals(1, cards.size());
        assertEquals(testCard1, cards.get(0));
    }
}
