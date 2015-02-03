import java.awt.*;
import java.awt.event.*;
import java.io.IOException;

class MainOptionsDialog extends BaseDialog implements ActionListener
{
    //main menu
    Button doPuzzle = new Button("Do a Puzzle");
    Button loadPics = new Button("Import Pictures");
    Button chooseDiff = new Button("Choose difficulty");
    Button quit = new Button("Quit");
    Panel p1 = new Panel();
    
    Label lab1 = new Label("Hello .......... ");
    Label lab2 = new Label("Puzzles you have completed so far: ");
    Label lab3 = new Label("You are on difficulty level: ");
    
    Label puzzlesComp = new Label("5");
    Label diffLevel = new Label("6");
    
    //main record for user logged onto system, initialised in loadprogRec()
    UserProgressFileRec mainProgRec;
    
    //menu global vars for the Puzzle:
//        int numsegs, picOffsetX, picOffsetY, sizeLimit;
    
    // other menus controlled by main:
//        ChooseUserDialog nameMenu ;
    VirtualPuzzleApp parentVPuzzle;
    
    public MainOptionsDialog(VirtualPuzzleApp parent, String userProgFName, String userImageRecFName, String picIndexFName) throws IOException
    {
        super(parent);
//            nameMenu = new ChooseUserDialog(parent, this, userProgFName);
        parentVPuzzle = parent;
        
        doPuzzle.addActionListener(this);
        loadPics.addActionListener(this);
        chooseDiff.addActionListener(this);
        quit.addActionListener(this);
        
        top.add(lab1);
        
        GridLayout mainGrid = new GridLayout(3,2);
        mainGrid.setVgap(20);
        main.setLayout(mainGrid);
        //p1.setLayout(new FlowLayout());
        //p1.add(puzzlesComp)
        //main.add(lab1);
        main.add(lab2);
        //main.add(p1);
        main.add(puzzlesComp);
        main.add(lab3);
        main.add(diffLevel);
        
        //bottom.setLayout(new FlowLayout(FlowLayout.RIGHT));
        bottom.setLayout(new FlowLayout());
        bottom.add(doPuzzle);
        bottom.add(loadPics);
        bottom.add(chooseDiff);
        bottom.add(quit);
        
        add(top,BorderLayout.NORTH);
        add(main,BorderLayout.CENTER);
        add(bottom,BorderLayout.SOUTH);
        
    }
/*        
        public void loadPuzzle(String picFilePath)
        {
        //    Puzzle Puzzle1 = new Puzzle(numsegs, int picOffsetX,
        //        int picOffsetY, int limit, String picFilePath, this);
            Puzzle Puzzle1 = new Puzzle(numsegs, 0,
                    0, 800, picFilePath, (Frame) this.getParent());
            hide();
        }
*/        
    public void loadProgRec(UserProgressFileRec tempProgRec)
    {
        mainProgRec = tempProgRec;
        
    }
    
    public void showWinScreen(int time, int numMoves)
    {
        Dialog winDialog = new Dialog((Frame) this.getParent(), "congratualtions!!", true);
        winDialog.setSize(300, 200);
        winDialog.setLayout(new FlowLayout());
        winDialog.add(new Label("Well done, you won that Puzzle in "+time+" seconds!"));
        winDialog.add(new Label("with "+numMoves+" moves"));
        winDialog.show();
    }
    
    public void actionPerformed(ActionEvent e)
    {
        System.out.println("MainOptionsDialog actionPerformed command: " + e.getActionCommand());
        
        if(e.getActionCommand() == "Quit")
        {
            dispose();
            System.exit(0);
        }
        else if(e.getActionCommand() == "Do a Puzzle")
        {
            hide();
            parentVPuzzle.showPuzzleMenu();    
        }
        else if(e.getActionCommand() == "loadPics")
        {
            hide();
            //loadPicsDialog1.show();    
        }
    }
}