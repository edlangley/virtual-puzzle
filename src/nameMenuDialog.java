import java.awt.*;
import java.awt.event.*;

class nameMenuDialog extends Dialog implements ActionListener
{
	
	Button ok = new Button("OK");
	Button Cancel = new Button("Cancel");
	Button Add = new Button("Add");
	
	List nameList = new List();
	Label lab1 = new Label("Pick Your name from the list:");
	Label lab2 = new Label("If this is your first time using this software");
	Label lab3 = new Label("click add.");
	//file handler:
	userProgFileHandler userProgFile;
	
	public nameMenuDialog(Frame parent, mainMenuDialog main, String userProgFName)
	{
		super(parent);
		userProgFile = new userProgFileHandler(userProgFName);
		//list stuff
		for(int i = 0;i<userProgFile.numRecs;i++)
		{
			userProgFile.readRec(i);
			nameList.additem(userProgFile.progRec.fName+" "+userProgFile.progRec.lName);
		}
		
		
		addWindowListener(new WindowAdapter()
			{
				public void windowClosing(WindowEvent e)
				{
					dispose();
				
				}
			});
		setSize(300,383);
		setTitle("Welcome - choose your name");
		setModal(true);
		setResizable(false);
		
		setLayout(null);
		lab1.setBounds(20,30,300,15);
		lab2.setBounds(20,50,300,15);
		lab3.setBounds(20,65,300,10);
		lst.setBounds(20,85,260,250);
		
		
		ok.setBounds(54,345,72,23);
		ok.addActionListener(this);
		
		Add.setBounds(131,345,72,23);
		Add.addActionListener(this);
		
		Cancel.setBounds(208,345,72,23);
		Cancel.addActionListener(this);
		
		add(lab1);
		add(lab2);
		add(lab3);
		add(lst);
		add(ok);
		add(Add);
		add(Cancel);
		
		//add(upper, "Center");
		//add(lower,"South");
	}
	
	public void actionPerformed(ActionEvent e)
	{		
		if(e.getActionCommand() == "Cancel")
		{
			dispose();
			System.exit(0);
		}
		if(e.getActionCommand() == "OK")
		{
			int recNum = nameList.getSelectedIndex();
			userProgFile.readRec(recNum);
			main.loadProgRec(userProgFile.progRec)
		}	
	}		
}
	