import java.awt.*;
import java.awt.event.*;

class BaseDialog extends Dialog
{
    Panel top = new Panel();
    Panel main = new Panel();
    Panel bottom = new Panel();
    
    Button backButton = new Button("Back");
    
    Frame parentFrame;
    
    public BaseDialog(Frame parent)
    {
        super(parent);
        parentFrame = parent;
        
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
    
    public void moveToCenter()
    {
        Rectangle rect = parentFrame.getBounds();

        double width = getBounds().getWidth();
        double height = getBounds().getHeight();

        double x = rect.getCenterX() - (width / 2);
        double y = rect.getCenterY() - (height/ 2);

        /* Could be new Point(x, y) */
        Point leftCorner = new Point();
        leftCorner.setLocation(x, y);

        setLocation(leftCorner);
    }
    
    @Override
    public void setVisible(final boolean visible)
    {
        moveToCenter();
        super.setVisible(visible);
    }
}
