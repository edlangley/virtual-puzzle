import java.awt.*;
import java.awt.event.*;

class BaseDialog extends Dialog
{
    Panel top = new Panel();
    Panel main = new Panel();
    Panel bottom = new Panel();
    
    Button ok = new Button("OK");
    Button cancel = new Button("Cancel");
    Button back = new Button("Back to menu");
    
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
