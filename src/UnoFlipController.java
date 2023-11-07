import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class UnoFlipController implements ActionListener {
    private UnoFlipModel model;

    public UnoFlipController(UnoFlipModel model){
        this.model = model;
    }


    @Override
    public void actionPerformed(ActionEvent e) {
        try{
            int index = Integer.parseInt(e.getActionCommand());
            if(model.checkValid(Integer.parseInt(e.getActionCommand()))){
                model.playTurn(Integer.parseInt(e.getActionCommand()));
            }
        } catch (NumberFormatException err){
            if(e.getActionCommand().equals("draw")){
                model.playTurn(-1);
            }
        }


    }
}
