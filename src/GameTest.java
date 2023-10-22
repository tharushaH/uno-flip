import org.junit.*;


import static org.junit.Assert.*;
public class GameTest {
    private Game game;


    @Before
    public void setUp(){
        game = new Game();


    }

    @Test
    public void testEmptyGame(){
        assertTrue(game.getPlayers().size()==0);
        assertTrue(game.getTurnDirection());
        assertTrue(game.getCurrentTurn() == 0);
        assertTrue(game.getNextTurn()==1);
        assertTrue(game.getCurrentColour() == null);
        assertTrue(game.getCurrentRank() == null);
        assertTrue(game.getNumPlayers() == 0);
        assertTrue(game.getChosenCardIndex() == -1);
    }
    @Test
    public void testGetCurrentColour(){
        assertTrue(game.getCurrentColour()==null);
    }
    @Test
    public void testGetCurrentRank(){
        assertTrue(game.getCurrentRank()==null);
    }
    @Test
    public void testSetCurrentRank(){
        game.setCurrentRank(Card.Rank.ONE);
        assertTrue(game.getCurrentRank()== Card.Rank.ONE);
    }
    @Test
    public void testGetCurrentTurn(){
        assertTrue(game.getCurrentTurn()==0);
    }
    @Test
    public void testSetTopCard(){
        game.setTopCard(new Card(Card.Rank.FIVE, Card.Colour.RED));
        assertTrue(game.getTopCard().getRank()== Card.Rank.FIVE);
        assertTrue(game.getTopCard().getColour()== Card.Colour.RED);
    }
    @Test
    public void testGetCurrentPlayer(){
        Player player = new Player("Bobby");
        game.addPlayer(player);
        assertTrue(game.getCurrentPlayer().equals(player));
    }
    @Test
    public void testFlipTurnDirection(){
        assertTrue(game.getTurnDirection());
        game.flipTurnDirection();
        assertFalse(game.getTurnDirection());
    }
    @Test
    public void testGetNextTurn(){
        assertTrue(game.getNextTurn()==1);
    }
    @Test
    public void testNextTurn(){
        game.addPlayer(new Player("Bobby"));
        game.addPlayer(new Player("Mark"));
        game.setNumPlayers(2);
        assertTrue(game.getCurrentTurn()==0);
        assertTrue(game.getNextTurn()==1);
        game.nextTurn();
        assertTrue(game.getNextTurn()==0);

    }
    @Test
    public void testAddPlayer(){
        game.addPlayer(new Player("Bobby"));
        assertTrue(game.getPlayers().size()==1);
    }

    @Test
    public void testSetCurrentColour(){
        game.setCurrentColour(Card.Colour.BLUE);
        assertTrue(game.getCurrentColour()== Card.Colour.BLUE);
    }
    @Test
    public void testSkipTurn(){
        game.addPlayer(new Player("Bobby"));
        game.addPlayer(new Player("Mark"));
        game.setNumPlayers(2);
        assertTrue(game.getTurnDirection());
        assertTrue(game.getCurrentTurn()==0);
        game.skipTurn();
        assertTrue(game.getCurrentTurn()==0);
    }
    @Test
    public void testDrawNCards(){
        game.addPlayer(new Player("Bobby"));
        game.addPlayer(new Player("Mark"));
        game.setNumPlayers(2);
        game.drawNCards(2,game.getCurrentTurn());
        assertTrue(game.getCurrentPlayer().getHandSize()==9);
    }




}
