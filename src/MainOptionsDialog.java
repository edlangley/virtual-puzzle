import java.awt.*;
import java.awt.event.*;
import java.io.IOException;

class MainOptionsDialog extends BaseDialog implements ActionListener
{
    VirtualPuzzleApp parentVPuzzle;

    Button doPuzzleButton = new Button("Do a Puzzle");
    Button managePuzzlesButton = new Button("Manage Puzzles");
    Button changeUserButton = new Button("Change User");
    Button quitButton = new Button("Quit");
    
    Label helloLabel = new Label("Hello .......... ");
    Label puzzCompTextLabel = new Label("Puzzles you have completed so far: ");
    Label diffLvlTextLabel = new Label("You are on difficulty level: ");
    
    Label puzzCompValueLabel = new Label("5");
    Label diffLvlValueLabel = new Label("6");
    
    //main record for user logged onto system, initialised in loadprogRec()
    UsersFileRec userRec;

    
    public MainOptionsDialog(VirtualPuzzleApp parent) throws IOException
    {
        super(parent);
        parentVPuzzle = parent;
        
        doPuzzleButton.addActionListener(this);
        managePuzzlesButton.addActionListener(this);
        changeUserButton.addActionListener(this);
        quitButton.addActionListener(this);
        
        top.add(helloLabel);
        
        GridLayout mainGrid = new GridLayout(3,2);
        mainGrid.setVgap(20);
        main.setLayout(mainGrid);
        main.add(puzzCompTextLabel);
        main.add(puzzCompValueLabel);
        main.add(diffLvlTextLabel);
        main.add(diffLvlValueLabel);

        bottom.setLayout(new FlowLayout());
        bottom.add(doPuzzleButton);
        bottom.add(managePuzzlesButton);
        bottom.add(changeUserButton);
        bottom.add(quitButton);
        
        add(top,BorderLayout.NORTH);
        add(main,BorderLayout.CENTER);
        add(bottom,BorderLayout.SOUTH);
        
    }

    public void loadUserRec(UsersFileRec tempUserRec)
    {
        userRec = tempUserRec;
        
        helloLabel.setText("Hello " + userRec.fName + " " + userRec.lName);
        puzzCompValueLabel.setText(new Integer(userRec.numPuzzlesDone).toString());
        diffLvlValueLabel.setText(new Integer(userRec.diffLevel).toString());
    }

    public void actionPerformed(ActionEvent e)
    {
        if(e.getActionCommand().equals("Do a Puzzle"))
        {
            setVisible(false);
            parentVPuzzle.showChoosePuzzleDialog();
        }
        else if(e.getActionCommand().equals("Manage Puzzles"))
        {
            setVisible(false);
            parentVPuzzle.showManagePuzzlesDialog();
        }
        else if(e.getActionCommand().equals("Change User"))
        {
            setVisible(false);
            parentVPuzzle.showChooseUserDialog();
        }
        else if(e.getActionCommand().equals("Quit"))
        {
            dispose();
            System.exit(0);
        }
    }
}
