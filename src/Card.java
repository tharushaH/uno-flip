public class Card {
    public enum Colour {RED, BLUE, YELLOW, GREEN, WILD};
    public enum Rank {ONE, TWO, THREE, FOUR, FIVE, SIX, SEVEN, EIGHT, NINE, DRAW_ONE, REVERSE, SKIP, WILD, WILD_DRAW_2};

    private Rank rank;
    private Colour colour;

    public Card(Rank rank, Colour colour){
        this.rank = rank;
        this.colour = colour;
    }

    public boolean isWild(){
        return this.rank == Rank.WILD;
    }

    public Colour getColour() {return null;}

    public Rank getRank(){return null;};

    public String toString(){return null;}

}
