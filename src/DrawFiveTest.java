import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;
public class DrawFiveTest {
    private UnoFlipModel unoFlipModel;

    private Card testCard;

    @Before
    public void setup(){
        unoFlipModel = new UnoFlipModel();
        unoFlipModel.getPlayers().add(new Player("test1"));
        unoFlipModel.getPlayers().add(new Player("test2"));
        unoFlipModel.setNumPlayers(2);
        testCard = new Card(Card.Rank.DRAW_ONE, Card.Colour.RED,Card.Rank.DRAW_FIVE, Card.Colour.ORANGE); //false since card is on darkside
    }

    @Test
    public void testExecuteSequence(){
        unoFlipModel.getTurnSeqs().get(15).executeSequence(testCard);
        assertTrue(unoFlipModel.getTurnDirection());
        assertEquals(0, unoFlipModel.getCurrentTurn());
        assertEquals(1, unoFlipModel.getNextTurn());
        assertEquals(12, unoFlipModel.getPlayers().get(1).getHandSize());
    }
}
