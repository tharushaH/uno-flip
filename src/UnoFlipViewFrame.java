import javax.swing.*;
import java.awt.*;

public class UnoFlipViewFrame extends JFrame implements UnoFlipView {
    private JPanel handPanel;
    private JPanel currTurnPanel;
    private JLabel topCardLabel;
    private JLabel currPlayerLabel;
    private JLabel topCardNameLabel;
    private static final ImageIcon YELLOW_REVERSE = new ImageIcon("src\\images\\yellow_reverse.png");

    public UnoFlipViewFrame() {
        super("Uno Flip!");
        this.setLayout(new BorderLayout(20,50));

        // make a panel to display info on the current turn
        currTurnPanel = new JPanel();
        currTurnPanel.setLayout(new BorderLayout(30,30));

        // Create a layered pane to stack the top card label and text label
        JLayeredPane layeredPane = new JLayeredPane();

        // add sample player name to the current turn panel
        currPlayerLabel = new JLabel("Current player: Hubert");
        currPlayerLabel.setFont(new Font("Dialog", Font.PLAIN, 18));
        this.add(currPlayerLabel, BorderLayout.WEST);

        // add sample top card to the current turn panel
        topCardLabel = new JLabel(YELLOW_REVERSE);
        currTurnPanel.add(topCardLabel, BorderLayout.CENTER);

        // add sample top card name to the current turn panel
        topCardNameLabel = new JLabel("The top card");
        topCardNameLabel.setFont(new Font("Dialog", Font.PLAIN, 18));

        // make a panel to display current player's hand
        handPanel = new JPanel();
        handPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 10, 10));

        // create scroll pane and have it hold the hand panel to allow user to scroll through cards
        JScrollPane scrollPane = new JScrollPane(handPanel);
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);

        // add sample buttons to the hand panel
        for (int i = 0; i < 20; i++) {
            JButton card = new JButton();
            card.setPreferredSize(new Dimension(200,300));
            card.setIcon(YELLOW_REVERSE);
            handPanel.add(card);
        }

        // create button to draw a card and to go to next turn
        JButton drawCard = new JButton("Draw card");
        drawCard.setFont(new Font("Dialog", Font.PLAIN, 18));
        JButton nextTurn = new JButton(("Next turn"));
        nextTurn.setFont(new Font("Dialog", Font.PLAIN, 18));

        // create a panel for draw card and next turn buttons
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(2,1));
        buttonPanel.add(drawCard);
        buttonPanel.add(nextTurn);

        // create a panel to hold the cards and the buttons
        JPanel controlPanel = new JPanel();
        controlPanel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1.0; // Hand panel takes up most of the space
        gbc.weighty = 1.0;
        controlPanel.add(scrollPane, gbc);

        gbc.weightx = 0.2; // Button panel takes up less space
        gbc.weighty = 0.2;
        controlPanel.add(buttonPanel, gbc);

        this.add(currTurnPanel, BorderLayout.CENTER);
        this.add(controlPanel, BorderLayout.SOUTH);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setExtendedState(JFrame.MAXIMIZED_BOTH);
        this.setVisible(true);
    }

    /**
     * Handles a UnoFlipEvent object by updating the view.
     *
     * @param e
     */
    @Override
    public void handleUnoFlipStatusUpdate(UnoFlipEvent e) {
    }

    public static void main(String[] args) {
        new UnoFlipViewFrame();
    }
}
