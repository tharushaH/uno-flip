import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;
public class SkipEveryoneTest {
    private UnoFlipModel unoFlipModel;
    private Card testCard;

    @Before
    public void setUp(){
        unoFlipModel = new UnoFlipModel();
        unoFlipModel.getPlayers().add(new Player("test1"));
        unoFlipModel.getPlayers().add(new Player("test2"));
        unoFlipModel.getPlayers().add(new Player("test3"));
        unoFlipModel.setNumPlayers(3);
        testCard = new Card(Card.Rank.ONE, Card.Colour.RED,Card.Rank.ONE, Card.Colour.ORANGE);
    }

    @Test
    public void testExecuteSequence(){
        unoFlipModel.getTurnSeqs().get(15).executeSequence(testCard);
        assertTrue(unoFlipModel.getTurnDirection());
        assertEquals(testCard, unoFlipModel.getTopCard());
        assertEquals(Card.Colour.RED, unoFlipModel.getCurrentColour());
        assertEquals(Card.Rank.ONE, unoFlipModel.getCurrentRank());
        assertEquals(0,unoFlipModel.getCurrentTurn());
        assertTrue(unoFlipModel.getSkipEveryoneFlag());

    }

    @Test
    public void testExecuteSequenceAtLastTurn(){

        unoFlipModel.setTurnFinished(true);
        unoFlipModel.nextTurn();
        unoFlipModel.setTurnFinished(true);
        unoFlipModel.nextTurn();

        unoFlipModel.getTurnSeqs().get(15).executeSequence(testCard);
        assertTrue(unoFlipModel.getTurnDirection());
        assertEquals(testCard, unoFlipModel.getTopCard());
        assertEquals(Card.Colour.RED, unoFlipModel.getCurrentColour());
        assertEquals(Card.Rank.ONE, unoFlipModel.getCurrentRank());
        assertEquals(2,unoFlipModel.getCurrentTurn());
        assertEquals(0,unoFlipModel.getNextTurn());
        assertTrue(unoFlipModel.getSkipEveryoneFlag());
    }

    @Test
    public void testExecuteSequenceAtMiddle(){

        unoFlipModel.setTurnFinished(true);
        unoFlipModel.nextTurn();

        unoFlipModel.getTurnSeqs().get(15).executeSequence(testCard);
        assertTrue(unoFlipModel.getTurnDirection());
        assertEquals(testCard, unoFlipModel.getTopCard());
        assertEquals(Card.Colour.RED, unoFlipModel.getCurrentColour());
        assertEquals(Card.Rank.ONE, unoFlipModel.getCurrentRank());
        assertEquals(1,unoFlipModel.getCurrentTurn());
        assertEquals(2,unoFlipModel.getNextTurn());
        assertTrue(unoFlipModel.getSkipEveryoneFlag());
    }

}
