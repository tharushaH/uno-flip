import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class SelfDrawOneTest {
    private SelfDrawOne selfDrawOne;
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
        game.getTurnSeqs().get(14).executeSequence(testCard);
        assertTrue(game.getTurnDirection());
        assertEquals(1, game.getCurrentTurn());
        assertEquals(0, game.getNextPlayerIndex());
        assertEquals(8, game.getPlayers().get(0).getHandSize());

    }

}
