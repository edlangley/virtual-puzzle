import java.awt.*;
import java.awt.event.*;
import java.io.IOException;

class MainOptionsDialog extends BaseDialog implements ActionListener
{
    VirtualPuzzleApp parentVPuzzle;

    Button doPuzzle = new Button("Do a Puzzle");
    Button loadPics = new Button("Manage Puzzles");
    Button quit = new Button("Quit");
    Panel p1 = new Panel();
    
    Label lab1 = new Label("Hello .......... ");
    Label lab2 = new Label("Puzzles you have completed so far: ");
    Label lab3 = new Label("You are on difficulty level: ");
    
    Label puzzlesComp = new Label("5");
    Label diffLevel = new Label("6");
    
    //main record for user logged onto system, initialised in loadprogRec()
    UserProgressFileRec mainProgRec;

    
    public MainOptionsDialog(VirtualPuzzleApp parent, String userProgFName, String userImageRecFName, String picIndexFName) throws IOException
    {
        super(parent);
        parentVPuzzle = parent;
        
        doPuzzle.addActionListener(this);
        loadPics.addActionListener(this);
        quit.addActionListener(this);
        
        top.add(lab1);
        
        GridLayout mainGrid = new GridLayout(3,2);
        mainGrid.setVgap(20);
        main.setLayout(mainGrid);
        main.add(lab2);
        main.add(puzzlesComp);
        main.add(lab3);
        main.add(diffLevel);

        bottom.setLayout(new FlowLayout());
        bottom.add(doPuzzle);
        bottom.add(loadPics);
        bottom.add(quit);
        
        add(top,BorderLayout.NORTH);
        add(main,BorderLayout.CENTER);
        add(bottom,BorderLayout.SOUTH);
        
    }

    public void loadProgRec(UserProgressFileRec tempProgRec)
    {
        mainProgRec = tempProgRec;
        
    }

    public void showWinScreen(int time, int numMoves)
    {
        Dialog winDialog = new Dialog((Frame) this.getParent(), "Congratulations!!", true);
        winDialog.setSize(300, 200);
        winDialog.setLayout(new FlowLayout());
        winDialog.add(new Label("Well done, you won that Puzzle in "+time+" seconds!"));
        winDialog.add(new Label("with "+numMoves+" moves"));
        winDialog.show();
    }

    public void actionPerformed(ActionEvent e)
    {
        if(e.getActionCommand().equals("Quit"))
        {
            dispose();
            System.exit(0);
        }
        else if(e.getActionCommand().equals("Do a Puzzle"))
        {
            hide();
            parentVPuzzle.showChoosePuzzleDialog();
        }
        else if(e.getActionCommand().equals("Manage Puzzles"))
        {
            hide();
            parentVPuzzle.showLoadPicsDialog();
        }
    }
}
