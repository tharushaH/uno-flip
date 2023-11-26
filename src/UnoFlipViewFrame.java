import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;


/**
 * Create the main Uno Flip! game frame.
 */
public class UnoFlipViewFrame extends JFrame implements UnoFlipView {
    private UnoFlipController controller;
    private JPanel handPanel;

    private JPanel buttonPanel;
    private JLabel topCardLabel;
    private JLabel currPlayerLabel;
    private JLabel topCardNameLabel;
    private JTextArea statusArea;
    private JButton drawCard;
    private JPanel currentColourPanel;
    private HashMap<String,ImageIcon> imageIconHashMap;
    public final static String DRAW_CMD = "draw";
    public final static String NEXT_CMD = "next";
    public final static String START_CMD = "start";
    public final static String WILD_CMD = "wild";
    public final static String CHALLENGE_CMD = "challenge";


    public UnoFlipViewFrame() {
        super("Uno Flip!");
        this.setLayout(new BorderLayout(20,50));

        UnoFlipModel model = new UnoFlipModel();
        model.addUnoFlipView(this);
        controller = new UnoFlipController(model);

        // make a panel to display info on the current turn
        JPanel currTurnPanel = new JPanel();
        currTurnPanel.setLayout(new BorderLayout(30,30));

        // add player name to the current turn panel
        currPlayerLabel = new JLabel();
        currPlayerLabel.setBounds(-100, 0,100,0);
        this.add(currPlayerLabel, BorderLayout.WEST);

        // add top card to the current turn panel
        topCardLabel = new JLabel();
        currTurnPanel.add(topCardLabel, BorderLayout.CENTER);

        // add sample top card name to the current turn panel
        topCardNameLabel = new JLabel();

        // Create a layered pane to layer components
        JLayeredPane layeredPane = new JLayeredPane();

        // Set the layout of the layered pane to null to manually set the positions of components
        layeredPane.setLayout(null);

        // Add topCardLabel to the layered pane and set its bounds
        layeredPane.add(topCardLabel, 2);
        topCardLabel.setBounds(500, 0, 200, 375);

        // Add topCardNameLabel to the layered pane and set its bounds
        layeredPane.add(topCardNameLabel, 1);
        topCardNameLabel.setBounds(530, 0, 200, 30); // Adjust the position based on your layout

        // Add the layered pane to the currTurnPanel
        currTurnPanel.add(layeredPane, BorderLayout.CENTER);

        // make a panel to display current player's hand
        handPanel = new JPanel();
        handPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 10, 10));

        // create scroll pane and have it hold the hand panel to allow user to scroll through cards
        JScrollPane scrollPane = new JScrollPane(handPanel);
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);

        // create button to draw a card and to go to next turn
        drawCard = new JButton("Draw card");
        drawCard.setFont(new Font("Dialog", Font.PLAIN, 18));
        drawCard.setActionCommand(DRAW_CMD);
        drawCard.addActionListener(controller);
        JButton nextTurn = new JButton(("Next turn"));
        nextTurn.setFont(new Font("Dialog", Font.PLAIN, 18));
        nextTurn.setActionCommand(NEXT_CMD);
        nextTurn.addActionListener(controller);

        // create a panel for draw card and next turn buttons
        buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(2,1));
        buttonPanel.add(drawCard);
        buttonPanel.add(nextTurn);

        // create a panel to hold the cards and the buttons
        JPanel controlPanel = new JPanel();
        controlPanel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;

        scrollPane.setPreferredSize(new Dimension(300, 340));
        gbc.weightx = 1.0; // Hand panel takes up most of the space
        gbc.weighty = 1.0;
        controlPanel.add(scrollPane, gbc);

        gbc.weightx = 0.2; // Button panel takes up less space
        gbc.weighty = 0.2;
        controlPanel.add(buttonPanel, gbc);

        // create a status area to hold the status
        statusArea = new JTextArea(1,30);
        statusArea.setEditable(false);
        statusArea.setFont(new Font("Arial", Font.PLAIN, 16));
        statusArea.setWrapStyleWord(true);
        statusArea.setLineWrap(true);
        JScrollPane statusScrollPanel = new JScrollPane(statusArea);

        // create an EmptyBorder with 10 pixels of margin on all sides
        EmptyBorder marginBorder = new EmptyBorder(10, 0, 10, 10);
        statusScrollPanel.setBorder(marginBorder);

        // create current colour area to hold the current colour
        JLabel colourLabel = new JLabel(("Current colour: "));
        currentColourPanel = new JPanel();
        currentColourPanel.setPreferredSize(new Dimension(100,100));
        currentColourPanel.setBackground(Color.WHITE);
        JPanel colourPanel = new JPanel(new BorderLayout());
        colourPanel.setBorder(marginBorder);
        colourPanel.add(colourLabel, BorderLayout.WEST);
        colourPanel.add(currentColourPanel, BorderLayout.CENTER);



        JLabel label = new JLabel("GAME STATUS: ");
        JPanel statusPanel = new JPanel(new BorderLayout());
        statusPanel.add(label, BorderLayout.NORTH);
        statusPanel.add(statusScrollPanel, BorderLayout.CENTER);
        statusPanel.add(colourPanel, BorderLayout.SOUTH);


        this.add(statusPanel, BorderLayout.EAST);
        this.add(currTurnPanel, BorderLayout.CENTER);
        this.add(controlPanel, BorderLayout.SOUTH);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setExtendedState(JFrame.MAXIMIZED_BOTH);
        this.setVisible(true);

        ActionEvent startEvent = new ActionEvent(this, ActionEvent.ACTION_PERFORMED, START_CMD);
        controller.actionPerformed(startEvent);
    }

    /**
     * Handles a UnoFlipEvent object by updating the view.
     *
     * @param e The event to handle
     */
    @Override
    public void handleUnoFlipStatusUpdate(UnoFlipEvent e) {
        if(e.getTurnFinished()){
            drawCard.setEnabled(false);
        } else {
            drawCard.setEnabled(true);
        }
        // clear status area
        statusArea.setText("");

        // Change the currentColourPanel to match the current colour of the game
        switch (e.getCurrColour()){
            case RED -> currentColourPanel.setBackground(Color.RED);
            case BLUE -> currentColourPanel.setBackground(Color.BLUE);
            case YELLOW -> currentColourPanel.setBackground(Color.YELLOW);
            case GREEN -> currentColourPanel.setBackground(Color.GREEN);
            case ORANGE -> currentColourPanel.setBackground(Color.ORANGE);
            case PINK -> currentColourPanel.setBackground(Color.PINK);
            case PURPLE -> currentColourPanel.setBackground(Color.decode("#800080"));
            case TEAL -> currentColourPanel.setBackground(Color.decode("#008080"));
        }

        // check wild to select colour
        if(e.getStatus().equals(Card.Colour.RED.toString()) || e.getStatus().equals(Card.Colour.BLUE.toString()) || e.getStatus().equals(Card.Colour.YELLOW.toString()) || e.getStatus().equals(Card.Colour.GREEN.toString())){
            statusArea.append("\nSelected Colour: " + e.getStatus());
        } else if (e.getStatus().startsWith("WINNER:")) {

            JOptionPane.showMessageDialog(this, e.getStatus(), "WINNER WINNER CHICKEN DINNER", JOptionPane.WARNING_MESSAGE);
            //show player scores
            for(int i =0; i < e.getPlayersScores().size(); i ++){
                statusArea.append(e.getPlayersScores().get(i)+ "\n");
            }
            //disable buttons
            drawCard.setEnabled(false);
            handPanel.removeAll();
            buttonPanel.removeAll();
            this.repaint();

        } else if (e.getStatus().equals("WILD") || e.getStatus().equals("WILD_DRAW_2")){
            statusArea.append("\nSelecting Colour...");
        } else {
            statusArea.append(e.getStatus());
        }

        //display cards since there are still cards in current player's hand
        if( !e.getStatus().startsWith("WINNER:")) {
            // update the hand panel with the new hand's cards
            handPanel.removeAll();  // remove current hand, about to replace with new one
            String currHand = e.getCurrHand();
            String[] currHandArray = currHand.split(" ");


            if(!(currHandArray[0] == "")){
                for (int i = 0; i < currHandArray.length; i++) {

                    JButton newCard = new JButton();
                    newCard.setPreferredSize(new Dimension(200, 300));
                    newCard.setIcon(new ImageIcon(getClass().getResource("images/" + currHandArray[i] + ".png")));
                    newCard.setActionCommand(Integer.toString(i));  // each card's action command is based on their hand index
                    newCard.addActionListener(controller);
                    if (e.getTurnFinished()) {
                        newCard.setEnabled(false);
                    }
                    handPanel.add(newCard);
                }
            }



            this.repaint();  // prevent visual bug by resetting the frame

            // update the top card
            topCardLabel.setIcon(new ImageIcon(getClass().getResource("images/" + e.getTopCard() + ".png")));
            topCardNameLabel.setText(e.getTopCard());
            topCardNameLabel.setFont(new Font("Dialog", Font.PLAIN, 18));

            // update the current player
            currPlayerLabel.setText("Current player: " + e.getCurrPlayerName());
            currPlayerLabel.setFont(new Font("Dialog", Font.PLAIN, 18));
        }

    }

    public static void main(String[] args) {
        new UnoFlipViewFrame();
    }
}
