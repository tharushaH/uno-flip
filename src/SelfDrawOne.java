/**
 * SelfDrawOne is a subclass of TurnSequence used to handle the game sequences of self drawing one in the game, Uno Flip! by
 * updating the game's state.
 * @author Hubert Dang
 * Date: 2023-10-22
 */
public class SelfDrawOne extends TurnSequence {

    public SelfDrawOne(UnoFlipModel unoFlipModel) {
        super(unoFlipModel);
    }

    /**
     * Executes the appropriate game sequence according to the card played by changing the game state.
     *
     * @param card The card that was played
     */
    @Override
    public void executeSequence(Card card) {
        unoFlipModel.drawNCards(1, unoFlipModel.getCurrentTurn());
    }
}
