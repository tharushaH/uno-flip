
import javax.swing.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
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

    public UnoFlipController(UnoFlipModel model){
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
        String actionCommand =  e.getActionCommand();
        JComboBox comboBox;
        int result = 0;

        switch(actionCommand){

            // User starts the game and is prompted for amount of players and names for each player
            case UnoFlipViewFrame.START_CMD:

                // Amount of players options for drop down menu
                String[] numPlayerOptions = {"2", "3", "4"};

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
                    int numPlayers = 0;

                    String option = (String) comboBox.getSelectedItem();
                    numPlayers = Integer.parseInt(option);

                    this.model.setNumPlayers(numPlayers);
                } else { // Exit the game if the user cancels, absolutely must select an option, otherwise, do not start the game
                    System.exit(0);
                }

                // Prompt the user for the name of each player
                for (int i = 0; i < this.model.getNumPlayers(); i++) {
                    String name = (String) JOptionPane.showInputDialog("Enter player " + Integer.toString(i+1) + " name");
                    Player player;

                    // If player does not select name, give a default name (Player + player number) ex. Player 2
                    if (name == null || name.equals("")) {
                        player = this.model.createPlayer("Player " + (i+1));
                    }
                    else {
                        player =this.model.createPlayer(name);
                    }
                    this.model.addPlayer(player);
                }

                this.model.setUpInitialTopCard();
                break;

            // User selects the draw button option to draw a card
            case UnoFlipViewFrame.DRAW_CMD:
                this.model.playTurn(UnoFlipModel.DRAW_ONE_BUTTON);
                break;

            // User selects the next turn button to go to the next turn
            case UnoFlipViewFrame.NEXT_CMD:
                this.model.nextTurn();
                break;

            // A card is selected from the hand
            default:
                // Try Catch Exception to catch NumberFormatException if the given command was not an Integer value
                try {
                    // Model playTurn call to play the card at the index of the parsed integer value.
                    this.model.playTurn(Integer.parseInt(e.getActionCommand()));

                    // Makes the current player is the one that placed the wild card and not the next player.
                    if((this.model.getTopCard().isWild() && this.model.getTurnFinished())){

                        // Colour options for Player to chose from
                        String[] colourOptions = {"RED", "BLUE", "YELLOW", "GREEN"};

                        comboBox = new JComboBox<>(colourOptions);

                        while(true) {
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

                        // User places a Wild Draw 2 and prompts the next player to challenge
                        if(this.model.getTopCard().getRank() == Card.Rank.WILD_DRAW_2){
                            result = JOptionPane.showConfirmDialog(null, "Do you want to challenge?", "Confirmation", JOptionPane.YES_NO_OPTION);

                            boolean challenge = result == JOptionPane.YES_OPTION;

                            this.model.setChallengeFlag(challenge);
                            this.model.challenge();
                        }
                    }

                }
                // Catch NumberFormatException for commands that are not Integer values
                catch (NumberFormatException err) {
                    System.out.println("Invalid ActionCommand: " + e.getActionCommand());
                }
        }
    }
}

