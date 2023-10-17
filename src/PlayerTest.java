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

        c1 = new Card(Card.Rank.TWO, Card.Colour.BLUE);
        c2 = new Card(Card.Rank.FIVE, Card.Colour.RED);

    }

    /**
     * Test used to verify the player objects are created successfully.
     */
    @Test
    public void testConstructor(){
        //Make sure the name given matches the one in the object
        assertEquals("Kramer", p1.getName());
    }

    @Test
    public void testAddCardToHand(){
        //Test to make sure that the hand should be empty
        assertTrue(p1.hasZeroCards());

        //Add the Card to the players hand.
        p1.addCardToHand(1);

        //Make sure that there a card in the hand
        assertFalse(p1.hasZeroCards());

    }

    @Test
    public void testPlayCard(){

        //Test to make sure that the hand should be empty
        assertTrue(p1.hasZeroCards());

        //add 1 cards to the hand
        p1.addCardToHand(1);

        //Play a card
        Card playedCard = p1.playCard(0);

        //Test to see player has no cards
        assertTrue(p1.hasZeroCards());
    }

    @Test
    public void testHasZeroCards(){

        //Test to make sure that the hand should be empty
        assertTrue(p1.hasZeroCards());

        //add 1 cards to the hand
        p1.addCardToHand(1);

        //Play a card
        Card playedCard = p1.playCard(0);

        /*****/
        //Test to see player has no cards again
        assertTrue(p1.hasZeroCards());

        //add 1 cards to the hand
        p1.addCardToHand(2);

        //Test to see player has no cards again
        assertFalse(p1.hasZeroCards());

    }


    @Test
    public void testToString(){

    }

    @Test
    public void testGetName(){

    }







}
