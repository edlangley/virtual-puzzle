import java.awt.*;
import java.awt.event.*;
import java.io.IOException;

class VirtualPuzzleApp extends Frame
    implements ActionListener, MouseListener, MouseMotionListener, KeyListener
{
    String usersFName = "data/users.dat";
    String puzzlesFName = "data/puzzles.dat";
    
    UsersFileRec currentUserRec = null;
    
 // Hard coded for now:
    int numsegs = 3;
    int picOffsetX = 10;
    int picOffsetY = 10;
    int maxPuzzleScaledDimensionPx = 800;
    
    MenuBar menuBar = new MenuBar();
    Menu fileMenu = new Menu("File");
    
    Puzzle puzzle1;
    ChooseUserDialog userChooseDialog;
    MainOptionsDialog mainDialog;
    ChoosePuzzleDialog puzzleChooseDialog;
    ManagePuzzlesDialog puzzlesManageDialog;
    
    public static void main(String args[]) throws IOException
    {
        VirtualPuzzleApp mainFrame = new VirtualPuzzleApp();
        mainFrame.setSize(800, 600);
        mainFrame.setTitle("Virtual Puzzle");
        mainFrame.setVisible(true);
        
        mainFrame.showChooseUserDialog();
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
        
        userChooseDialog = new ChooseUserDialog(this, usersFName);
        mainDialog = new MainOptionsDialog(this);
        puzzleChooseDialog = new ChoosePuzzleDialog(this, puzzlesFName);
        puzzlesManageDialog = new ManagePuzzlesDialog(this, puzzlesFName);
        
        puzzle1 = new Puzzle(this);
        puzzle1.addMouseListener(this);
        puzzle1.addKeyListener(this);
        add(puzzle1, BorderLayout.CENTER);
    }
    
    public void showChooseUserDialog()
    {
        userChooseDialog.show();
    }
    
    public void showMainDialog()
    {
        mainDialog.show();
    }
    
    public void showChoosePuzzleDialog()
    {
        puzzleChooseDialog.loadPuzzleList(puzzlesFName);
        puzzleChooseDialog.show();
    }
    
    public void showManagePuzzlesDialog()
    {
        puzzlesManageDialog.show();
    }
    
    public void showWinScreen(int time, int numMoves)
    {
        mainDialog.showWinScreen(time, numMoves);
    }
    
    public void loadUser(UsersFileRec userRec)
    {
        currentUserRec = userRec;
        mainDialog.loadUserRec(currentUserRec);
    }
    
    public void loadPuzzle(PuzzlesFileRec puzzleRec)
    {
        puzzle1.load(puzzleRec.imgFileName,
                     puzzleRec.numSegmentsX, puzzleRec.numSegmentsY,
                     picOffsetX, picOffsetY,
                     maxPuzzleScaledDimensionPx);
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

