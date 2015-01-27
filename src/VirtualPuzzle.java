import java.awt.*;
import java.awt.event.*;

class VirtualPuzzle extends Frame
	implements ActionListener, MouseListener, MouseMotionListener, KeyListener
{
	Menu f = new Menu("File");
	
	mainMenuDialog mainMenu;
	
	public static void main(String args[])
	{
		VirtualPuzzle mainFrame = new VirtualPuzzle();
		mainFrame.setSize(800, 600);
		mainFrame.setTitle("Virtual Puzzle for Children");
		mainFrame.setVisible(true);
	}
	
	public VirtualPuzzle()
	{
		addWindowListener(new WindowAdapter()
		{
			public void windowClosing(WindowEvent e)
			{
				dispose();
				System.exit(0);
			}
		});
		MenuItem[] file = { new MenuItem("Back to Logon"),
							new MenuItem("Back to Menu"),
							new MenuItem("Pause")};
		for(int i = 0;i<file.length;i++)
			f.add(file[i]);
		f.addSeparator();
		f.add(new MenuItem("Exit"));
		
		mainMenu = new mainMenuDialog();
	}
	public void actionPerformed(ActionEvent e)
	{
	}

	public void mouseClicked(MouseEvent e)
	{
		//System.out.println("got to mouseclicked: x: "+e.getX()+" y: "+e.getY());
		if(puzzle1.ready)
		{
			mainMenu.puzzle1.findSegMouse(e.getX(),e.getY());
		}
	}

	public void mouseReleased(MouseEvent e)
	{
	}

	public void mouseEntered(MouseEvent e)
	{
	}

	public void mouseExited(MouseEvent e)
	{
	}

	public void mousePressed(MouseEvent e)
	{
	}

	public void mouseDragged(MouseEvent e)
	{
	}

	public void mouseMoved(MouseEvent e)
	{
	}
	
	public void keyTyped(KeyEvent e)
	{
	}
	
	public void keyPressed(KeyEvent e)
	{
		//System.out.println("Key pressed");
		if(puzzle1.ready)
		{
			mainMenu.puzzle1.findSegKeyboard(e);
		}
	}
	
	public void keyReleased(KeyEvent e)
	{
	}
}

