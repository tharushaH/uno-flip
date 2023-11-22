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
        unoFlipModel.setTopCard(testCard);
        unoFlipModel.setCurrentColour(testCard.getColour());
        assertTrue(unoFlipModel.getTurnDirection());
        assertEquals(testCard, unoFlipModel.getTopCard());
        Card.flipSide();
        unoFlipModel.setCurrentColour(testCard.getColour());
        unoFlipModel.setCurrentRank(testCard.getRank());
        assertEquals(Card.Colour.ORANGE, unoFlipModel.getCurrentColour());
        assertEquals(Card.Rank.FLIP, unoFlipModel.getCurrentRank());
        assertEquals(0, unoFlipModel.getCurrentTurn());
        assertEquals(1, unoFlipModel.getNextTurn());

    }

}


