import java.awt.*;
import java.awt.event.*;
import java.io.IOException;

class ChoosePuzzleDialog extends BaseDialog implements ActionListener
{
    Label lab1 = new Label("Choose a picture");
    Button go = new Button("Go");
    List puzzleList = new List();
    PicFileHandler picIndex;
    
    VirtualPuzzleApp parentVPuzzle;
    
    public ChoosePuzzleDialog(VirtualPuzzleApp parent, String picRecordFileName) throws IOException
    {
        super(parent);
        parentVPuzzle = parent;
        
        picIndex = new PicFileHandler(picRecordFileName);
        
        for(int i = 0;i<picIndex.numRecs;i++)
        {
            picIndex.readRec(i);
            puzzleList.addItem(picIndex.ImageRec.picName);
        }
        go.addActionListener(this);
        cancel.addActionListener(this);
        
        top.add(lab1);
        main.add(puzzleList);
        
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
        //    mainMenu.show();
            main.show();
        }
        if(e.getActionCommand() == "Go")
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
            parentVPuzzle.loadPuzzle(picIndex.ImageRec.picFileName+
                picIndex.ImageRec.picDirectory);
        }
    }
}
