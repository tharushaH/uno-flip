import org.junit.*;


import static org.junit.Assert.*;
public class UnoFlipModelTest {
    private UnoFlipModel unoFlipModel;


    @Before
    public void setUp(){
        unoFlipModel = new UnoFlipModel();


    }
    @Test
    public void testSetNumPlayersValid() {
        int numPlayers = 3;
        unoFlipModel.setNumPlayers(numPlayers);
        assertEquals(numPlayers, unoFlipModel.getNumPlayers());
    }
    @Test
    public void testSetNumPlayersInvalid() {
        int invalidNumPlayers = 5;
        assertEquals(0, unoFlipModel.getNumPlayers());
    }
    @Test
    public void testSetUpInitialTopCard(){
        unoFlipModel.addPlayer(new Player("Bobby"));
        unoFlipModel.addPlayer(new Player("Mark"));
        unoFlipModel.setNumPlayers(2);
        assertEquals(2,unoFlipModel.getNumPlayers());
        unoFlipModel.setUpInitialTopCard();
        assertFalse(unoFlipModel.getTopCard().getRank().ordinal()==13);
    }
    @Test
    public void testPlayTurnSelfDrawOne(){
        Player p = new Player("Bobby");
        Player p1 = new Player("Mark");
        unoFlipModel.setNumPlayers(2);
        assertEquals(2,unoFlipModel.getNumPlayers());
        unoFlipModel.addPlayer(p);
        unoFlipModel.addPlayer(p1);
        unoFlipModel.setUpInitialTopCard();
        unoFlipModel.playTurn(-1);
        assertEquals(-1, unoFlipModel.getChosenCardIndex());
        assertNotNull(unoFlipModel.getTopCard());

    }

    @Test
    public void testEmptyGame(){
        assertTrue(unoFlipModel.getPlayers().size()==0);
        assertTrue(unoFlipModel.getTurnDirection());
        assertTrue(unoFlipModel.getCurrentTurn() == 0);
        assertTrue(unoFlipModel.getNextTurn()==1);
        assertTrue(unoFlipModel.getCurrentColour() == null);
        assertTrue(unoFlipModel.getCurrentRank() == null);
        assertTrue(unoFlipModel.getNumPlayers() == 0);
        assertTrue(unoFlipModel.getChosenCardIndex() == -2);
    }
    @Test
    public void testGetCurrentColour(){
        assertTrue(unoFlipModel.getCurrentColour()==null);
    }
    @Test
    public void testGetCurrentRank(){
        assertTrue(unoFlipModel.getCurrentRank()==null);
    }
    @Test
    public void testSetCurrentRank(){
        unoFlipModel.setCurrentRank(Card.Rank.ONE);
        assertTrue(unoFlipModel.getCurrentRank()== Card.Rank.ONE);
    }
    @Test
    public void testGetCurrentTurn(){
        assertTrue(unoFlipModel.getCurrentTurn()==0);
    }
    @Test
    public void testSetTopCard(){
        unoFlipModel.setTopCard(new Card(Card.Rank.FIVE, Card.Colour.RED, Card.Rank.FIVE, Card.Colour.ORANGE));
        assertTrue(unoFlipModel.getTopCard().getRank()== Card.Rank.FIVE);
        assertTrue(unoFlipModel.getTopCard().getColour()== Card.Colour.RED);
    }
    @Test
    public void testGetCurrentPlayer(){
        Player player = new Player("Bobby");
        unoFlipModel.addPlayer(player);
        assertTrue(unoFlipModel.getCurrentPlayer().equals(player));
    }
    @Test
    public void testFlipTurnDirection(){
        assertTrue(unoFlipModel.getTurnDirection());
        unoFlipModel.flipTurnDirection();
        assertFalse(unoFlipModel.getTurnDirection());
    }
    @Test
    public void testGetNextTurn(){
        assertTrue(unoFlipModel.getNextTurn()==1);
    }
    @Test
    public void testNextTurn(){
        unoFlipModel.addPlayer(new Player("Bobby"));
        unoFlipModel.addPlayer(new Player("Mark"));
        unoFlipModel.setNumPlayers(2);
        assertTrue(unoFlipModel.getCurrentTurn()==0);
        assertTrue(unoFlipModel.getNextTurn()==1);
        unoFlipModel.setTurnFinished(true);
        unoFlipModel.nextTurn();
        assertEquals(0, unoFlipModel.getNextTurn());

    }
    @Test
    public void testAddPlayer(){
        unoFlipModel.addPlayer(new Player("Bobby"));
        assertTrue(unoFlipModel.getPlayers().size()==1);
    }

    @Test
    public void testSetCurrentColour(){
        unoFlipModel.setCurrentColour(Card.Colour.BLUE);
        assertTrue(unoFlipModel.getCurrentColour()== Card.Colour.BLUE);
    }
    @Test
    public void testSkipTurn(){
        unoFlipModel.addPlayer(new Player("Bobby"));
        unoFlipModel.addPlayer(new Player("Mark"));
        unoFlipModel.setNumPlayers(2);
        assertTrue(unoFlipModel.getTurnDirection());
        assertTrue(unoFlipModel.getCurrentTurn()==0);
        unoFlipModel.setSkipTurnFlag();
        assertTrue(unoFlipModel.getCurrentTurn()==0);
    }
    @Test
    public void testDrawNCards(){
        unoFlipModel.addPlayer(new Player("Bobby"));
        unoFlipModel.addPlayer(new Player("Mark"));
        unoFlipModel.setNumPlayers(2);
        unoFlipModel.drawNCards(2, unoFlipModel.getCurrentTurn());
        assertTrue(unoFlipModel.getCurrentPlayer().getHandSize()==9);
    }
    @After
    public void teardown(){
        unoFlipModel=null;


    }

    @Test
    public void testXML(){


        unoFlipModel.setUpInitialTopCard();
        unoFlipModel.addPlayer(new Player("Bobby"));
        unoFlipModel.addPlayer(new Player("Mark"));
        //System.out.println(unoFlipModel.modelPlayersToXML());
        //System.out.println(unoFlipModel.modelDataToXML());
        unoFlipModel.exportToXMLFile(UnoFlipModel.SAVE_FILE_PREFIX + UnoFlipModel.CURRENT_STATE_MODEL_DATA, UnoFlipModel.XML_MODEL_DATA_FLAG);
        unoFlipModel.exportToXMLFile(UnoFlipModel.SAVE_FILE_PREFIX + UnoFlipModel.CURRENT_STATE_MODEL_DECK, UnoFlipModel.XML_MODEL_DECK_FLAG);
        unoFlipModel.exportToXMLFile(UnoFlipModel.SAVE_FILE_PREFIX + UnoFlipModel.CURRENT_STATE_MODEL_PLAYERS, UnoFlipModel.XML_MODEL_PLAYERS_FLAG);


    }



}
