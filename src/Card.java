import java.awt.*;

public class Card {
    public enum Colour {RED, BLUE, YELLOW, GREEN, WILD};
    public enum Rank {ONE, TWO, THREE, FOUR, FIVE, SIX, SEVEN, EIGHT, NINE, DRAW_ONE, REVERSE, SKIP, WILD, WILD_DRAW_2};

    private final Rank rank;
    private final Colour colour;

    public Card(Rank rank, Colour colour){
        if ((rank == Rank.WILD || rank == Rank.WILD_DRAW_2) && !(colour == Colour.WILD))
            throw new IllegalArgumentException("ERROR: Invalid Card Creation.");
        if (colour == Colour.WILD && !(rank == Rank.WILD || rank == Rank.WILD_DRAW_2))
            throw new IllegalArgumentException("ERROR: Invalid Card Creation.");
        this.rank = rank;
        this.colour = colour;
    }

    public boolean isWild(){
        return this.colour == Colour.WILD;
    }

    public Colour getColour() {
        return this.colour;
    }

    public Rank getRank(){
        return this.rank;
    }

    @Override
    public String toString(){
        if (this.rank == Rank.WILD_DRAW_2)
            return this.colour + " DRAW 2";
        if (this.rank == Rank.DRAW_ONE)
            return this.colour + " DRAW 1";
        if (this.rank == Rank.WILD)
            return this.colour + " CARD";
        return this.colour + " " + this.rank;
    }

    @Override
    public boolean equals(Object obj){
        if (obj == null)
            return false;
        if (obj.getClass() != this.getClass())
            return false;
        Card other = (Card) obj;
        return this.rank == other.rank && this.colour == other.colour;
    }

}
