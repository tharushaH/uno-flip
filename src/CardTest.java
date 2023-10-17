import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.junit.Assert.*;

public class CardTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();
    private Card regularCard;
    private Card drawOneCard;
    private Card wildCard;
    private Card wildDrawTwoCard;

    @Before
    public void setUp() {
        regularCard = new Card(Card.Rank.FIVE, Card.Colour.GREEN);
        drawOneCard = new Card(Card.Rank.DRAW_ONE, Card.Colour.BLUE);
        wildCard = new Card(Card.Rank.WILD, Card.Colour.WILD);
        wildDrawTwoCard = new Card(Card.Rank.WILD_DRAW_2, Card.Colour.WILD);
    }

    @Test
    public void createWrongCards(){
        assertThrows(IllegalArgumentException.class,() -> new Card(Card.Rank.WILD, Card.Colour.RED));
        assertThrows(IllegalArgumentException.class,() -> new Card(Card.Rank.WILD_DRAW_2, Card.Colour.RED));
        assertThrows(IllegalArgumentException.class,() -> new Card(Card.Rank.REVERSE, Card.Colour.WILD));
        assertThrows(IllegalArgumentException.class,() -> new Card(Card.Rank.SKIP, Card.Colour.WILD));
        assertThrows(IllegalArgumentException.class,() -> new Card(Card.Rank.DRAW_ONE, Card.Colour.WILD));
        assertThrows(IllegalArgumentException.class,() -> new Card(Card.Rank.ONE, Card.Colour.WILD));
    }

    @Test
    public void testIsWild() {
        assertFalse(regularCard.isWild());
        assertFalse(drawOneCard.isWild());
        assertTrue(wildCard.isWild());
        assertTrue(wildDrawTwoCard.isWild());

    }

    @Test
    public void testGetColour() {
        assertEquals(Card.Colour.GREEN, regularCard.getColour());
        assertEquals(Card.Colour.BLUE, drawOneCard.getColour());
        assertEquals(Card.Colour.WILD, wildCard.getColour());
        assertEquals(Card.Colour.WILD, wildDrawTwoCard.getColour());
    }

    @Test
    public void testGetRank() {
        assertEquals(Card.Rank.FIVE, regularCard.getRank());
        assertEquals(Card.Rank.DRAW_ONE, drawOneCard.getRank());
        assertEquals(Card.Rank.WILD, wildCard.getRank());
        assertEquals(Card.Rank.WILD_DRAW_2, wildDrawTwoCard.getRank());
    }

    @Test
    public void testToString() {
        assertEquals("GREEN FIVE", regularCard.toString());
        assertEquals("BLUE DRAW 1", drawOneCard.toString());
        assertEquals("WILD CARD", wildCard.toString());
        assertEquals("WILD DRAW 2", wildDrawTwoCard.toString());
    }
}
