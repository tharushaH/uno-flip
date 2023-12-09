
import javax.swing.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Objects;



/**
 * The UnoFlipController class implements the ActionListener interface
 * to handle user interactions and events in the Uno Flip game.
 */

public class UnoFlipController implements ActionListener {
    private UnoFlipModel model;

    /**
     * Constructs a UnoFlipController with the specified UnoFlipModel.
     *
     * @param model The UnoFlipModel instance associated with the controller.
     */

    public UnoFlipController(UnoFlipModel model) {
        this.model = model;
    }

    /**
     * Invoked when an action occurs. Handles various actions triggered
     * by user interactions with the Uno Flip game.
     *
     * @param e The ActionEvent representing the user's action.
     */

    @Override
    public void actionPerformed(ActionEvent e) {
        String actionCommand = e.getActionCommand();
        JComboBox comboBox;
        int result = 0;

        switch (actionCommand) {

            // User starts the game and is prompted for amount of players and names for each player
            case UnoFlipViewFrame.START_CMD:
                int numTotalPlayers = 0;
                boolean notFirstTime = false;
                int numPlayers = 0;
                ArrayList<Player> allPlayers = new ArrayList<>();

                do {
                    allPlayers.clear();
                    numTotalPlayers = 0;

                    if (notFirstTime) {
                        JOptionPane.showMessageDialog(null,
                                "There needs to be more than 2 total players.",
                                "Player creation error",
                                JOptionPane.ERROR_MESSAGE);
                    }
                    // Amount of players options for drop down menu
                    String[] numPlayerOptions = {"1", "2", "3", "4", "5", "6"};

                    comboBox = new JComboBox<>(numPlayerOptions);


                    result = JOptionPane.showOptionDialog(

                            null,
                            comboBox,
                            "Select number of players:",
                            JOptionPane.DEFAULT_OPTION,
                            JOptionPane.PLAIN_MESSAGE,
                            null,
                            null,
                            null);

                    // initialize the number of players with the inputted amount from the drop-down menu
                    if (result == JOptionPane.OK_OPTION) {

                        String option = (String) comboBox.getSelectedItem();
                        numPlayers = Integer.parseInt(option);
                        numTotalPlayers += numPlayers;

                    } else { // Exit the game if the user cancels, absolutely must select an option, otherwise, do not start the game
                        System.exit(0);
                    }

                    // Prompt the user for the name of each player
                    for (int i = 0; i < numPlayers; i++) {
                        String name = (String) JOptionPane.showInputDialog("Enter player " + Integer.toString(i + 1) + " name");
                        Player player;

                        // If player does not select name, give a default name (Player + player number) ex. Player 2
                        if (name == null || name.equals("")) {
                            player = this.model.createPlayer("Player " + (i + 1));
                        } else {
                            player = this.model.createPlayer(name);
                        }
                        allPlayers.add(player);
                    }


                    // Amount of AI players options for drop down menu
                    String[] numAIPlayerOptions = {"0", "1", "2", "3", "4", "5", "6"};

                    comboBox = new JComboBox<>(numAIPlayerOptions);


                    result = JOptionPane.showOptionDialog(

                            null,
                            comboBox,
                            "Select number of AI players:",
                            JOptionPane.DEFAULT_OPTION,
                            JOptionPane.PLAIN_MESSAGE,
                            null,
                            null,
                            null);

                    // initialize the number of players with the inputted amount from the drop-down menu
                    if (result == JOptionPane.OK_OPTION) {
                        String option = (String) comboBox.getSelectedItem();
                        numPlayers = Integer.parseInt(option);
                        numTotalPlayers += numPlayers;

                    } else { // Exit the game if the user cancels, absolutely must select an option, otherwise, do not start the game
                        System.exit(0);
                    }

                    // Prompt the user for the name of each player
                    for (int i = 0; i < numPlayers; i++) {
                        AI aiPlayer = this.model.createAIPlayer();

                        allPlayers.add(aiPlayer);
                    }
                    notFirstTime = true;
                } while (numTotalPlayers < 2);

                for (int i = 0; i < numTotalPlayers; i++) {
                    this.model.addPlayer(allPlayers.get(i));
                }

                this.model.setNumPlayers(numTotalPlayers);
                this.model.setUpInitialTopCard();
                this.model.savePrev();
                break;

            // User selects the draw button option to draw a card
            case UnoFlipViewFrame.DRAW_CMD:
                this.model.playTurn(UnoFlipModel.DRAW_ONE_BUTTON);
                break;

            case UnoFlipViewFrame.UNDO_CMD:
                this.model.undoRedoTurn();
                UnoFlipViewFrame.redo.setEnabled(true);
                UnoFlipViewFrame.undo.setEnabled(false);
                break;
            case UnoFlipViewFrame.REDO_CMD:
                this.model.undoRedoTurn();
                UnoFlipViewFrame.redo.setEnabled(false);
                UnoFlipViewFrame.undo.setEnabled(true);
                break;
            case UnoFlipViewFrame.REPLAY_CMD:
                this.model.restartGame();
                break;
            case UnoFlipViewFrame.SAVE_CMD:
                this.model.saveGame();
                JOptionPane.showMessageDialog(null, "Current game saved...", "Game Save", JOptionPane.INFORMATION_MESSAGE);
                break;
            case UnoFlipViewFrame.LOAD_CMD:
                this.model.loadGame();
                JOptionPane.showMessageDialog(null, "Loaded saved game...", "Game loaded", JOptionPane.INFORMATION_MESSAGE);
                break;

            // User selects the next turn button to go to the next turn
            case UnoFlipViewFrame.NEXT_CMD:
                UnoFlipViewFrame.redo.setEnabled(false);
                UnoFlipViewFrame.undo.setEnabled(false);
                this.model.nextTurn();
                if (this.model.getPlayers().get(this.model.getCurrentTurn()) instanceof AI) {
                    this.model.playAITurn();

                    if(!(this.model.isWinner())) {
                        model.setPreviousColour(model.getCurrentColour()); // hold the current colour before playing next card

                        if ((this.model.getTopCard().isWild() && this.model.getTurnFinished()) && (this.model.getStatus() != UnoFlipModel.AI_DRAW_CARD)) {

                            // Lets the AI pick the colour depending on wildPickColour implementation
                            this.model.setCurrentColour(((AI) this.model.getPlayers().get(this.model.getCurrentTurn())).wildPickColour(this.model.getCardSide()));

                            // If top card is wild draw 2 or wild draw colour, prompt for challenges
                            if (this.model.getTopCard().getRank() == Card.Rank.WILD_DRAW_2 || this.model.getTopCard().getRank() == Card.Rank.WILD_DRAW_COLOUR) {

                                // If next player is not an AI and is human prompt for challenge
                                if (!this.model.isNextPlayerAI()) {
                                    result = JOptionPane.showConfirmDialog(null, "Do you want to challenge?", "Confirmation", JOptionPane.YES_NO_OPTION);

                                    boolean challenge = result == JOptionPane.YES_OPTION;

                                    if (challenge) {
                                        boolean guilty = model.challenge(); // returns true if guilty, false if innocent
                                        if (guilty) {
                                            model.guiltyConsequences();
                                        } else {
                                            model.innocentConsequences();
                                        }
                                    } else {
                                        // standard flow of the game if no challenge
                                        if (model.getTopCard().getRank() == Card.Rank.WILD_DRAW_2) {
                                            // wild draw 2 flow of events
                                            model.drawNCards(2, model.getNextTurn());
                                            model.setStatus(UnoFlipModel.STATUS_DONE);
                                        } else {
                                            // wild draw colour flow of events
                                            model.drawCardUntilColour(model.getCurrentColour(), model.getNextTurn());
                                            model.setStatus(UnoFlipModel.STATUS_DONE);
                                        }
                                    }
                                } else {
                                    // standard flow of the game if no challenge
                                    if (model.getTopCard().getRank() == Card.Rank.WILD_DRAW_2) {
                                        // wild draw 2 flow of events
                                        model.drawNCards(2, model.getNextTurn());
                                        model.setStatus(UnoFlipModel.STATUS_DONE);
                                    } else {
                                        // wild draw colour flow of events
                                        model.drawCardUntilColour(model.getCurrentColour(), model.getNextTurn());
                                        model.setStatus(UnoFlipModel.STATUS_DONE);
                                    }
                                }
                            }
                        }
                    }

                }
                this.model.savePrev();
                break;

            // A card is selected from the hand
            default:
                // Try Catch Exception to catch NumberFormatException if the given command was not an Integer value
                try {
                    model.setPreviousColour(model.getCurrentColour()); // hold the current colour before playing next card

                    // Model playTurn call to play the card at the index of the parsed integer value.
                    this.model.playTurn(Integer.parseInt(e.getActionCommand()));

                    if(!(this.model.isWinner())) {
                        // Makes the current player is the one that placed the wild card and not the next player.
                        if ((this.model.getTopCard().isWild() && this.model.getTurnFinished())) {

                            // Colour options for Player to chose from
                            String[] lightColourOptions = {Card.Colour.RED.toString(), Card.Colour.BLUE.toString(),
                                    Card.Colour.YELLOW.toString(), Card.Colour.GREEN.toString()};
                            String[] darkColourOptions = {Card.Colour.ORANGE.toString(), Card.Colour.PINK.toString(),
                                    Card.Colour.PURPLE.toString(), Card.Colour.TEAL.toString()};

                            if (model.getCardSide() == Card.LIGHT) {
                                comboBox = new JComboBox<>(lightColourOptions);
                            } else {
                                comboBox = new JComboBox<>(darkColourOptions);
                            }

                            while (true) {
                                result = JOptionPane.showOptionDialog(
                                        null,
                                        comboBox,
                                        "Select colour:",
                                        JOptionPane.DEFAULT_OPTION,
                                        JOptionPane.PLAIN_MESSAGE,
                                        null,
                                        null,
                                        null);

                                // User chooses colour and selects okay to change the current colour
                                if (result == JOptionPane.OK_OPTION) {
                                    Card.Colour colour;

                                    String option = (String) comboBox.getSelectedItem();
                                    colour = Card.Colour.valueOf(option);

                                    this.model.setCurrentColour(colour);
                                    break;
                                }
                            }

                            if (this.model.getTopCard().getRank() == Card.Rank.WILD_DRAW_2 || this.model.getTopCard().getRank() == Card.Rank.WILD_DRAW_COLOUR) {
                                if (this.model.isNextPlayerAI()) {
                                    // standard flow of the game if no challenge
                                    if (model.getTopCard().getRank() == Card.Rank.WILD_DRAW_2) {
                                        // wild draw 2 flow of events
                                        model.drawNCards(2, model.getNextTurn());
                                        model.setStatus(UnoFlipModel.STATUS_DONE);
                                    } else {
                                        // wild draw colour flow of events
                                        model.drawCardUntilColour(model.getCurrentColour(), model.getNextTurn());
                                        model.setStatus(UnoFlipModel.STATUS_DONE);
                                    }
                                } else {
                                    result = JOptionPane.showConfirmDialog(null, "Do you want to challenge?", "Confirmation", JOptionPane.YES_NO_OPTION);

                                    boolean challenge = result == JOptionPane.YES_OPTION;

                                    if (challenge) {
                                        boolean guilty = model.challenge(); // returns true if guilty, false if innocent
                                        if (guilty) {
                                            model.guiltyConsequences();
                                        } else {
                                            model.innocentConsequences();
                                        }
                                    } else {
                                        // standard flow of the game if no challenge
                                        if (model.getTopCard().getRank() == Card.Rank.WILD_DRAW_2) {
                                            // wild draw 2 flow of events
                                            model.drawNCards(2, model.getNextTurn());
                                            model.setStatus(UnoFlipModel.STATUS_DONE);
                                        } else {
                                            // wild draw colour flow of events
                                            model.drawCardUntilColour(model.getCurrentColour(), model.getNextTurn());
                                            model.setStatus(UnoFlipModel.STATUS_DONE);
                                        }
                                    }
                                }
                            }

                        }
                    }

                } catch (NumberFormatException err) {
                    System.out.println("Invalid command");
                }
        }
    }
}

