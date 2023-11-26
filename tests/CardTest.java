import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class CardTest {

    private Card numberCard;
    private Card drawDrawOneOrFiveCard;
    private Card wildCard;
    private Card wildDrawTwoColourCard;


    @Before
    public void setUp() {
        numberCard = new Card(Card.Rank.FIVE, Card.Colour.GREEN, Card.Rank.FIVE, Card.Colour.TEAL);
        drawDrawOneOrFiveCard = new Card(Card.Rank.DRAW_ONE, Card.Colour.BLUE, Card.Rank.DRAW_FIVE, Card.Colour.PINK);
        wildCard = new Card(Card.Rank.WILD, Card.Colour.WILD, Card.Rank.WILD, Card.Colour.WILD_DARK);
        wildDrawTwoColourCard = new Card(Card.Rank.WILD_DRAW_2, Card.Colour.WILD, Card.Rank.WILD_DRAW_COLOUR, Card.Colour.WILD_DARK);
        if (Card.getSide() == Card.DARK) {
            Card.flipSide();    // always start a test on light side
        }
    }


    @Test
    public void testIsWild() {
        assertFalse(numberCard.isWild());
        assertFalse(drawDrawOneOrFiveCard.isWild());
        assertTrue(wildCard.isWild());
        assertTrue(wildDrawTwoColourCard.isWild());
        Card.flipSide();    // test dark side as well
        assertFalse(numberCard.isWild());
        assertFalse(drawDrawOneOrFiveCard.isWild());
        assertTrue(wildCard.isWild());
        assertTrue(wildDrawTwoColourCard.isWild());
    }

    @Test
    public void testGetColour() {
        assertEquals(Card.Colour.GREEN, numberCard.getColour());
        assertEquals(Card.Colour.BLUE, drawDrawOneOrFiveCard.getColour());
        assertEquals(Card.Colour.WILD, wildCard.getColour());
        assertEquals(Card.Colour.WILD, wildDrawTwoColourCard.getColour());
        Card.flipSide();    // test dark side as well
        assertEquals(Card.Colour.TEAL, numberCard.getColour());
        assertEquals(Card.Colour.PINK, drawDrawOneOrFiveCard.getColour());
        assertEquals(Card.Colour.WILD_DARK, wildCard.getColour());
        assertEquals(Card.Colour.WILD_DARK, wildDrawTwoColourCard.getColour());
    }

    @Test
    public void testGetRank() {
        assertEquals(Card.Rank.FIVE, numberCard.getRank());
        assertEquals(Card.Rank.DRAW_ONE, drawDrawOneOrFiveCard.getRank());
        assertEquals(Card.Rank.WILD, wildCard.getRank());
        assertEquals(Card.Rank.WILD_DRAW_2, wildDrawTwoColourCard.getRank());
        Card.flipSide();    // test dark side as well
        assertEquals(Card.Rank.FIVE, numberCard.getRank());
        assertEquals(Card.Rank.DRAW_FIVE, drawDrawOneOrFiveCard.getRank());
        assertEquals(Card.Rank.WILD, wildCard.getRank());
        assertEquals(Card.Rank.WILD_DRAW_COLOUR, wildDrawTwoColourCard.getRank());
    }

    @Test
    public void testToString() {
        Card flipCard = new Card(Card.Rank.FLIP, Card.Colour.GREEN, Card.Rank.FLIP, Card.Colour.TEAL);

        assertEquals("green_flip", flipCard.toString());
        assertEquals("green_5", numberCard.toString());
        assertEquals("blue_draw_1", drawDrawOneOrFiveCard.toString());
        assertEquals("wild_card", wildCard.toString());
        assertEquals("wild_draw_2", wildDrawTwoColourCard.toString());
        Card.flipSide();    // test dark side as well
        assertEquals("teal_flip", flipCard.toString());
        assertEquals("teal_5", numberCard.toString());
        assertEquals("pink_draw_5", drawDrawOneOrFiveCard.toString());
        assertEquals("wild_dark_card", wildCard.toString());
        assertEquals("wild_draw_colour", wildDrawTwoColourCard.toString());
    }

    @Test
    public void testEquals(){
        Card sameAsNumberCard =  new Card(Card.Rank.FIVE, Card.Colour.GREEN, Card.Rank.FIVE, Card.Colour.TEAL);
        Card sameAsDrawDrawOneOrFiveCard  = new Card(Card.Rank.DRAW_ONE, Card.Colour.BLUE, Card.Rank.DRAW_FIVE, Card.Colour.PINK);
        Card sameAsWildCard =  new Card(Card.Rank.WILD, Card.Colour.WILD, Card.Rank.WILD, Card.Colour.WILD_DARK);
        Card sameAsWildDrawTwoColourCard = new Card(Card.Rank.WILD_DRAW_2, Card.Colour.WILD, Card.Rank.WILD_DRAW_COLOUR, Card.Colour.WILD_DARK);

        assertEquals(sameAsNumberCard, numberCard);
        assertEquals(sameAsDrawDrawOneOrFiveCard, drawDrawOneOrFiveCard);
        assertEquals(sameAsWildCard, wildCard);
        assertEquals(sameAsWildDrawTwoColourCard, wildDrawTwoColourCard);
        assertNotEquals(sameAsDrawDrawOneOrFiveCard, numberCard);
        assertNotEquals(sameAsWildCard, wildDrawTwoColourCard);
        Card.flipSide();    // test with dark side as well
        assertEquals(sameAsNumberCard, numberCard);
        assertEquals(sameAsDrawDrawOneOrFiveCard, drawDrawOneOrFiveCard);
        assertEquals(sameAsWildCard, wildCard);
        assertEquals(sameAsWildDrawTwoColourCard, wildDrawTwoColourCard);
    }
}