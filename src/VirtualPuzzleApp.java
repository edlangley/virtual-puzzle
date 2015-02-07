import java.awt.*;
import java.awt.event.*;
import java.io.IOException;

class VirtualPuzzleApp extends Frame
    implements ActionListener, MouseListener, MouseMotionListener, KeyListener
{
    // Hard coded for now:
    String userProgFName = "data/ed.user";
    String userImageRecFName = "None";
    String picIndexFName = "data/puzzles.dat";
    
    int numsegs = 3;
    int picOffsetX = 10;
    int picOffsetY = 10;
    int maxPuzzleScaledDimensionPx = 800;
    String picFilePath = "data/the_park.jpg";
    //String picFilePath = "data/Testpic2.jpg";
    
    MenuBar menuBar = new MenuBar();
    Menu fileMenu = new Menu("File");
    
    Puzzle puzzle1;
    MainOptionsDialog mainDialog;
    ChoosePuzzleDialog puzzleChooseDialog;
    ManagePuzzlesDialog puzzlesManageDialog;
    
    public static void main(String args[]) throws IOException
    {
        VirtualPuzzleApp mainFrame = new VirtualPuzzleApp();
        mainFrame.setSize(800, 600);
        mainFrame.setTitle("Virtual Puzzle");
        mainFrame.setVisible(true);
        
        mainFrame.showMainDialog();
    }
    
    public VirtualPuzzleApp() throws IOException
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
        
        mainDialog = new MainOptionsDialog(this, userProgFName, userImageRecFName, picIndexFName);
        puzzleChooseDialog = new ChoosePuzzleDialog(this, picIndexFName);
        puzzlesManageDialog = new ManagePuzzlesDialog(this, picIndexFName);
        
        puzzle1 = new Puzzle(this);
        puzzle1.addMouseListener(this);
        puzzle1.addKeyListener(this);
        add(puzzle1, BorderLayout.CENTER);
    }
    
    public void showMainDialog()
    {
        mainDialog.show();
    }
    
    public void showChoosePuzzleDialog()
    {
        puzzleChooseDialog.show();
        
        // For interim testing:
        //loadPuzzle(picFilePath);
    }
    
    public void showLoadPicsDialog()
    {
        puzzlesManageDialog.show();
    }
    
    public void loadPuzzle(String picFilePath)
    {
        puzzle1.load(picFilePath, numsegs, picOffsetX,
                picOffsetY, maxPuzzleScaledDimensionPx);
    }
    
    public void showWinScreen(int time, int numMoves)
    {
        mainDialog.showWinScreen(time, numMoves);
    }
    
    public void actionPerformed(ActionEvent e)
    {
    }

    public void mouseClicked(MouseEvent e)
    {
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
        if(puzzle1.ready)
        {
            puzzle1.findSegKeyboard(e);
        }
    }
    
    public void keyReleased(KeyEvent e)
    {
    }
}

