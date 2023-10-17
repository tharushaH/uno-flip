import junit.framework.TestCase;
import org.junit.*;

import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

public class PlayerTest extends junit.framework.TestCase{

    private Player p1;
    private Player p2;
    private Card c1;
    private Card c2;


    /**
     *  Sets up the Player objects.
     *  Called before every test case method.
     */
    @Before
    public void setUp(){
        p1 = new Player("Kramer");
        //c1 = new Card(Card.Rank.TWO, Card.Colour.BLUE);
        //c2 = new Card(Card.Rank.FIVE, Card.Colour.RED);

    }

    /**
     * Test used to verify the player objects are created successfully.
     */
    @Test
    public void testConstructor(){
        //Make sure the name given matches the one in the object
        assertEquals("Kramer", p1.getName());

        //make sure that each player is initialized with the right amount of cards
        assertEquals(7,p1.getHandSize());
    }

    @Test
    public void testAddCardToHand(){
        //Test to check make sure the player starts off with 7 cards
        assertEquals(7,p1.getHandSize());

        //Add the Card to the players hand.
        p1.addCardToHand(1);

        //Make sure that there a card in the hand
        assertEquals(8,p1.getHandSize());

    }

    @Test
    public void testPlayCard(){
        //Test to check make sure the player starts off with 7 cards
        assertEquals(7,p1.getHandSize());

        //Play a card
        Card playedCard = p1.playCard(0);

        //Test to see that there are 6 cards now in hand
        assertEquals(6, p1.getHandSize());
    }

    @Test
    public void testGetHandSize(){

        //Test to check make sure the player starts off with 7 cards
        assertEquals(7,p1.getHandSize());

        //Play a card
        Card playedCard = p1.playCard(0);

        //Test to see that there are 6 cards now in hand
        assertEquals(6, p1.getHandSize());

    }
    @Test
    public void testGetName(){
        //test to see that the name given to the player matches the name that is initialized
        assertEquals("Kramer", p1.getName());

    }

    @Test
    public void testPrintHand(){


    }



    @Test
    public void testToString(){

    }









}
