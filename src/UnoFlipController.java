
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
                int numTotalPlayers = 0;
                boolean notFirstTime = false;
                int numPlayers = 0;
                ArrayList<Player> allPlayers = new ArrayList<>();

                do{
                    allPlayers.clear();
                    numTotalPlayers = 0;

                    if (notFirstTime){
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
                        String name = (String) JOptionPane.showInputDialog("Enter player " + Integer.toString(i+1) + " name");
                        Player player;

                        // If player does not select name, give a default name (Player + player number) ex. Player 2
                        if (name == null || name.equals("")) {
                            player = this.model.createPlayer("Player " + (i+1));
                        }
                        else {
                            player =this.model.createPlayer(name);
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

                for(int i = 0; i < numTotalPlayers; i++){
                    this.model.addPlayer(allPlayers.get(i));
                }

                this.model.setNumPlayers(numTotalPlayers);
                this.model.setUpInitialTopCard();
                break;

            // User selects the draw button option to draw a card
            case UnoFlipViewFrame.DRAW_CMD:
                this.model.playTurn(UnoFlipModel.DRAW_ONE_BUTTON);
                break;

            // User selects the next turn button to go to the next turn
            case UnoFlipViewFrame.NEXT_CMD:
                this.model.nextTurn();
                if(this.model.getPlayers().get(this.model.getCurrentTurn()) instanceof AI){
                    this.model.playAITurn();
                }
                break;

            // A card is selected from the hand
            default:
                // Try Catch Exception to catch NumberFormatException if the given command was not an Integer value
                try {
                    // Model playTurn call to play the card at the index of the parsed integer value.
                    this.model.playTurn(Integer.parseInt(e.getActionCommand()));

                    // Makes the current player is the one that placed the wild card and not the next player.
                    if((this.model.getTopCard().isWild() && this.model.getTurnFinished())){

                        if(this.model.isNextPlayerAI()){
                            AI aiPlayer = (AI) this.model.getPlayers().get(this.model.getNextTurn());

                            this.model.setCurrentColour(aiPlayer.wildPickColour());

                            if(this.model.getTopCard().getRank() == Card.Rank.WILD_DRAW_2){
                                this.model.setChallengeFlag(false);
                                this.model.challenge();
                            }
                        } else{
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
                // Catch NumberFormatException for commands that are not Integer values
                catch (NumberFormatException err) {
                    System.out.println("Invalid ActionCommand: " + e.getActionCommand());
                }
        }
    }
}

