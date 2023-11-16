import java.awt.*;


/**
 * The Card class represents a card used in the Uno Flip game. Each card has a color and a rank.
 * It provides methods for checking wild, getting the rank and colour of the card, and generating a string
 * representation of the hand.
 *
 * Date: 2023-10-18
 * @author  Tharusha Herath
 */

public class Card {
    public enum Colour {RED, BLUE, YELLOW, GREEN, WILD};
    public enum Rank {ONE, TWO, THREE, FOUR, FIVE, SIX, SEVEN, EIGHT, NINE, DRAW_ONE, REVERSE, SKIP, WILD, WILD_DRAW_2};

    public static final int RANK_NUMBER_CARDS = 8; //Indicate the ordinal of the last number card in the enum Rank
    public static final int RANK_WILD_DRAW_2 = 13; // Indicate the ordinal of the WILD_DRAW_2 card in the enum Rank
    private final Rank rank;
    private final Colour colour;

    /**
     * Constructs a Card object with the specified rank and color.
     *
     * @param rank   the rank of the card
     * @param colour the color of the card
     * @throws IllegalArgumentException if an invalid card is attempted to be created
     */
    public Card(Rank rank, Colour colour){
        if ((rank == Rank.WILD || rank == Rank.WILD_DRAW_2) && !(colour == Colour.WILD))
            throw new IllegalArgumentException("ERROR: Invalid Card Creation.");
        if (colour == Colour.WILD && !(rank == Rank.WILD || rank == Rank.WILD_DRAW_2))
            throw new IllegalArgumentException("ERROR: Invalid Card Creation.");
        this.rank = rank;
        this.colour = colour;
    }

    /**
     * Checks if the card is a wild card.
     *
     * @return true if the card is a wild card, false otherwise
     */
    public boolean isWild(){
        return this.colour == Colour.WILD;
    }

    /**
     * Returns the colour of the card.
     * @return the colour of the Card.
     */
    public Colour getColour() {
        return this.colour;
    }

    /**
     * Returns the rank of the card.
     * @return the rank of the Card.
     */
    public Rank getRank(){
        return this.rank;
    }

    /**
     * Returns a string representation of the card, displaying its color followed by its rank.
     *
     * @return the string representation of the card
     */
    @Override
    public String toString(){

        if (this.rank == Rank.WILD_DRAW_2)  //If a Wild Draw 2
            return (this.colour + "_draw_2").toLowerCase();
        if (this.rank == Rank.DRAW_ONE)     //If a Wild Draw 1
            return (this.colour + "_draw_1").toLowerCase();
        if (this.rank == Rank.WILD)         //If a Wild Card
            return (this.colour + "_card").toLowerCase();

        if(this.rank.ordinal() < 9){ //If number card
            return (this.colour + "_"+ (this.rank.ordinal()+1)).toLowerCase();
        }

        return (this.colour + "_" + this.rank).toLowerCase();   //If a Colour non-number card

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
        return this.rank == other.rank && this.colour == other.colour;
    }

}
