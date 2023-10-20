public class WildDrawTwo extends TurnSequence {

    /**
     * Executes the appropriate game sequence according to the card played by changing the game state.
     *
     * @param card The card that was played
     */
    @Override
    public void executeSequence(Card card) {
        game.setTopCard(card);
        game.setCurrentColour(game.getColourSelectedByWild());
        game.setCurrentRank(Card.Rank.WILD);
        game.drawNCards(2, game.getNextTurn());
        game.skipTurn();
    }
}