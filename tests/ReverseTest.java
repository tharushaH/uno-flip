import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class ReverseTest {
    private UnoFlipModel unoFlipModel;
    private Card testCard;

    @Before
    public void setUp(){
        unoFlipModel = new UnoFlipModel();
        unoFlipModel.getPlayers().add(new Player("test1"));
        unoFlipModel.getPlayers().add(new Player("test2"));
        unoFlipModel.setNumPlayers(2);
        testCard = new Card(Card.Rank.ONE, Card.Colour.RED, Card.Rank.ONE, Card.Colour.ORANGE);
    }

    @Test
    public void testExecuteSequence(){
        unoFlipModel.getTurnSeqs().get(10).executeSequence(testCard);
        assertFalse(unoFlipModel.getTurnDirection());
        assertEquals(testCard, unoFlipModel.getTopCard());
        assertEquals(Card.Colour.RED, unoFlipModel.getCurrentColour());
        assertEquals(Card.Rank.ONE, unoFlipModel.getCurrentRank());
        assertEquals(0, unoFlipModel.getCurrentTurn());
        assertEquals(1, unoFlipModel.getNextTurn());
        assertEquals(7, unoFlipModel.getPlayers().get(1).getHandSize());

    }

}

