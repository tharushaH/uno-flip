import junit.framework.TestCase;
import org.junit.*;

import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

public class PlayerTest {

    private Player p1;
    private Deck deck;

    /**
     *  Sets up the Player objects.
     *  Called before every test case method.
     */
    @Before
    public void setUp(){
        p1 = new Player("Kramer", deck);
        deck = new Deck();



    }

    /**
     * Test used to verify the player objects are created successfully.
     */
    @Test
    public void testConstructor(){
        //Make sure the name given matches the one in the object
        Assert.assertEquals("Kramer", p1.getName());

        //make sure that each player is initialized with the right amount of cards
        Assert.assertEquals(7, p1.getHandSize());
    }

    @Test
    public void testAddCardToHand(){
        //Test to check make sure the player starts off with 7 cards
        Assert.assertEquals(7, p1.getHandSize());

        //Add the Card to the players hand.
        p1.addCardToHand(1, deck);

        //Make sure that there a card in the hand
        Assert.assertEquals(8, p1.getHandSize());

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
        Assert.assertEquals(7, p1.getHandSize());

        //Play a card
        Card playedCard = p1.playCard(0, deck);

        //Test to see that there are 6 cards now in hand
        Assert.assertEquals(6, p1.getHandSize());


        //Play each of the cards in the hand, and check the number of cards in the hand
        for(int i=0; i < 6; i++){
           p1.playCard(0, deck);
            Assert.assertEquals(5-i, p1.getHandSize());
        }

    }

    @Test
    public void testGetHandSize(){

        //Test to check make sure the player starts off with 7 cards
        Assert.assertEquals(7, p1.getHandSize());

        //Play a card
        Card playedCard = p1.playCard(0, deck);

        //Play each of the cards in the hand, and check the number of cards in the hand
        for(int i=0; i < 6; i++){
            p1.playCard(0, deck);
            Assert.assertEquals(5-i, p1.getHandSize());
        }

    }
    @Test
    public void testGetName(){
        //test to see that the name given to the player matches the name that is initialized
        Assert.assertEquals("Kramer", p1.getName());

    }


    @Test
    public void testToString(){

        // Play all cards, there should not be nay cards left in the player's hand
        for(int i=0; i<7; i++){
            p1.playCard(0, deck);
        }

        Assert.assertEquals("", p1.toString());
    }


    @Test
    public void testColourInHand(){

        // Play all cards, there should not be nay cards left in the player's hand
        for(int i=0; i<7; i++){
            p1.playCard(0, deck);
        }

        Assert.assertFalse(p1.colourInHand(Card.Colour.RED));
    }

    @Test
    public void testGetCard(){
        Card c = p1.getCard(0);
        Assert.assertEquals(p1.playCard(0, deck), c);
        c = p1.getCard(0);
        Assert.assertEquals(p1.playCard(0, deck), c);
        c = p1.getCard(0);
        Assert.assertEquals(p1.playCard(0, deck), c);
        c = p1.getCard(0);

    }

    @Test
    public void testGetHandScore(){
        //Initially Player has 7 cards, score should not be 0
        assertNotEquals(0,p1.getHandScore());

        // Play all cards, there should not be nay cards left in the player's hand
        for(int i=0; i<7; i++){
            p1.playCard(0, deck);
        }
        //Once playing all cards, score should be zero
        Assert.assertEquals(0, p1.getHandScore());

        //Add card to hand
        p1.addCardToHand(1, deck);
        assertNotEquals(0,p1.getHandScore());
    }



    @Test
    public void TestSetPlayerScore(){

        //Initally Player should have no score
        Assert.assertEquals(0, p1.getPlayerScore());

        p1.setPlayerScore(500);
        Assert.assertEquals(500, p1.getPlayerScore());
    }




}
