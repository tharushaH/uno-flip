import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class SelfDrawOneTest {
    private UnoFlipModel unoFlipModel;
    private Card testCard;

    @Before
    public void setUp(){
        unoFlipModel = new UnoFlipModel();
        unoFlipModel.getPlayers().add(new Player("test1"));
        unoFlipModel.getPlayers().add(new Player("test2"));
        unoFlipModel.setNumPlayers(2);
        testCard = new Card(Card.Rank.ONE, Card.Colour.RED, Card.Rank.ONE, Card.Colour.RED);
    }

    @Test
    public void testExecuteSequence(){
        unoFlipModel.getTurnSeqs().get(unoFlipModel.TURN_SEQ_SELF_DRAW_ONE).executeSequence(testCard);
        assertTrue(unoFlipModel.getTurnDirection());
        assertEquals(0, unoFlipModel.getCurrentTurn());
        assertEquals(1, unoFlipModel.getNextTurn());
        assertEquals(8, unoFlipModel.getPlayers().get(0).getHandSize());

    }

}
