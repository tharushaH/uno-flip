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
        testCard = new Card(Card.Rank.FLIP, Card.Colour.RED);
    }

    @Test
    public void testExecuteSequence(){
        unoFlipModel.getTurnSeqs().get(12).executeSequence(testCard);
        assertTrue(unoFlipModel.getTurnDirection());
        assertEquals(testCard, unoFlipModel.getTopCard());
        assertEquals(Card.Colour.PINK, unoFlipModel.getCurrentColour());
        assertEquals(Card.Rank.ONE, unoFlipModel.getCurrentRank());
        assertEquals(0, unoFlipModel.getCurrentTurn());
        assertEquals(1, unoFlipModel.getNextTurn());

    }

}


