import junit.framework.TestCase;
import org.junit.*;

import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

public class PlayerTest extends junit.framework.TestCase{

    private Player p1;
    private Player p2;
    private Card c1;


    /**
     *  Sets up the Player objects.
     *  Called before every test case method.
     */
    @Before
    public void setUp(){
        p1 = new Player("Jerry");
        p2 = new Player("Kramer");

        c1 = new Card(Card.Rank.TWO, Card.Colour.BLUE);

    }

    /**
     * Test used to verify the player objects are created successfully.
     */
    @Test
    public void testConstructor(){
        //Make sure the name given matches the one in the object
        assertEquals("Jerry", p1.getName());
    }

    @Test
    public void testAddCardToHand(){
        //Test to make sure that the hand should be empty
        assertTrue(p1.hasZeroCards());

        //Add the Card to the players hand.
        p1.addCardToHand(1, c1);

        // Test to make sure the player has 1 card in hand
        /*********assertEquals(1,p1.hand);
         *
         */
    }

    @Test
    public void testPlayCard(){

    }

    @Test
    public void testHasZeroCards(){
    }

    @Test
    public void testPrintHand(){

    }

    @Test
    public void testToString(){

    }

    @Test
    public void testGetName(){

    }







}
