import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.FileDialog;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Label;
import java.awt.Panel;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class EditUserDialog extends BaseDialog implements ActionListener
{
    VirtualPuzzleApp parentVPuzzle;
    ChooseUserDialog parentUserChooseDlg;
    
    Label firstNameLabel = new Label("First Name:");
    TextField firstNameText = new TextField(20);
    Label lastNameLabel = new Label("Last Name:");
    TextField lastNameText = new TextField(20);
    
    Button okButton = new Button("OK");
    Button cancelButton = new Button("Cancel");
    
    public EditUserDialog(VirtualPuzzleApp parent, ChooseUserDialog parentDlg)
    {
        super(parent);
        
        parentVPuzzle = parent;
        parentUserChooseDlg = parentDlg;
        
        okButton.addActionListener(this);
        cancelButton.addActionListener(this);
        
        setSize(480, 300);
        
        Panel namesPanel = new Panel();
        GridBagLayout gridbag = new GridBagLayout();
        GridBagConstraints constraints = new GridBagConstraints();
        namesPanel.setLayout(gridbag);
        
        constraints.insets = new Insets(5, 5, 5, 5);
        gridbag.setConstraints(firstNameLabel, constraints);
        namesPanel.add(firstNameLabel);
        constraints.gridwidth = GridBagConstraints.REMAINDER; //end row
        gridbag.setConstraints(firstNameText, constraints);
        namesPanel.add(firstNameText);
        constraints.gridwidth = 1;
        gridbag.setConstraints(lastNameLabel, constraints);
        namesPanel.add(lastNameLabel);
        gridbag.setConstraints(lastNameText, constraints);
        namesPanel.add(lastNameText);
        
        main.add(namesPanel);
        
        bottom.setLayout(new FlowLayout(FlowLayout.RIGHT));
        bottom.add(okButton);
        bottom.add(cancelButton);
        
        add(main,BorderLayout.CENTER);
        add(bottom,BorderLayout.SOUTH);
    }
    
    public void actionPerformed(ActionEvent e)
    {
        if(e.getActionCommand().equals("OK"))
        {
            UsersFileRec newUserRec = new UsersFileRec();
            newUserRec.fName = firstNameText.getText();
            newUserRec.lName = lastNameText.getText();
            
            setVisible(false);
            parentUserChooseDlg.addNewUser(newUserRec);
        }
        else if(e.getActionCommand().equals("Cancel"))
        {
            setVisible(false);
        }
    }
}
