import java.awt.*;
import java.awt.event.*;

class ChooseDifficultyDialog extends BaseDialog implements ActionListener
    {
        //load pics button
        Button browse = new Button("Browse...");
        List namelist = new List();
        
        public ChooseDifficultyDialog(Frame parent, MainOptionsDialog main)
        {
            super(parent);
            
        }
        
        public void actionPerformed(ActionEvent e)
        {        
            if(e.getActionCommand() == "cancel")
            {
                dispose();
                main.show();
            }
            if(e.getActionCommand() == "loadPics")
            {
                hide();
                //loadPicsDialog1.show();
                
            }
        }
    }
