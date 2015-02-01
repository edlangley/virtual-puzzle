import java.awt.*;
import java.awt.event.*;
import java.io.IOException;

/*
 * TODO:
 * Rename:
 * mainMenuDialog -> startingOptionsDialog
 * doPuzzleDialog -> puzzleChooseDialog
 * nameMenuDialog -> userChooseDialog
 * 
 */



class VirtualPuzzle extends Frame
    implements ActionListener, MouseListener, MouseMotionListener, KeyListener
{
    // Hard coded for now:
    String userProgFName = "data/ed.user";
    String userImageRecFName = "None";
    String picIndexFName = "None";
    
    int numsegs = 3;
    int picOffsetX = 10;
    int picOffsetY = 10;
    int maxPuzzleScaledDimensionPx = 800;
    String picFilePath = "data/the_park.jpg";
    //String picFilePath = "data/Testpic2.jpg";
    
    MenuBar menuBar = new MenuBar();
    Menu fileMenu = new Menu("File");
    
    puzzle puzzle1;
    mainMenuDialog mainMenu;
    doPuzzleDialog puzzleDialog;
    
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
        
        setLayout(new BorderLayout());
        
        MenuItem[] file = { new MenuItem("Back to Logon"),
                            new MenuItem("Back to Menu"),
                            new MenuItem("Pause") };
        for(int i = 0;i<file.length;i++)
            fileMenu.add(file[i]);
        fileMenu.addSeparator();
        fileMenu.add(new MenuItem("Exit"));
        
        menuBar.add(fileMenu);
        setMenuBar(menuBar);
        
        mainMenu = new mainMenuDialog(this, userProgFName, userImageRecFName, picIndexFName);
        puzzleDialog = new doPuzzleDialog(this, userImageRecFName);
        
        puzzle1 = new puzzle(this);
        puzzle1.addMouseListener(this);
        puzzle1.addKeyListener(this);
        add(puzzle1, BorderLayout.CENTER);
    }
    
    public void showMainMenu()
    {
        mainMenu.show();
    }
    
    public void showPuzzleMenu()
    {
        //puzzleDialog.show();
        
        // For interim testing:
        loadPuzzle(picFilePath);
    }
    
    public void loadPuzzle(String picFilePath)
    {
        puzzle1.load(picFilePath, numsegs, picOffsetX,
                picOffsetY, maxPuzzleScaledDimensionPx);
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

