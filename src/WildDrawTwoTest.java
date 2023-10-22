import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class WildDrawTwoTest {
    private Game game;
    private Card testCard;

    @Before
    public void setUp(){
        game = new Game();
        game.getPlayers().add(new Player("test1"));
        game.getPlayers().add(new Player("test2"));
        game.setNumPlayers(2);
        testCard = new Card(Card.Rank.ONE, Card.Colour.RED);
    }

    @Test
    public void testExecuteSequence(){
        game.getTurnSeqs().get(13).executeSequence(testCard);
        assertTrue(game.getTurnDirection());
        assertEquals(testCard, game.getTopCard());
        assertEquals(Card.Colour.RED, game.getCurrentColour());
        assertEquals(Card.Rank.ONE, game.getCurrentRank());
        assertEquals(0, game.getCurrentTurn());
        assertEquals(1, game.getNextTurn());
        assertEquals(9, game.getPlayers().get(1).getHandSize());

    }

}

