/**
 * TurnSequence is an abstract class to be extended in order to handle game sequences based on the card played.
 */
public abstract class TurnSequence {
    protected static Game game = new Game();

    /**
     * Executes the appropriate game sequence according to the card played by changing the game state.
     */
    public abstract void executeSequence();

    /**
     * Checks if the card to play is a valid card to play according to Uno Flip! rules.
     * @param card The card to play
     * @return true if the card is valid, false otherwise
     */
    public boolean isValid(Card card) {
        if (card.isWild()) {
            return true;
        }
        return game.getCurrentRank() == card.getRank() || game.getCurrentColour() == card.getColour();
    }
}
