/**
 * Reverse is a subclass of TurnSequence used to handle the game sequences of reverse card in the game, Uno Flip! by
 * updating the game's state.
 * @author Hubert Dang
 * Date: 2023-10-22
 */
public class Reverse extends TurnSequence {

    public Reverse(UnoFlipModel unoFlipModel) {
        super(unoFlipModel);
    }

    /**
     * Executes the appropriate game sequence according to the card played by changing the game state.
     *
     * @param card The card that was played
     */
    @Override
    public void executeSequence(Card card) {
        unoFlipModel.setTopCard(card);
        unoFlipModel.setCurrentColour(card.getColour());
        unoFlipModel.setCurrentRank(card.getRank());
        unoFlipModel.flipTurnDirection();
        unoFlipModel.nextTurn();
    }
}
