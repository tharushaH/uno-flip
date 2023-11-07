import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class UnoFlipController implements ActionListener {
    private UnoFlipModel model;

    public UnoFlipController(UnoFlipModel model){
        this.model = model;
    }


    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getActionCommand().equals("draw")){
            model.playTurn(-1);
        }
        if(e.getActionCommand().equals("next")) {
            model.nextTurn();
        }

        try{
            int index = Integer.parseInt(e.getActionCommand());
            if(model.checkValid(Integer.parseInt(e.getActionCommand()))){
                model.playTurn(Integer.parseInt(e.getActionCommand()));
            }
        } catch (NumberFormatException err){
            System.out.println("Invalid command: " + e.getActionCommand());
        }


    }
}
