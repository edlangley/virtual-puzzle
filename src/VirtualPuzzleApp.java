import java.awt.*;
import java.awt.event.*;
import java.io.IOException;

class VirtualPuzzleApp extends Frame
    implements ActionListener, MouseListener, MouseMotionListener, KeyListener
{
    String usersFName = "data/users.dat";
    String puzzlesFName = "data/puzzles.dat";
    
    UsersFileRec currentUserRec = null;
    int currentUserIx;
    PuzzlesFileRec currentPuzzleRec = null;
    
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
        mainFrame.setExtendedState(MAXIMIZED_BOTH);
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
        puzzleChooseDialog.loadPuzzleList(puzzlesFName, currentUserRec);
        puzzleChooseDialog.show();
    }
    
    public void showManagePuzzlesDialog()
    {
        puzzlesManageDialog.show();
    }
    
    public void showWinScreen(int time, int numMoves)
    {
        final BaseDialog winDialog = new BaseDialog(this);
        winDialog.setSize(400, 120);
        winDialog.setTitle("Congratulations!!");
        
        winDialog.top.setLayout(new FlowLayout(FlowLayout.CENTER));
        winDialog.top.add(new Label("Well done, you won that Puzzle in "+time+" seconds!"));
        winDialog.main.setLayout(new FlowLayout(FlowLayout.CENTER));
        winDialog.main.add(new Label("with "+numMoves+" moves"));
        
        Button okButton = new Button ("OK");
        okButton.addActionListener ( new ActionListener()
            {
                public void actionPerformed( ActionEvent e )
                {
                    winDialog.setVisible(false);
                    showMainDialog();
                }
            });
        winDialog.bottom.setLayout(new FlowLayout(FlowLayout.CENTER));
        winDialog.bottom.add(okButton);
        
        winDialog.add(winDialog.top,BorderLayout.NORTH);
        winDialog.add(winDialog.main,BorderLayout.CENTER);
        winDialog.add(winDialog.bottom,BorderLayout.SOUTH);
        winDialog.setVisible(true);
    }
    
    public void loadUser(int userIx, UsersFileRec userRec)
    {
        currentUserRec = userRec;
        currentUserIx = userIx;
        mainDialog.loadUserRec(currentUserRec);
    }
    
    public boolean loadPuzzle(PuzzlesFileRec puzzleRec)
    {
        if(puzzle1.load(puzzleRec.imgFileName,
                        puzzleRec.numSegmentsX, puzzleRec.numSegmentsY,
                        picOffsetX, picOffsetY,
                        maxPuzzleScaledDimensionPx) == false)
        {
            showMessageDialog("Unable to load puzzle image file " + puzzleRec.imgFileName);
            return false;
        }
        currentPuzzleRec = puzzleRec;
        
        return true;
    }
    
    public void showMessageDialog(String message)
    {
        final Dialog messageDialog = new Dialog(this, "Alert", true);
        
        messageDialog.setLayout(new FlowLayout());

        Button okButton = new Button ("OK");
        okButton.addActionListener ( new ActionListener()
        {
            public void actionPerformed( ActionEvent e )
            {
                // Hide dialog
                messageDialog.setVisible(false);
            }
        });

        messageDialog.add(new Label (message));
        messageDialog.add(okButton);

        messageDialog.pack();
        messageDialog.setVisible(true);
    }
    
    public void updateUserScore(int newTimeTaken, int newNumMoves)
    {
        boolean scoreWasUpdated = false;
        
        for(int scoreRecIx = 0; scoreRecIx < currentUserRec.puzzleScores.size(); scoreRecIx++)
        {
            if(currentUserRec.puzzleScores.get(scoreRecIx).puzzleName.equals(currentPuzzleRec.puzzleName))
            {
                UserPuzzleScoreRec scoreRec = currentUserRec.puzzleScores.get(scoreRecIx);
                if((newTimeTaken * newNumMoves) < (scoreRec.timeTaken * scoreRec.numMoves))
                {
                    scoreRec.timeTaken = newTimeTaken;
                    scoreRec.numMoves = newNumMoves;
                }
                currentUserRec.puzzleScores.set(scoreRecIx, scoreRec);
                scoreWasUpdated = true;
            }
        }
        
        if(scoreWasUpdated == false)
        {
            UserPuzzleScoreRec scoreRec = new UserPuzzleScoreRec();
            scoreRec.puzzleName = currentPuzzleRec.puzzleName;
            scoreRec.timeTaken = newTimeTaken;
            scoreRec.numMoves = newNumMoves;
            currentUserRec.puzzleScores.add(scoreRec);
            
            currentUserRec.numPuzzlesDone++;
        }
        
        if((newTimeTaken * newNumMoves) > currentUserRec.diffLevel)
        {
            currentUserRec.diffLevel = (newTimeTaken * newNumMoves);
        }

        FileHandler<UsersFileRec> usersFileHandler = new FileHandler(usersFName);
        usersFileHandler.updateRec(currentUserIx, currentUserRec);
        
        mainDialog.loadUserRec(currentUserRec);
    }
    
    public void updateScoreRecords(String oldPuzzleName, PuzzlesFileRec puzzleRec)
    {
        FileHandler<UsersFileRec> usersFileHandler = new FileHandler(usersFName);
        for(int userIx = 0; userIx < usersFileHandler.numRecords(); userIx++)
        {
            UsersFileRec userRec = usersFileHandler.readRec(userIx);
            for(int scoreRecIx = 0; scoreRecIx < userRec.puzzleScores.size(); scoreRecIx++)
            {
                if(userRec.puzzleScores.get(scoreRecIx).puzzleName.equals(oldPuzzleName))
                {
                    UserPuzzleScoreRec newScoreRec = userRec.puzzleScores.get(scoreRecIx);
                    newScoreRec.puzzleName = new String(puzzleRec.puzzleName);
                    userRec.puzzleScores.set(scoreRecIx, newScoreRec);
                    usersFileHandler.updateRec(userIx, userRec);
                }
            }
        }
        
        currentUserRec = usersFileHandler.readRec(currentUserIx);
    }
    
    public void removeUnneededScoreRecords(String oldPuzzleName)
    {
        FileHandler<UsersFileRec> usersFileHandler = new FileHandler(usersFName);
        for(int userIx = 0; userIx < usersFileHandler.numRecords(); userIx++)
        {
            UsersFileRec userRec = usersFileHandler.readRec(userIx);
            for(int scoreRecIx = 0; scoreRecIx < userRec.puzzleScores.size(); scoreRecIx++)
            {
                if(userRec.puzzleScores.get(scoreRecIx).puzzleName.equals(oldPuzzleName))
                {
                    userRec.puzzleScores.remove(scoreRecIx);
                    usersFileHandler.updateRec(userIx, userRec);
                }
            }
        }
        
        currentUserRec = usersFileHandler.readRec(currentUserIx);
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

