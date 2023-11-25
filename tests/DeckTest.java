import org.junit.*;
import static org.junit.Assert.*;

public class DeckTest {
    private Deck deck;
    final static int MAX_SIZE = 112;

    @Before
    public void setUp() {
        deck = new Deck();
    }

    @Test
    public void testDeckConstructor() {
        assertEquals(MAX_SIZE, deck.size());
    }

    @Test
    public void testTakeCard() {
        // should return a Card and size should decrease
        assertNotNull(deck.takeCard());
        assertEquals(MAX_SIZE - 1, deck.size());
        assertNotNull(deck.takeCard());
        assertEquals(MAX_SIZE - 2, deck.size());
        assertNotNull(deck.takeCard());
        assertEquals((MAX_SIZE - 3), deck.size());
    }

    @Test
    public void testTakeCardFromEmptyDeck() {
        // empty out the deck by taking all the cards
        for (int i = 0; i < MAX_SIZE; i++) {
            deck.takeCard();
        }
        assertEquals(0, deck.size());    // discard pile has nothing to make new deck with, should be 0

        deck = new Deck();

        // empty out deck
        for (int i = 0; i < MAX_SIZE - 5; i++) {
            deck.takeCard();
        }

        // put some cards in discard
        deck.putCard(deck.takeCard());
        deck.putCard(deck.takeCard());
        deck.putCard(deck.takeCard());
        deck.putCard(deck.takeCard());

        deck.takeCard();    // take 2 cards even though deck is empty
        deck.takeCard();

        assertEquals(3, deck.size());    // deck should reset with 4 cards, but 2 more were taken after*/
    }

    @Test
    public void testPutCard() {
        Card card1 = new Card(Card.Rank.EIGHT, Card.Colour.BLUE, Card.Rank.EIGHT, Card.Colour.PINK);
        Card card2 = new Card(Card.Rank.REVERSE, Card.Colour.RED, Card.Rank.REVERSE, Card.Colour.ORANGE);

        // take cards until only 3 left in deck
        for (int i = 0; i < (MAX_SIZE - 3); i++) {
            deck.takeCard();
        }

        // size should still be 3 after putting card, because putting card goes to discard
        deck.putCard(card1);
        assertEquals(3, deck.size());
        deck.putCard(card2);
        assertEquals(3, deck.size());
    }

    @Test
    public void testPutCardWithFullDeck() {
        Card card1 = new Card(Card.Rank.EIGHT, Card.Colour.BLUE, Card.Rank.EIGHT, Card.Colour.BLUE);

        // adding card to full deck should throw an exception
        assertThrows(IllegalStateException.class, () -> {
            deck.putCard(card1);
        });
        assertEquals(MAX_SIZE, deck.size());
    }

    @Test
    public void testPutCardWithCardAlreadyInDeck() {
        Card card1 = deck.takeCard();
        deck.putCard(card1);
        assertThrows(IllegalArgumentException.class, () -> {deck.putCard(card1);});
        assertEquals(MAX_SIZE - 1, deck.size());

        Card card2 = deck.takeCard();
        deck.putCard(card2);
        assertThrows(IllegalArgumentException.class, () -> {deck.putCard(card2);});
        assertEquals(MAX_SIZE - 2, deck.size());
    }

    @Test
    public void testPutCardWithNull() {
        assertThrows(NullPointerException.class, () -> {deck.putCard(null);});  // cannot put null in deck
        assertEquals(MAX_SIZE, deck.size());
    }
}
