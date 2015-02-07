import java.awt.*;
import java.awt.event.*;

class BaseDialog extends Dialog
{
    Panel top = new Panel();
    Panel main = new Panel();
    Panel bottom = new Panel();
    
    Button backButton = new Button("Back");
    
    public BaseDialog(Frame parent)
    {
        super(parent);
        addWindowListener(new WindowAdapter()
            {
                public void windowClosing(WindowEvent e)
                {
                    dispose();
                
                }
            });
        setSize(480,200);
        
        setModal(true);
        setResizable(false);
        setLayout(new BorderLayout());
    }
}
