public class SelfDrawOne extends TurnSequence {

    /**
     * Executes the appropriate game sequence according to the card played by changing the game state.
     *
     * @param card The card that was played
     */
    @Override
    public void executeSequence(Card card) {
        game.drawNCards(1, game.getCurrentTurn());
        game.nextTurn();
    }
}
