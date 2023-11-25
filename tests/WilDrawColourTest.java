import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class WilDrawColourTest {
    private UnoFlipModel unoFlipModel;
    private Card testCard;
    private static final int WILD_DRAW_COLOUR_INDEX = 16;

    @Before
    public void setUp(){
        unoFlipModel = new UnoFlipModel();
        unoFlipModel.getPlayers().add(new Player("test1"));
        unoFlipModel.getPlayers().add(new Player("test2"));
        unoFlipModel.setNumPlayers(2);
        unoFlipModel.flipCardSide();    // need to flip the side to access wild draw colour card (dark side card)
        testCard = new Card(Card.Rank.WILD_DRAW_2, Card.Colour.WILD, Card.Rank.WILD_DRAW_COLOUR, Card.Colour.WILD_DARK);
    }

    @Test
    public void testExecuteSequence(){
        unoFlipModel.getTurnSeqs().get(WILD_DRAW_COLOUR_INDEX).executeSequence(testCard);
        assertTrue(unoFlipModel.getTurnDirection());    // turnDirection = true means clockwise
        assertEquals(testCard, unoFlipModel.getTopCard());
        assertEquals(Card.Colour.WILD_DARK, unoFlipModel.getCurrentColour());
        assertEquals(Card.Rank.WILD_DRAW_COLOUR, unoFlipModel.getCurrentRank());
        assertEquals(0, unoFlipModel.getCurrentTurn()); // still first player's turn
        assertEquals(1, unoFlipModel.getNextTurn());    // next player according to turn direction
        assertTrue(unoFlipModel.getSkipTurnFlag()); // next player's turn should be skipped
        assertEquals(UnoFlipModel.STATUS_CHALLENGE_MESSAGE, unoFlipModel.getStatus());  // wild draw colour is challengable
    }
}
