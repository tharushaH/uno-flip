public class WildDrawColour extends TurnSequence {


    public WildDrawColour(UnoFlipModel unoFlipModel) {
        super(unoFlipModel);
    }

    @Override
    public void executeSequence(Card card) {
        unoFlipModel.setTopCard(card);
        unoFlipModel.setCurrentColour(Card.Colour.WILD_DARK);
        unoFlipModel.setCurrentRank(Card.Rank.WILD_DRAW_COLOUR);
        unoFlipModel.setStatus(UnoFlipModel.STATUS_CHALLENGE_MESSAGE);
        unoFlipModel.setSkipTurnFlag();
    }
}
