/**
 * TurnSequence is an abstract class to be extended in order to handle game sequences based on the card played.
 * @author Hubert Dang
 * Date: 2023-10-22
 */
public abstract class TurnSequence {
    protected static Game game;

    public TurnSequence(Game game){
        this.game = game;
    }

    /**
     * Executes the appropriate game sequence according to the card played by changing the game state.
     * @param card The card that was played
     */
    public abstract void executeSequence(Card card);

    /**
     * Checks if the card to play is a valid card to play according to Uno Flip! rules.
     * @param card The card to play
     * @return true if the card is valid, false otherwise
     */
    public boolean isValid(Card card) {
        if (card.isWild()) {
            if (card.getRank() == Card.Rank.WILD_DRAW_2) {
                if (game.getCurrentPlayer().colourInHand(game.getCurrentColour())) {
                    return false;   // can only play wild draw 2 if player doesn't have current colour
                }
            }
            return true;
        }
        return game.getCurrentRank() == card.getRank() || game.getCurrentColour() == card.getColour();
    }
}
