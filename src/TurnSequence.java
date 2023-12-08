/**
 * TurnSequence is an abstract class to be extended in order to handle game sequences based on the card played.
 * @author Hubert Dang
 * Date: 2023-10-22
 */
public abstract class TurnSequence {
    protected static UnoFlipModel unoFlipModel;

    public TurnSequence(UnoFlipModel unoFlipModel){
        this.unoFlipModel = unoFlipModel;
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
            return true;
        }
        return unoFlipModel.getCurrentRank() == card.getRank() || unoFlipModel.getCurrentColour() == card.getColour();
    }

    /**
     * Sets the model with the passed model
     * @param model
     */
    public static void setUnoFlipModel(UnoFlipModel model){
        unoFlipModel = model;
    }
}
