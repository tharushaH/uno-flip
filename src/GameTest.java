import org.junit.*;
import static org.junit.Assert.*;
public class GameTest {
    private Game game;
    private Player player;

    @Before
    public void setUp(){
        game = new Game();
        player = new Player("Bobby");
       // Deck deck = game.getDeck();


    }
    @Test
    public void testPlayGame(){

    }
    /**
    @Test
    public void testEmptyGame(){
        assertTrue(game.getPlayers().size() == 0);
        assertTrue(game.getTurnDirection());
        assertTrue(game.getCurrentTurn() == 0);
        //assertFalse(game.getDeck().empty());
        assertTrue(game.getCurrentColour() == null);
        assertTrue(game.getCurrentRank() == null);
    }
    */





}
