import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class WildTest {
    private UnoFlipModel unoFlipModel;
    private Card testCard;

    @Before
    public void setUp(){
        unoFlipModel = new UnoFlipModel();
        unoFlipModel.getPlayers().add(new Player("test1"));
        unoFlipModel.getPlayers().add(new Player("test2"));
        unoFlipModel.setNumPlayers(2);
        testCard = new Card(Card.Rank.WILD, Card.Colour.WILD, Card.Rank.ONE, Card.Colour.ORANGE);
    }

    @Test
    public void testExecuteSequence(){
        unoFlipModel.getTurnSeqs().get(12).executeSequence(testCard);
        assertTrue(unoFlipModel.getTurnDirection());
        assertEquals(testCard, unoFlipModel.getTopCard());
        assertEquals(Card.Colour.WILD, unoFlipModel.getCurrentColour());
        assertEquals(Card.Rank.WILD, unoFlipModel.getCurrentRank());
        assertEquals(0, unoFlipModel.getCurrentTurn());
        assertEquals(1, unoFlipModel.getNextTurn());
    }
    @After
    public void teardown(){
        unoFlipModel=null;
        testCard=null;

    }

}

