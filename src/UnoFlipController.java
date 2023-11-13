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
        if (e.getActionCommand().equals(UnoFlipViewFrame.START_CMD)){

            String[] numPlayerOptions = {"2", "3", "4"};

            JComboBox<String> comboBox = new JComboBox<>(numPlayerOptions);

            int result = JOptionPane.showOptionDialog(
                    null,
                    comboBox,
                    "Select number of players:",
                    JOptionPane.DEFAULT_OPTION,
                    JOptionPane.PLAIN_MESSAGE,
                    null,
                    null,
                    null);

            int numPlayers = 0;
            if (result == JOptionPane.OK_OPTION) {
                String option = (String) comboBox.getSelectedItem();
                numPlayers = Integer.parseInt(option);
                this.model.setNumPlayers(numPlayers);
            } else {
                System.exit(0); // absolutely must select an option, otherwise, do not start the game
            }

            for (int i = 0; i < numPlayers; i++){
                String name = (String) JOptionPane.showInputDialog("Enter player " + Integer.toString(i+1) + " name");
                if (name.equals("")){
                    this.model.setPlayer("Player " + (i+1));
                } else{
                    this.model.setPlayer(name);
                }
            }
            this.model.setUpInitialTopCard();
        }
        else if(e.getActionCommand().equals(UnoFlipViewFrame.DRAW_CMD)){
            this.model.playTurn(-1);
        }
        else if(e.getActionCommand().equals(UnoFlipViewFrame.NEXT_CMD)) {
            this.model.nextTurn();
        }
        else {
            try {
                this.model.playTurn(Integer.parseInt(e.getActionCommand()));

            } catch (NumberFormatException err) {
                System.out.println("Invalid ActionCommand: " + e.getActionCommand());
            }
        }
    }
}
