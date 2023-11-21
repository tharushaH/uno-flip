/**
 * Flip is a subclass of TurnSequence used to handle the game sequences of flip cards in the game, Uno Flip! by
 * flipping all the cards to the other side, updating the game's state.
 * @author Amilesh Nanthakumaran
 * Date: 2023-11-20
 */
public class Flip extends TurnSequence{

    public Flip(UnoFlipModel unoFlipModel) {
        super(unoFlipModel);
    }

    /**
     * Executes the appropriate game sequence according to the card played by changing the game state.
     *
     * @param card The card that was played
     */
    @Override
    public void executeSequence(Card card) {
        Card.flipSide();
        unoFlipModel.setTopCard(card);
        unoFlipModel.setCurrentColour(card.getColour());
        unoFlipModel.setCurrentRank(card.getRank());
        unoFlipModel.nextTurn();
    }
}
