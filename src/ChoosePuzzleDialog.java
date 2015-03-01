import java.awt.*;
import java.awt.event.*;
import java.io.IOException;

import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableColumnModel;
import javax.swing.table.DefaultTableModel;

class ChoosePuzzleDialog extends BaseDialog implements ActionListener, ListSelectionListener
{
    VirtualPuzzleApp parentVPuzzle;
    
    Label topLabel = new Label("Choose a puzzle");
    Button goButton = new Button("Go");
    
    private JTable puzzleTable = new JTable();
    private JScrollPane puzzleScrollPane = new JScrollPane(puzzleTable);
    
    FileHandler<PuzzlesFileRec> puzzlesFileHandler;
    
    public ChoosePuzzleDialog(VirtualPuzzleApp parent, String puzzlesFileName)
    {
        super(parent);
        parentVPuzzle = parent;
        
        setSize(320, 400);
        goButton.addActionListener(this);
        backButton.addActionListener(this);
        
        top.add(topLabel);
        
        puzzleTable.setGridColor(Color.WHITE);
        puzzleTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        puzzleTable.setColumnSelectionAllowed(false);
        puzzleTable.getTableHeader().setReorderingAllowed(false);
        puzzleTable.getSelectionModel().addListSelectionListener(this);
        puzzleScrollPane.getViewport().setBackground(Color.WHITE);
        
        GridBagLayout gridbag = new GridBagLayout();
        GridBagConstraints constraints = new GridBagConstraints();
        main.setLayout(gridbag);
        
        constraints.insets = new Insets(10, 10, 10, 10);
        constraints.fill = GridBagConstraints.BOTH;
        constraints.weightx = 1.0;
        constraints.weighty = 1.0;
        constraints.gridwidth = GridBagConstraints.REMAINDER; //end row
        gridbag.setConstraints(puzzleScrollPane, constraints);
        main.add(puzzleScrollPane);
        
        bottom.setLayout(new FlowLayout(FlowLayout.RIGHT));
        bottom.add(goButton);
        bottom.add(backButton);
        
        add(top,BorderLayout.NORTH);
        add(main,BorderLayout.CENTER);
        add(bottom,BorderLayout.SOUTH);
        
        // Nothing in table selected initially
        goButton.disable();
    }
    
    public void loadPuzzleList(String puzzlesFileName, UsersFileRec userRec)
    {
        puzzlesFileHandler = new FileHandler(puzzlesFileName);
        
        DefaultTableModel newTableData = new DefaultTableModel(0, 3)
        {
            String columnNames[] = { "Puzzle Name", "Best Time", "Num Moves" };
            
            public String getColumnName(int index)
            { 
                return columnNames[index]; 
            }
            
            public boolean isCellEditable(int row, int column)
            {
                return false;
            }
        };
        String[] rowVals = new String[3];
        
        for(int rowIx = 0; rowIx < puzzlesFileHandler.numRecords(); rowIx++)
        {
            String puzzleName = puzzlesFileHandler.readRec(rowIx).puzzleName;
            rowVals[0] = puzzleName;
            
            boolean userHasNoScore = true;
            for(int scoreRecIx = 0; scoreRecIx < userRec.puzzleScores.size(); scoreRecIx++)
            {
                if(userRec.puzzleScores.get(scoreRecIx).puzzleName.equals(puzzleName))
                {
                    rowVals[1] = new Integer(userRec.puzzleScores.get(scoreRecIx).timeTaken).toString() + " second";
                    if(userRec.puzzleScores.get(scoreRecIx).timeTaken != 1)
                    {
                        rowVals[1] += "s";
                    }
                    rowVals[2] = new Integer(userRec.puzzleScores.get(scoreRecIx).numMoves).toString();
                    
                    userHasNoScore = false;
                }
            }
            if(userHasNoScore)
            {
                rowVals[1] = "-";
                rowVals[2] = "-";
            }
            
            newTableData.addRow(rowVals);
        }
        
        puzzleTable.setModel(newTableData);
        
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(SwingConstants.RIGHT);
        puzzleTable.getColumnModel().getColumn(1).setCellRenderer( centerRenderer );
        puzzleTable.getColumnModel().getColumn(2).setCellRenderer( centerRenderer );
    }
    
    public void actionPerformed(ActionEvent e)
    {
        if(e.getActionCommand().equals("Go"))
        {
            int recIx = puzzleTable.getSelectedRow();
            PuzzlesFileRec chosenPuzzleRec = puzzlesFileHandler.readRec(recIx);
            if(parentVPuzzle.loadPuzzle(chosenPuzzleRec) == true)
            {
                setVisible(false);
            }
        }
        else if(e.getActionCommand().equals("Back"))
        {
            setVisible(false);
            parentVPuzzle.showMainDialog();
        }
    }

    public void valueChanged(ListSelectionEvent arg0)
    {
        if(puzzleTable.getSelectedRow() == -1)
        {
            goButton.disable();
        }
        else
        {
            goButton.enable();
        }
    }
}
