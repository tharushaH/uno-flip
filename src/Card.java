/**
 * The Card class represents a card used in the Uno Flip game. Each card has a color and a rank.
 * It provides methods for checking wild, getting the rank and colour of the card, and generating a string
 * representation of the hand.
 *
 * Date: 2023-10-18
 * @author  Tharusha Herath
 */

public class Card {
    public enum Colour {RED, BLUE, YELLOW, GREEN, WILD, TEAL, PINK, PURPLE, ORANGE, WILD_DARK};
    public enum Rank {ONE, TWO, THREE, FOUR, FIVE, SIX, SEVEN, EIGHT, NINE, DRAW_ONE, REVERSE, SKIP, WILD, WILD_DRAW_2,
        DRAW_FIVE, SKIP_EVERYONE, WILD_DRAW_COLOUR, FLIP};

    public static final int RANK_NUMBER_CARDS = 8; //Indicate the ordinal of the last number card in the enum Rank
    public static final int RANK_WILD_DRAW_2 = 13; // Indicate the ordinal of the WILD_DRAW_2 card in the enum Rank

    public static final int RANK_DRAW_ONE = 9;  // ordinal of the draw one enum in Rank enums
    public static final boolean LIGHT = true;
    public static final boolean DARK = false;
    private final Rank lightRank;
    private final Colour lightColour;
    private final Rank darkRank;
    private final Colour darkColour;
    private static boolean side;


    /**
     * Constructs a Card object with the specified rank and color.
     *
     * @param lightRank The card's light side rank
     * @param lightColour The card's light side colour
     * @param darkRank The card's dark side rank
     * @param darkColour The card's dark side colour
     * @throws IllegalArgumentException if an invalid card is attempted to be created
     */
    public Card(Rank lightRank, Colour lightColour, Rank darkRank, Colour darkColour){
        this.lightRank = lightRank;
        this.lightColour = lightColour;
        this.darkRank = darkRank;
        this.darkColour = darkColour;
        side = LIGHT;
    }

    /**
     * Checks if the card is a wild card.
     *
     * @return true if the card is a wild card, false otherwise
     */
    public boolean isWild(){
        return (this.lightColour == Colour.WILD && this.darkColour == Colour.WILD_DARK);
    }

    /**
     * Returns the colour of the card.
     * @return the colour of the Card.
     */
    public Colour getColour() {
        if (side == LIGHT) {
            return lightColour;
        } else {
            return darkColour;
        }
    }

    /**
     * Returns the rank of the card.
     * @return the rank of the Card.
     */
    public Rank getRank(){
        if (side == LIGHT) {
            return lightRank;
        } else {
            return darkRank;
        }
    }

    /**
     * Flips the side of all cards.
     */
    public static void flipSide() {
        side = !side;
    }

    /**
     * Returns a string representation of the card, displaying its color followed by its rank.
     *
     * @return the string representation of the card
     */
    @Override
    public String toString() {
        // some cards' formats cannot be derived purely from their rank and colour enums
        if (this.getRank() == Rank.WILD_DRAW_2)
            return (this.getColour() + "_draw_2").toLowerCase();
        if (this.getRank() == Rank.DRAW_ONE)
            return (this.getColour() + "_draw_1").toLowerCase();
        if (this.getRank() == Rank.WILD && (this.getColour() == Colour.WILD || this.getColour() == Colour.WILD_DARK))
            return (this.getColour() + "_card").toLowerCase();
        if (this.getRank() == Rank.WILD_DRAW_COLOUR)
            return (this.getColour() + "_draw_colour");
        if(this.getRank().ordinal() < 9) // if number card
            return (this.getColour() + "_"+ (this.getRank().ordinal()+1)).toLowerCase();    // +1 because ordinal 0-based

        return (this.getColour() + "_" + this.getRank()).toLowerCase();   // if a Colour non-number card
    }

    /**
     * Checks if this card is equal to another object.
     *
     * @param obj the object to compare with this card
     * @return true if the cards are equal, false otherwise
     */
    @Override
    public boolean equals(Object obj){
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (obj.getClass() != this.getClass())
            return false;
        Card other = (Card) obj;
        return this.getRank() == other.getRank() && this.getColour() == other.getColour();
    }

}
