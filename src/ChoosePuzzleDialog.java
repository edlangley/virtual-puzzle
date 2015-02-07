import java.awt.*;
import java.awt.event.*;
import java.io.IOException;

class ChoosePuzzleDialog extends BaseDialog implements ActionListener
{
    VirtualPuzzleApp parentVPuzzle;
    
    Label topLabel = new Label("Choose a puzzle");
    GridLayout mainGrid;
    List puzzleList = new List();
    String diffLevelString = "Current difficulty level available: ";
    Label diffLevelLabel = new Label(diffLevelString);
    Button goButton = new Button("Go");
    
    PicFileHandler picIndex;
    
    public ChoosePuzzleDialog(VirtualPuzzleApp parent, String picRecordFileName) throws IOException
    {
        super(parent);
        parentVPuzzle = parent;
        
        setSize(320, 400);
        goButton.addActionListener(this);
        backButton.addActionListener(this);
        
        top.add(topLabel);
        
        GridBagLayout gridbag = new GridBagLayout();
        GridBagConstraints constraints = new GridBagConstraints();
        main.setLayout(gridbag);
        
        constraints.insets = new Insets(10, 10, 10, 10);
        constraints.fill = GridBagConstraints.BOTH;
        constraints.weightx = 1.0;
        constraints.weighty = 1.0;
        constraints.gridwidth = GridBagConstraints.REMAINDER; //end row
        gridbag.setConstraints(puzzleList, constraints);
        main.add(puzzleList);
        constraints.weighty = 0.0;
        gridbag.setConstraints(diffLevelLabel, constraints);
        main.add(diffLevelLabel);
        
        bottom.setLayout(new FlowLayout(FlowLayout.RIGHT));
        bottom.add(goButton);
        bottom.add(backButton);
        
        add(top,BorderLayout.NORTH);
        add(main,BorderLayout.CENTER);
        add(bottom,BorderLayout.SOUTH);
        
        
        picIndex = new PicFileHandler(picRecordFileName);
        for(int i = 0;i<picIndex.numRecs;i++)
        {
            picIndex.readRec(i);
            puzzleList.addItem(picIndex.ImageRec.picName);
        }
    }
    
    public void actionPerformed(ActionEvent e)
    {
        if(e.getActionCommand().equals("Go"))
        {
/*
            PicFileHandler picIndex = null;
            try {
                picIndex = new PicFileHandler(indexfilename);
            } catch (IOException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
*/
            int recNum = puzzleList.getSelectedIndex();
            try {
                picIndex.readRec(recNum);
            } catch (IOException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
            parentVPuzzle.loadPuzzle(picIndex.ImageRec.picFileName +
                picIndex.ImageRec.picDirectory);
        }        
        else if(e.getActionCommand().equals("Back"))
        {
            hide();
            parentVPuzzle.showMainDialog();
        }
    }
}
