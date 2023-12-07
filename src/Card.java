/**
 * The Card class represents a card used in the Uno Flip game. Each card has a color and a rank.
 * It provides methods for checking wild, getting the rank and colour of the card, and generating a string
 * representation of the hand.
 *
 * Date: 2023-10-18
 * @author  Tharusha Herath
 */

public class Card {
    public enum Colour {RED, BLUE, YELLOW, GREEN, WILD, TEAL, PINK, PURPLE, ORANGE, WILD_DARK,NULL};
    public enum Rank {ONE, TWO, THREE, FOUR, FIVE, SIX, SEVEN, EIGHT, NINE, DRAW_ONE, REVERSE, SKIP, WILD, WILD_DRAW_2,
        DRAW_FIVE, SKIP_EVERYONE, WILD_DRAW_COLOUR, FLIP,NULL};

    public static final int RANK_NUMBER_CARDS = 8; //Indicate the ordinal of the last number card in the enum Rank
    public static final int RANK_WILD_DRAW_2 = 13; // Indicate the ordinal of the WILD_DRAW_2 card in the enum Rank

    public static final int RANK_DRAW_ONE = 9;  // ordinal of the draw one enum in Rank enums
    public static final boolean LIGHT = true;
    public static final boolean DARK = false;
    private Rank lightRank;
    private Colour lightColour;
    private Rank darkRank;
    private Colour darkColour;
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
     * Returns the light rank of the card.
     * @return The light rank of the card
     */
    public Rank getLightRank() {
        return lightRank;
    }

    /**
     * Returns the light colour of the card.
     * @return The light colour of the card
     */
    public Colour getLightColour() {
        return lightColour;
    }

    /**
     * Returns the dark rank of the card.
     * @return The dark rank of the card
     */
    public Rank getDarkRank() {
        return darkRank;
    }

    /**
     * Returns the dark colour of the card.
     * @return The dark colour of the card
     */
    public Colour getDarkColour() {
        return darkColour;
    }

    /**
     * Returns the current side (light or dark) of all Card objects (defined by constants LIGHT and DARK)
     * @return LIGHT (true) if light side, DARK (false) if dark side
     */
    public static boolean getSide() {
        return side;
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
        if (this.getRank() == Rank.DRAW_FIVE)
            return (this.getColour() + "_draw_5").toLowerCase();
        if (this.getRank() == Rank.WILD && (this.getColour() == Colour.WILD || this.getColour() == Colour.WILD_DARK))
            return (this.getColour() + "_card").toLowerCase();
        if (this.getRank() == Rank.WILD_DRAW_COLOUR)
            return ("wild_draw_colour");
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

    /**
     * Returns an XML representation of the Card.
     * @param numTabs The number of tabs the main opening tag should start with.
     * @return XML representation of the card.
     */
    public String toXML(int numTabs) {
        String baseTabLength = "";    // any tabs within the return String will append to this base tab length

        // add tabs to the base tab length
        for (int i = 0; i < numTabs; i++) {
            baseTabLength += "\t";
        }

        return baseTabLength +
                "<Card>\n" + baseTabLength + "\t" +
                "<lightRank>" + this.getLightRank() + "</lightRank>\n" + baseTabLength + "\t" +
                "<lightColour>" + this.getLightColour() + "</lightColour>\n" + baseTabLength + "\t" +
                "<darkRank>" + this.getDarkRank() + "</darkRank>\n" + baseTabLength + "\t" +
                "<darkColour>" + this.getDarkColour() + "</darkColour>\n" + baseTabLength + "\t" +
                "<side>" + Card.getSide() + "</side>\n" + baseTabLength +
                "</Card>";
    }

    public void setLightRank(Rank lightRank) {
        this.lightRank = lightRank;
    }

    public void setLightColour(Colour lightColour) {
        this.lightColour = lightColour;
    }

    public void setDarkRank(Rank darkRank) {
        this.darkRank = darkRank;
    }

    public void setDarkColour(Colour darkColour) {
        this.darkColour = darkColour;
    }

    public static void setSide(boolean side) {
        Card.side = side;
    }
}
