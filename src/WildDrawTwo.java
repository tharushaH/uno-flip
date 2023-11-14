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

        unoFlipModel.setStatus(UnoFlipModel.CHALLENGE_STATUS_MESSAGE);
        unoFlipModel.notifyViews();

        if( unoFlipModel.getChallenge()) { // If next player challenges

            if( isValid(unoFlipModel.getTopCard())) { // IF WILD DRAW 2 is valid
                unoFlipModel.drawNCards(4, unoFlipModel.getNextTurn());
                unoFlipModel.setStatus("INNOCENT: NEXT PLAYER DRAWS 4 CARDS");
                unoFlipModel.notifyViews();

            } else {                    // If WILD DRAW 2 is not valid
                unoFlipModel.drawNCards(2, unoFlipModel.getCurrentTurn());
                unoFlipModel.setStatus("GUILTY:YOU DRAW 2 CARDS");
                unoFlipModel.notifyViews();
            }

        } else {
            unoFlipModel.drawNCards(2, unoFlipModel.getNextTurn());
        }

        unoFlipModel.skipTurn();
    }
}
