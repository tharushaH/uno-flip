import org.junit.*;
import static org.junit.Assert.*;

public class DeckTest {
    private Deck deck;
    final static int MAX_SIZE = 104;    // should be 112, but not including the 8 flip cards yet

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
        for (int i = 0; i < MAX_SIZE; i++) {
            deck.takeCard();
        }

        // put some cards in discard
        deck.putCard(new Card(Card.Rank.REVERSE, Card.Colour.RED));
        deck.putCard(new Card(Card.Rank.THREE, Card.Colour.BLUE));
        deck.putCard(new Card(Card.Rank.THREE, Card.Colour.BLUE));
        deck.putCard(new Card(Card.Rank.WILD, Card.Colour.WILD));

        deck.takeCard();    // take 2 cards even though deck is empty
        deck.takeCard();

        assertEquals(2, deck.size());    // deck should reset with 4 cards, but 2 more were taken after*/
    }

    @Test
    public void testPutCard() {
        Card card1 = new Card(Card.Rank.EIGHT, Card.Colour.BLUE);
        Card card2 = new Card(Card.Rank.REVERSE, Card.Colour.RED);

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
        Card card1 = new Card(Card.Rank.EIGHT, Card.Colour.BLUE);

        // adding card to full deck should throw an exception
        assertThrows(IllegalStateException.class, () -> {
            deck.putCard(card1);
        });
        assertEquals(MAX_SIZE, deck.size());
    }

    @Test
    public void testPutCardWithCardAlreadyInDeck() {
        Card card1 = new Card(Card.Rank.EIGHT, Card.Colour.BLUE);
        Card card2 = new Card(Card.Rank.REVERSE, Card.Colour.RED);

        // a card cannot be added if all the cards of the same colour and rank are already in the deck
        for (int i = 0; i < (MAX_SIZE - 3); i++) {
            deck.takeCard();    // empty out the deck
        }
        deck.putCard(card1);
        deck.putCard(card1);    // only 2 blue/eight cards, both are in deck now
        assertThrows(IllegalArgumentException.class, () -> {
            deck.putCard(card2);
        });
        assertEquals(2, deck.size());

        deck.putCard(card2);
        deck.putCard(card2);    // only 2 yellow/reverse cards, both are in deck now
        assertThrows(IllegalArgumentException.class, () -> {
            deck.putCard(card2);
        });
        assertEquals(4, deck.size());
    }

    @Test
    public void testPutCardWithNull() {
        assertThrows(NullPointerException.class, () -> {
            deck.putCard(null);
        });  // cannot put null in deck
        assertEquals(MAX_SIZE, deck.size());
    }
}
