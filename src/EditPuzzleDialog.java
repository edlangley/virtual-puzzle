import java.awt.BorderLayout;
import java.awt.Button;
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
    
    Label fileNameLabel = new Label("Image File:");
    TextField imageFileNamePathText = new TextField(30);
    Button browseButton = new Button("Browse...");
    
    // Difficulty widets
    //Panel horizPanel = new Panel();
    Label piecesHorizLabel = new Label("Number of horizontal pieces:");
    TextField piecesHorizText = new TextField(4);
    //Panel vertPanel = new Panel();
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
        
        top.setLayout(new FlowLayout(FlowLayout.LEFT));
        top.add(fileNameLabel);
        top.add(imageFileNamePathText);
        top.add(browseButton);
        
        // num segments horizontal and vertical
        //main.setLayout(new GridLayout(2,2));
        
        GridBagLayout gridbag = new GridBagLayout();
        GridBagConstraints constraints = new GridBagConstraints();
        main.setLayout(gridbag);
        
        constraints.insets = new Insets(5, 5, 5, 5);
        gridbag.setConstraints(piecesHorizLabel, constraints);
        main.add(piecesHorizLabel);
        constraints.gridwidth = GridBagConstraints.REMAINDER; //end row
        gridbag.setConstraints(piecesHorizText, constraints);
        main.add(piecesHorizText);
        constraints.gridwidth = 1;
        gridbag.setConstraints(piecesVertLabel, constraints);
        main.add(piecesVertLabel);
        gridbag.setConstraints(piecesVertText, constraints);
        main.add(piecesVertText);
        
        bottom.setLayout(new FlowLayout(FlowLayout.RIGHT));
        bottom.add(okButton);
        bottom.add(cancelButton);
        
        add(top,BorderLayout.NORTH);
        add(main,BorderLayout.CENTER);
        add(bottom,BorderLayout.SOUTH);
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
            hide();
            // parentPuzzleManageDlg.loadNewPuzzle or similar
        }
        else if(e.getActionCommand().equals("Cancel"))
        {
            hide();
        }
    }
}
