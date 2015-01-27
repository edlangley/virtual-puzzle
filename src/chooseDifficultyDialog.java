import java.awt.*;
import java.awt.event.*;

class chooseDifficultyDialog extends basicMenuDialog implements ActionListener
	{
		//load pics button
		Button browse = new Button("Browse...");
		List namelist = new List();
		
		public chooseDifficultyDialog(Frame parent, mainMenuDialog main)
		{
			super(parent);
			
		}
		
		public void actionPerformed(ActionEvent e)
		{		
			if(e.getActionCommand() == "cancel")
			{
				dispose();
				main.show();
			}
			if(e.getActionCommand() == "loadPics")
			{
				hide();
				//loadPicsDialog1.show();
				
			}
		}
	}// end of loadPicsDialog
	