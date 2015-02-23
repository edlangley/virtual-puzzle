import java.awt.*;
import java.awt.event.*;
import java.io.IOException;

import javax.swing.BoxLayout;

class ChooseUserDialog extends BaseDialog implements ActionListener, ItemListener
{
    VirtualPuzzleApp parentVPuzzle;

    Label topLabel1 = new Label("Pick Your name from the list\nnext line");
    Label topLabel2 = new Label("If this is your first time using this software");
    Label topLabel3 = new Label("click add.");
    Button addButton = new Button("Add");
    Button deleteButton = new Button("Delete");
    
    Button okButton = new Button("OK");
    Button quitButton = new Button("Quit");
    
    List nameList = new List();
    
    EditUserDialog editDialog;
    
    FileHandler<UsersFileRec> usersFileHandler;
    
    public ChooseUserDialog(VirtualPuzzleApp parent, String usersFName) throws IOException
    {
        super(parent);
        parentVPuzzle = parent;

        setSize(300, 384);
        setTitle("Welcome - choose your name");
        addButton.addActionListener(this);
        deleteButton.addActionListener(this);
        okButton.addActionListener(this);
        quitButton.addActionListener(this);
        nameList.addItemListener(this);
        
        Panel topPanel = new Panel();
        topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.Y_AXIS));
        topPanel.add(topLabel1);
        topPanel.add(topLabel2);
        topPanel.add(topLabel3);
        top.add(topPanel);
        //top.add(topLabel1);
        
        GridBagLayout gridbag = new GridBagLayout();
        GridBagConstraints constraints = new GridBagConstraints();
        main.setLayout(gridbag);
        
        constraints.insets = new Insets(5, 5, 5, 5);
        constraints.fill = GridBagConstraints.BOTH;
        constraints.gridheight = 4;
        constraints.weightx = 1.0;
        constraints.weighty = 1.0;
        gridbag.setConstraints(nameList, constraints);
        main.add(nameList);
        
        constraints.weightx = 0.0;
        constraints.weighty = 0.0;
        constraints.gridwidth = GridBagConstraints.REMAINDER; //end row
        constraints.gridheight = 1;
        gridbag.setConstraints(addButton, constraints);
        main.add(addButton);
        gridbag.setConstraints(deleteButton, constraints);
        main.add(deleteButton);
        
        bottom.setLayout(new FlowLayout(FlowLayout.RIGHT));
        bottom.add(okButton);
        bottom.add(quitButton);
        
        add(top,BorderLayout.NORTH);
        add(main,BorderLayout.CENTER);
        add(bottom,BorderLayout.SOUTH);
        
        editDialog = new EditUserDialog(parent, this);
        
        usersFileHandler = new FileHandler(usersFName);
        for(int i = 0; i < usersFileHandler.numRecords(); i++)
        {
            nameList.add(usersFileHandler.readRec(i).fName + " " + usersFileHandler.readRec(i).lName);
        }
        
        // Nothing in list selected initially
        deleteButton.disable();
        okButton.disable();
    }

    public void addNewUser(UsersFileRec newUserRec)
    {
        usersFileHandler.addRec(newUserRec);
        nameList.addItem(newUserRec.fName + " " + newUserRec.lName);
    }
    
    public void actionPerformed(ActionEvent e)
    {
        if(e.getActionCommand().equals("Add"))
        {
            editDialog.show();
        }
        else if(e.getActionCommand().equals("Delete"))
        {
            int recNum = nameList.getSelectedIndex();
            
            if(nameList.getSelectedIndex() >= 0)
            {
                nameList.remove(recNum);
                usersFileHandler.removeRec(recNum);
            }
        }
        else if(e.getActionCommand().equals("OK"))
        {
            int recNum = nameList.getSelectedIndex();
            
            if(nameList.getSelectedIndex() >= 0)
            {
                parentVPuzzle.loadUser(nameList.getSelectedIndex(), usersFileHandler.readRec(nameList.getSelectedIndex()));
                hide();
                parentVPuzzle.showMainDialog();
            }
        }
        else if(e.getActionCommand().equals("Quit"))
        {
            dispose();
            System.exit(0);
        }
    }
    
    public void itemStateChanged(ItemEvent e)
    {
        if(nameList.getSelectedIndex() == -1)
        {
            deleteButton.disable();
            okButton.disable();
        }
        else
        {
            deleteButton.enable();
            okButton.enable();
        }
    }
}
