import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Dialog;
import java.awt.FileDialog;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.Label;
import java.awt.Panel;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;


public class EditPuzzleDialog extends BaseDialog implements ActionListener
{
    VirtualPuzzleApp parentVPuzzle;
    ManagePuzzlesDialog parentPuzzleManageDlg;
    int currentRecIx;
    
    Label puzzleNameLabel = new Label("Puzzle Name:");
    TextField puzzleNameText = new TextField(20);
    
    Label fileNameLabel = new Label("Image File:");
    TextField imageFileNamePathText = new TextField(30);
    Button browseButton = new Button("Browse...");
    
    Label piecesHorizLabel = new Label("Number of horizontal pieces:");
    TextField piecesHorizText = new TextField(4);
    Label piecesVertLabel = new Label("Number of vertical pieces:");
    TextField piecesVertText = new TextField(4);
    
    Button okButton = new Button("OK");
    Button cancelButton = new Button("Cancel");
    
    public EditPuzzleDialog(VirtualPuzzleApp parent, ManagePuzzlesDialog parentDlg)
    {
        super(parent);
        
        parentVPuzzle = parent;
        parentPuzzleManageDlg = parentDlg;
        
        browseButton.addActionListener(this);
        okButton.addActionListener(this);
        cancelButton.addActionListener(this);
        
        setSize(480, 300);
        
        // puzzle name
        top.setLayout(new FlowLayout(FlowLayout.CENTER));
        top.add(puzzleNameLabel);
        top.add(puzzleNameText);
        
        // image file choose
        Panel imageFilePanel = new Panel();
        imageFilePanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        imageFilePanel.add(fileNameLabel);
        imageFilePanel.add(imageFileNamePathText);
        imageFilePanel.add(browseButton);
        main.add(imageFilePanel);
        
        // num segments horizontal and vertical
        Panel puzzleSizePanel = new Panel();
        GridBagLayout gridbag = new GridBagLayout();
        GridBagConstraints constraints = new GridBagConstraints();
        puzzleSizePanel.setLayout(gridbag);
        
        constraints.insets = new Insets(5, 5, 5, 5);
        gridbag.setConstraints(piecesHorizLabel, constraints);
        puzzleSizePanel.add(piecesHorizLabel);
        constraints.gridwidth = GridBagConstraints.REMAINDER; //end row
        gridbag.setConstraints(piecesHorizText, constraints);
        puzzleSizePanel.add(piecesHorizText);
        constraints.gridwidth = 1;
        gridbag.setConstraints(piecesVertLabel, constraints);
        puzzleSizePanel.add(piecesVertLabel);
        gridbag.setConstraints(piecesVertText, constraints);
        puzzleSizePanel.add(piecesVertText);
        
        main.add(puzzleSizePanel);
        
        bottom.setLayout(new FlowLayout(FlowLayout.RIGHT));
        bottom.add(okButton);
        bottom.add(cancelButton);
        
        add(top,BorderLayout.NORTH);
        add(main,BorderLayout.CENTER);
        add(bottom,BorderLayout.SOUTH);
        
        currentRecIx = -1; // < 0 means add a new record
    }
    
    public void clearForm()
    {
        currentRecIx = -1;
        
        puzzleNameText.setText("");
        imageFileNamePathText.setText("");
        piecesHorizText.setText("");
        piecesVertText.setText("");
    }

    public void loadPuzzleRecord(int recIx, PuzzlesFileRec puzzleRec)
    {
        currentRecIx = recIx;
        
        puzzleNameText.setText(puzzleRec.puzzleName);
        imageFileNamePathText.setText(puzzleRec.imgFileName);
        piecesHorizText.setText(new Integer(puzzleRec.numSegmentsX).toString());
        piecesVertText.setText(new Integer(puzzleRec.numSegmentsY).toString());
    }

    public void actionPerformed(ActionEvent e)
    {
        if(e.getActionCommand().equals("Browse..."))
        {
            FileDialog d = new FileDialog(parentVPuzzle, "What file do you want to open?");
            d.setFile("*.jpg");
            d.setDirectory("."); // Current directory
            d.show();
            String yourFile = "*.*";
            if((yourFile = d.getFile()) != null)
            {
                imageFileNamePathText.setText(d.getDirectory() + yourFile);
            }
            else
            {
            }
        }
        else if(e.getActionCommand().equals("OK"))
        {
            PuzzlesFileRec newPuzzleRec = new PuzzlesFileRec();
            newPuzzleRec.puzzleName = puzzleNameText.getText();
            newPuzzleRec.imgFileName = imageFileNamePathText.getText();
            
            Integer numSegs;
            try
            {
                numSegs = new Integer(piecesHorizText.getText());
            }
            catch(NumberFormatException numberFormatException)
            {
                parentVPuzzle.showMessageDialog("Number of horizontal segments is not a number.");
                return;
            }
            newPuzzleRec.numSegmentsX = numSegs.intValue();
            
            try
            {
                numSegs = new Integer(piecesVertText.getText());
            }
            catch(NumberFormatException numberFormatException)
            {
                parentVPuzzle.showMessageDialog("Number of vertical segments is not a number.");
                return;
            }
            newPuzzleRec.numSegmentsY = numSegs.intValue();
            
            hide();
            if(currentRecIx == -1)
            {
                parentPuzzleManageDlg.addNewPuzzle(newPuzzleRec);
            }
            else
            {
                parentPuzzleManageDlg.updatePuzzle(currentRecIx, newPuzzleRec);
            }
        }
        else if(e.getActionCommand().equals("Cancel"))
        {
            hide();
        }
    }
}
