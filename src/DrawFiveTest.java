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

        testCard = new Card(Card.Rank.DRAW_FIVE, Card.Colour.TEAL, false); //false since card is on darkside
        testTopCard = newCard(Card.RANK.FLIP, Card.Colour.GREEN, true);

        unoFlipModel.setTopCard(testTopCard);
    }

    @Test
    public void testExecuteSequence(){
        unoFlipModel.getTurnSeqs().get(15).executeSequence(testCard);
        assertTrue(unoFlipModel.getTurnDirection());
        assertEquals(1, unoFlipModel.getCurrentTurn());
        assertEquals(0, unoFlipModel.getNextTurn());
        assertEquals(12, unoFlipModel.getPlayers().get(0).getHandSize());
    }
}
