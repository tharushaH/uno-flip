/**
 * Number is a subclass of TurnSequence used to handle the game sequences of numbered cards in the game, Uno Flip! by
 * updating the game's state.
 * @author Hubert Dang
 */
public class Number extends TurnSequence{

    public Number(Game game) {
        super(game);
    }

    /**
     * Executes the appropriate game sequence according to the card played by changing the game state.
     *
     * @param card The card that was played
     */
    @Override
    public void executeSequence(Card card) {
        game.setTopCard(card);
        game.setCurrentColour(card.getColour());
        game.setCurrentRank(card.getRank());
        game.nextTurn();
    }
}
