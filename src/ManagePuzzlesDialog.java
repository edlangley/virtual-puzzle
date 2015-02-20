import java.awt.*;
import java.awt.event.*;
import java.io.IOException;

class ManagePuzzlesDialog extends BaseDialog implements ActionListener
{
    VirtualPuzzleApp parentVPuzzle;
    
    Label topLabel = new Label("Manage puzzles");
    Button addButton = new Button("Add");
    Button editButton = new Button("Edit");
    Button deleteButton = new Button("Delete");
    List puzzleList = new List();
    
    EditPuzzleDialog editDialog;
    
    FileHandler<PuzzlesFileRec> puzzlesFileHandler;

    public ManagePuzzlesDialog(VirtualPuzzleApp parent, String puzzlesFileName) throws IOException
    {
        super(parent);
        parentVPuzzle = parent;
        
        setSize(400, 400);
        setTitle("Manage Puzzles");
        addButton.addActionListener(this);
        editButton.addActionListener(this);
        deleteButton.addActionListener(this);
        backButton.addActionListener(this);
        
        top.add(topLabel);
        
        GridBagLayout gridbag = new GridBagLayout();
        GridBagConstraints constraints = new GridBagConstraints();
        main.setLayout(gridbag);
        
        constraints.insets = new Insets(5, 5, 5, 5);
        constraints.fill = GridBagConstraints.BOTH;
        constraints.gridheight = 4;
        constraints.weightx = 1.0;
        constraints.weighty = 1.0;
        gridbag.setConstraints(puzzleList, constraints);
        main.add(puzzleList);
        
        constraints.weightx = 0.0;
        constraints.weighty = 0.0;
        constraints.gridwidth = GridBagConstraints.REMAINDER; //end row
        constraints.gridheight = 1;
        gridbag.setConstraints(addButton, constraints);
        main.add(addButton);
        gridbag.setConstraints(editButton, constraints);
        main.add(editButton);
        gridbag.setConstraints(deleteButton, constraints);
        main.add(deleteButton);
        
        bottom.setLayout(new FlowLayout(FlowLayout.RIGHT));
        bottom.add(backButton);
        
        add(top,BorderLayout.NORTH);
        add(main,BorderLayout.CENTER);
        add(bottom,BorderLayout.SOUTH);
        
        editDialog = new EditPuzzleDialog(parent, this);
        
        
        puzzlesFileHandler = new FileHandler(puzzlesFileName);
        for(int i = 0; i < puzzlesFileHandler.numRecords(); i++)
        {
            puzzleList.addItem(puzzlesFileHandler.readRec(i).puzzleName);
        }
    }

    public void addNewPuzzle(PuzzlesFileRec newPuzzleRec)
    {
        puzzlesFileHandler.addRec(newPuzzleRec);
        puzzleList.addItem(newPuzzleRec.puzzleName);
    }

    public void updatePuzzle(int recIx, PuzzlesFileRec puzzleRec)
    {
        puzzlesFileHandler.updateRec(recIx, puzzleRec);
        puzzleList.replaceItem(puzzleRec.puzzleName, recIx);
    }

    public void actionPerformed(ActionEvent e)
    {        
        if(e.getActionCommand().equals("Add"))
        {
            editDialog.show();
        }
        else if(e.getActionCommand().equals("Edit"))
        {
            // TODO: Set up button to be enabled only when a list item is selected
            //System.err.println(puzzleList.getSelectedIndex());
            
            if(puzzleList.getSelectedIndex() >= 0)
            {
                editDialog.loadPuzzleRecord(puzzleList.getSelectedIndex(), puzzlesFileHandler.readRec(puzzleList.getSelectedIndex()));
                editDialog.show();
            }
        }
        else if(e.getActionCommand().equals("Back"))
        {
            hide();
            parentVPuzzle.showMainDialog();
        }
    }
}
