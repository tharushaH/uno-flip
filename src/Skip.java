public class Skip extends TurnSequence {

    public Skip(Game game) {
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
        game.skipTurn();
    }
}
