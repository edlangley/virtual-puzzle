import java.awt.*;
import java.awt.event.*;

class doPuzzleDialog extends basicMenuDialog implements ActionListener
{
	Label lab1 = new Label("Choose a picture");
	Button go = new Button("Go");
	List puzzleList = new List();
	String indexfilename;
	public doPuzzleDialog(Frame parent, mainMenuDialog mainMenu, picIndexRecFileHandler picIndex)
	{
		super(parent);
		indexfilename = picIndex.filename;
		
		for(int i = 0;i<picIndex.numRecs;i++)
		{
			picIndex.readRec(i);
			puzzleList.additem(picIndex.imageRec.picName);
		}
		go.addActionListener(this);
		cancel.addActionListener(this);
		
		top.add(lab1);
		main.add(puzzleList)
		
		bottom.setLayout(new FlowLayout(FlowLayout.RIGHT));
		bottom.add(go);
		bottom.add(cancel);
		
		add(top,BorderLayout.NORTH);
		add(main,BorderLayout.CENTER);
		add(bottom,BorderLayout.SOUTH);
		
	}
	
	public void actionPerformed(ActionEvent e)
	{		
		if(e.getActionCommand() == "cancel")
		{
			dispose();
			mainMenu.show();
		}
		if(e.getActionCommand() == "Go")
		{
			picIndexRecFileHandler picIndex = new picIndexRecFileHandler(indexfilename);
			int recNum = puzzleList.getSelectedIndex();
			picIndex.readRec(recNum);
			main.loadPuzzle(picIndex.imageRec.picFilename+
				picIndex.imageRec.picDirectory);
		}
	}
}
	