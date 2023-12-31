/**
 * WildDrawTwo is a subclass of TurnSequence used to handle the game sequences of Wild Draw Two cards in the game, Uno Flip! by
 * updating the game's state.
 * @author Hubert Dang
 * Date: 2023-10-22
 */
public class WildDrawTwo extends TurnSequence {

    public WildDrawTwo(UnoFlipModel unoFlipModel) {
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
        unoFlipModel.setCurrentColour(Card.Colour.WILD);
        unoFlipModel.setCurrentRank(Card.Rank.WILD_DRAW_2);
        unoFlipModel.setStatus(UnoFlipModel.STATUS_CHALLENGE_MESSAGE);
        unoFlipModel.setSkipTurnFlag();
    }
}
