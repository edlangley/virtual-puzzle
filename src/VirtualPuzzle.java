import java.awt.*;
import java.awt.event.*;
import java.io.IOException;

class VirtualPuzzle extends Frame
	implements ActionListener, MouseListener, MouseMotionListener, KeyListener
{
	// Hard coded for now:
	String userProgFName = "data/ed.user";
	String userImageRecFName = "None";
	String picIndexFName = "None";
	
	int numsegs = 16;
	int picOffsetX = 0;
	int picOffsetY = 0;
	int maxPuzzleScaledDimensionPx = 800;
	String picFilePath = "data/atlanta_downtown_smaller.jpg";
	
	
	Menu f = new Menu("File");
	
	puzzle puzzle1;
	mainMenuDialog mainMenu;
	
	public static void main(String args[]) throws IOException
	{
		VirtualPuzzle mainFrame = new VirtualPuzzle();
		mainFrame.setSize(800, 600);
		mainFrame.setTitle("Virtual Puzzle");
		mainFrame.setVisible(true);
		
		mainFrame.showMainMenu();
	}
	
	public VirtualPuzzle() throws IOException
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
		
		mainMenu = new mainMenuDialog(this, userProgFName, userImageRecFName, picIndexFName);
	}
	
	public void showMainMenu()
	{
		mainMenu.show();
	}
	
	public void loadPuzzle(String picFilePath)
	{
	//	puzzle Puzzle1 = new puzzle(numsegs, int picOffsetX,
	//		int picOffsetY, int limit, String picFilePath, this);
		
		puzzle Puzzle1 = new puzzle(this, numsegs, picOffsetX,
				picOffsetY, maxPuzzleScaledDimensionPx, picFilePath);
				
	}
	
	public void showWinScreen(int time, int numMoves)
	{
		mainMenu.showWinScreen(time, numMoves);
	}
	
	public void actionPerformed(ActionEvent e)
	{
	}

	public void mouseClicked(MouseEvent e)
	{
		//System.out.println("got to mouseclicked: x: "+e.getX()+" y: "+e.getY());
		if(puzzle1.ready)
		{
			puzzle1.findSegMouse(e.getX(),e.getY());
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
			puzzle1.findSegKeyboard(e);
		}
	}
	
	public void keyReleased(KeyEvent e)
	{
	}
}

