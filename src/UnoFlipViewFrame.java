import javax.swing.*;
import java.awt.*;

public class UnoFlipViewFrame extends JFrame implements UnoFlipView {
    private JPanel handPanel;
    private JPanel currTurnPanel;
    private JLabel topCardLabel;
    private JLabel currPlayerLabel;

    private static final ImageIcon YELLOW_REVERSE = new ImageIcon("src\\images\\yellow_reverse.png");

    public UnoFlipViewFrame() {
        super("Uno Flip!");
        this.setLayout(new BorderLayout(20,50));

        // make a panel to display info on the current turn
        currTurnPanel = new JPanel();
        currTurnPanel.setLayout(new BorderLayout(30,30));

        // add sample player name to the current turn panel
        currPlayerLabel = new JLabel("Hubert");
        currTurnPanel.add(currPlayerLabel, BorderLayout.WEST);

        // add sample top card to the current turn panel
        topCardLabel = new JLabel(YELLOW_REVERSE);
        currTurnPanel.add(topCardLabel, BorderLayout.CENTER);

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

        this.add(currTurnPanel, BorderLayout.CENTER);
        this.add(scrollPane, BorderLayout.SOUTH);
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
