import java.awt.*;
import java.awt.event.*;

	class loadPicsDialog extends basicMenuDialog implements ActionListener
	{
		//load pics button
		Button browse = new Button("Browse...");
		List namelist = new List();
		TextField imageNamebox = new TextField();
		
		Frame parentFrame;
		
		public loadPicsDialog(Frame parent, mainMenuDialog main)
		{
			super(parent);
			parentFrame = parent;
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
				class OpenL implements ActionListener
				{
					public void actionPerformed(ActionEvent e)
					{ // Two arguments, defaults to open file:
						 FileDialog d = new FileDialog(parentFrame, "What file do you want to open?");
						 d.setFile("*.jpg");
						 d.setDirectory("."); // Current directory
						 d.show();
						 String yourFile = "*.*";
						 if((yourFile = d.getFile()) != null)
						 {
						 	imageNamebox.setText(d.getDirectory() + yourFile);
						 }
						 else
						 {
						 }
					}
				}
				
			}
		}
	}// end of loadPicsDialog
	