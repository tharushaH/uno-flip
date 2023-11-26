import java.util.ArrayList;
import java.util.HashMap;

public class AI extends Player{
    private static int id = 1;
    private static final int DRAW = -1;
    public AI (){
        super("Bot " + id);
        id++;
    }

    /**
     * Returns the index of a playable card that the AI can play, if it matches the colour, rank , or if it is wild
     * If the AI does not have a playable card, the AI will return a -1 to signify the AI must draw a card
     *
     * @param currentColour current colour of the game
     * @param currentRank current rank of the game
     * @return the index of a playable card that the AI has.
     */
    public int playAICard(Card.Colour currentColour, Card.Rank currentRank){
        HashMap<Card, Integer> playAbleCards = new HashMap<>();

        // Get the card and the index of the card that are playable, by matching colour, rank, and if it is wild
        for (int i = 0; i < this.hand.size(); i++){
            if (this.hand.getCards().get(i).getColour() == currentColour || this.hand.getCards().get(i).getRank() == currentRank || this.hand.getCards().get(i).getColour() == Card.Colour.WILD || this.hand.getCards().get(i).getColour() == Card.Colour.WILD_DARK){
                playAbleCards.put(this.hand.getCards().get(i), i);
            }
        }

        // If playable cards empty, AI must draw card
        if (playAbleCards.isEmpty()){
            return DRAW;
        }

        // Check entries if they match the current colour or current rank, play that card
        // Otherwise play the first entry which will be a wild card
        for (Card cardEntry : playAbleCards.keySet()){
            if (cardEntry.getColour() == currentColour || cardEntry.getRank() == currentRank){
                return playAbleCards.get(cardEntry);
            }
        }

        return 0;
    }

    /**
     * Returns the most occurring colour other than wild.
     *
     * @return Card.Colour of the most occurring colour
     */
    public Card.Colour wildPickColour(boolean isLight){
        HashMap<Card.Colour, Integer> countColourMap = new HashMap<>();

        for (Card card: this.hand.getCards()){
            if(countColourMap.containsKey(card.getColour())){
                countColourMap.put(card.getColour(), countColourMap.get(card.getColour()) + 1);
            } else {
                countColourMap.put(card.getColour(), 1);
            }
        }

        int max = -1;
        Card.Colour mostColour = null;
        for(Card.Colour colourEntry: countColourMap.keySet()){
            int count = countColourMap.get(colourEntry);

            if (max < count && colourEntry != Card.Colour.WILD){
                max = count;
                mostColour = colourEntry;
            }
        }
        // Pick default colour if the bot only has wild cards
        if(mostColour == null){
            if(isLight){
                mostColour = Card.Colour.RED;
            } else{
                mostColour = Card.Colour.TEAL;
            }
        }



        return mostColour;
    }
}
