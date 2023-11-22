import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class FlipTest {
    private UnoFlipModel unoFlipModel;
    private Card testCard;






    @Before
    public void setUp(){
        unoFlipModel = new UnoFlipModel();
        unoFlipModel.getPlayers().add(new Player("test1"));
        unoFlipModel.getPlayers().add(new Player("test2"));
        unoFlipModel.setNumPlayers(2);
        testCard = new Card(Card.Rank.FLIP, Card.Colour.RED,Card.Rank.FLIP,Card.Colour.ORANGE);
    }

    @Test
    public void testExecuteSequence(){
        unoFlipModel.getTurnSeqs().get(UnoFlipModel.TURN_SEQ_FLIP).executeSequence(testCard); // should be index 18 according to enum
        // don't have the other turnSeqs added yet to model so have index as 15 instead of 18
        assertTrue(unoFlipModel.getTurnDirection());
        assertEquals(testCard, unoFlipModel.getTopCard()); // make sure the testCard is on top
        assertEquals(Card.DARK,Card.getSide()); // make sure side was flipped in card class
        assertEquals(Card.Colour.ORANGE, unoFlipModel.getCurrentColour()); // check to see if card was flipped
        assertEquals(Card.Rank.FLIP, unoFlipModel.getCurrentRank());
        assertEquals(0, unoFlipModel.getCurrentTurn()); //make sure nothing else was touched
        assertEquals(1, unoFlipModel.getNextTurn());

    }

}


