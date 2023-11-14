import java.util.EventObject;

public class UnoFlipEvent extends EventObject {
    private final String currPlayerName;
    private final String topCard;                 // toString() of the top card
    private final String currHand;                // toString() of the current hand
    private final String status;              // toString() of the current colour
    private final boolean isWild;

    /**
     * Constructs a Uno Flip! Event.
     *
     * @param model the object on which the Event initially occurred
     * @throws IllegalArgumentException if source is null
     */
    public UnoFlipEvent(UnoFlipModel model, String currPlayerName, String topCard, String currHand, String status, boolean isWild) {
        super(model);
        this.currPlayerName = currPlayerName;
        this.topCard = topCard;
        this.currHand = currHand;
        this.status = status;
        this.isWild = isWild;
    }

    /**
     * Gets the current player name of the event.
     *
     * @return the current player name
     */
    public String getCurrPlayerName() {
        return currPlayerName;
    }

    /**
     * Gets the string representation of the top card of the event.
     *
     * @return the string representation of the top card
     */
    public String getTopCard() {
        return topCard;
    }

    /**
     * Gets the string representation of the current hand of the event.
     *
     * @return the string representation of the current hand
     */
    public String getCurrHand() {
        return currHand;
    }

    /**
     * Gets the string representation of the current colour of the event.
     *
     * @return the string representation of the current colour
     */
    public String getStatus() {
        return status;
    }

    /**
     * Gets the boolean if the top card is wild or not.
     *
     * @return true if wild, otherwise false
     */
    public boolean getIsWild() {
        return isWild;
    }
}