import junit.framework.TestCase;
import org.junit.*;

import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

public class PlayerTest extends junit.framework.TestCase{

    private Player p1;

    /**
     *  Sets up the Player objects.
     *  Called before every test case method.
     */
    @Before
    public void setUp(){
        p1 = new Player("Kramer");



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

        /*
             POSSIBLE TEST
             - REMOVE ALL CARDS FROM HAND
             - ADD ONE CARD TO DECK
             - CHECK HANDSIZE

             *** POSSIBLE CONFLICT SINCE WHEN USING PLAYCARD(),
             IT MIGHT ONLY ALLOW SPECIFIC CARDS TO BE PLACES AND NOT ALL CARDS IN HAND
         */

    }

    @Test
    public void testPlayCard(){

        /*
                TESTING THE FIRST CARD THE PLAYER PLAYS
         */

        //Test to check make sure the player starts off with 7 cards
        assertEquals(7,p1.getHandSize());

        //Play a card
        Card playedCard = p1.playCard(0);

        //Test to see that there are 6 cards now in hand
        assertEquals(6, p1.getHandSize());


        //Play each of the cards in the hand, and check the number of cards in the hand
        for(int i=0; i < 6; i++){
            p1.playCard(0);
            assertEquals(i%6, p1.getHandSize());
        }

    }

    @Test
    public void testGetHandSize(){

        //Test to check make sure the player starts off with 7 cards
        assertEquals(7,p1.getHandSize());

        //Play a card
        Card playedCard = p1.playCard(0);

        //Play each of the cards in the hand, and check the number of cards in the hand
        for(int i=0; i < 6; i++){
            p1.playCard(0);
            assertEquals(i%6, p1.getHandSize());
        }

    }
    @Test
    public void testGetName(){
        //test to see that the name given to the player matches the name that is initialized
        assertEquals("Kramer", p1.getName());

    }


    @Test
    public void testToString(){

        // Play all cards, there should not be nay cards left in the player's hand
        for(int i=0; i<7; i++){
            p1.playCard(0);
        }

        assertEquals("YourCards:", p1.toString());
    }


    @Test
    public void testColourInHand(){

        // Play all cards, there should not be nay cards left in the player's hand
        for(int i=0; i<7; i++){
            p1.playCard(0);
        }

        assertFalse(p1.colourInHand(Card.Colour.RED));



    }

    @Test
    public void testGetCard(){
        assertEquals(p1.playCard(0), p1.getCard(0));
        assertEquals(p1.playCard(0), p1.getCard(0));
        assertEquals(p1.playCard(0), p1.getCard(0));
        assertEquals(p1.playCard(0), p1.getCard(0));
        assertEquals(p1.playCard(0), p1.getCard(0));
    }






}
