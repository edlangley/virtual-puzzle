import java.awt.*;
import java.awt.event.*;
import java.awt.Toolkit.*;
import java.awt.image.*;
import java.util.*;

class Puzzle extends Panel implements Runnable, ComponentListener
{
    private static int NUM_JUMBLE_SEG_SWAPS = 1000;
    private static int PUZZLE_MARGIN = 5;
    private static Color BLANK_SEGMENT_COLOR = new Color(0, 140, 255);
    private static Color BACKGROUND_COLOR = new Color(255, 252, 102);
    
    VirtualPuzzleApp parentVPuzzle;
    
    private int puzzleWidth, puzzleHeight, offSetX, offSetY;
    private int numSegsX, numSegsY, picSegSizeX, picSegSizeY, scaledSegSizeX, scaledSegSizeY;
    
    private int blankIndexX, blankIndexY;
    PuzzleSegment[][] positions;
    Image mainPic = null;
    
    Toolkit toolkit = Toolkit.getDefaultToolkit();
    
    Thread timer = null;
    MediaTracker track = new MediaTracker(this);
    
    int timeElapsedSecs = 0;
    Panel timePanel = new Panel();
    Label timeText = new Label();
    
    int numMovesMade = 0;
    boolean ready = false;
    boolean timerRunning = false;
    boolean paused = false;
    boolean won = false;
    
    public Puzzle(VirtualPuzzleApp parent)
    {
        parentVPuzzle = parent;
        
        setBackground(BACKGROUND_COLOR);
        addComponentListener(this);
    }
    
    public boolean load(String picName, int numSegsX, int numSegsY)
    {
        ready = false;
        paused = false;
        won = false;
        numMovesMade = 0;
        timeElapsedSecs = 0;
        timerRunning = false;
        if(timer != null)
        {
            try
            {
                timer.join();
            }
            catch(InterruptedException e)
            {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        timer = new Thread(this);
        timeText.setText(String.valueOf(timeElapsedSecs));
        
        timeText.setAlignment(Label.LEFT);
        timePanel.add(timeText);
        parentVPuzzle.add(timePanel, BorderLayout.SOUTH);
        
        if(loadImage(picName))
        {
            this.numSegsX = numSegsX;
            picSegSizeX = (mainPic.getWidth(this) / numSegsX);
            this.numSegsY = numSegsY;
            picSegSizeY = (mainPic.getHeight(this) / numSegsY);
            
            positions = new PuzzleSegment[numSegsX][numSegsY];
            
            calcScaleAndOffset();
            
            for(int x = 0; x< numSegsX; x++)
            {
                for(int y = 0; y< numSegsY; y++)
                {
                    positions[x][y] = new PuzzleSegment(picSegSizeX*x, picSegSizeY*y, picSegSizeX, picSegSizeY);
                    // put actual positions of segments into array:
                    positions[x][y].setActualPos(scaledSegSizeX*x, scaledSegSizeY*y);
                }
            }
            
            // set position of blank segment as lower left
            blankIndexX = numSegsX;
            blankIndexY = numSegsY;
            blankIndexX--; blankIndexY--;
            
            // keep blank index source positions 0 to get through won check
            positions[blankIndexX][blankIndexY].correctsX = 0;
            positions[blankIndexX][blankIndexY].correctsY = 0;
            
            ready = true;
            jumbleSegs(NUM_JUMBLE_SEG_SWAPS);
            timerRunning = true;
            timer.start();
        }
        else
        {
            System.out.println("LoadImage() error");
            return false;
        }
        
        return true;
    }

    public void run()
    {
        int numTicks = 0;
        
        while(timerRunning)
        {
            if(!paused)
            {
                if(numTicks++ >= 10)
                {
                    timeElapsedSecs +=1;
                    timeText.setText(String.valueOf(timeElapsedSecs));
                    
                    numTicks = 0;
                }
            }
            try
            {
                timer.sleep(100);
            }
            catch(InterruptedException e){return;}
        }
    }

    public void paint(Graphics g)
    {
        g.setColor(BLANK_SEGMENT_COLOR);
        
        if(ready)
        {
            for(int x = 0; x< numSegsX; x++)
            {
                for(int y= 0; y< numSegsY; y++)
                {
                    
                    g.drawImage(mainPic, offSetX+positions[x][y].dX, offSetY+positions[x][y].dY, 
                                offSetX+positions[x][y].dX+scaledSegSizeX, 
                                offSetY+positions[x][y].dY+scaledSegSizeY, positions[x][y].sX, 
                                positions[x][y].sY, positions[x][y].sX+picSegSizeX, 
                                positions[x][y].sY+picSegSizeY, this);
                }
            }
            // draw a blank box at blank indexes
            g.fillRect(offSetX+positions[blankIndexX][blankIndexY].dX, 
                        offSetY+positions[blankIndexX][blankIndexY].dY, 
                        scaledSegSizeX,scaledSegSizeY);
        }
        if(won)
        {
            won = false;
            timerRunning = false;
            try
            {
                timer.join();
            }
            catch(InterruptedException e)
            {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            timer = null;
            parentVPuzzle.updateUserScore(timeElapsedSecs, numMovesMade);
            parentVPuzzle.showWinScreen(timeElapsedSecs, numMovesMade);
        }
    }  
    
    public void findSegKeyboard(KeyEvent e)
    {
        if(!paused)
        {
            if(e.getKeyCode() == e.VK_UP)
            {
                System.out.println("Up");
                swapSeg(blankIndexX, blankIndexY-1, true);
            }
            if(e.getKeyCode() == e.VK_DOWN)
            {
                System.out.println("Down");
                swapSeg(blankIndexX, blankIndexY+1, true);
            }
            if(e.getKeyCode() == e.VK_LEFT)
            {
                System.out.println("Left");
                swapSeg(blankIndexX-1, blankIndexY, true);
            }
            if(e.getKeyCode() == e.VK_RIGHT)
            {
                System.out.println("Right");
                swapSeg(blankIndexX+1, blankIndexY, true);
            }
        }
    }
    
    public void findSegMouse(int x, int y)
    {
        if(!paused)
        {
            boolean foundX = false;
            boolean foundY = false;
            boolean cancel = false;
            int indexX = 0;
            int indexY = 0;
            
            x -= offSetX;
            y -= offSetY;
            System.out.println("offSetX - " + offSetX);
            System.out.println("offSetY - " + offSetY);
            
            indexX = x / scaledSegSizeX;
            indexY = y / scaledSegSizeY;
            System.out.println("Found X - " + indexX);
            System.out.println("Found Y - " + indexY);
            
            swapSeg(indexX, indexY, true);
        }
    }
    
    public void setPaused(boolean pauseVal)
    {
        paused = pauseVal;
        
        if(paused)
        {
            timeText.setText("Paused");
        }
    }
    
    private boolean loadImage(String picName)
    {
        mainPic = toolkit.getImage(picName);
        track.addImage(mainPic, 0);
        try
        {
            track.waitForID(0);
        }
        catch(InterruptedException e)
        {
            return(false);
        }
        
        if((mainPic.getWidth(this)== -1) && (mainPic.getHeight(this)== -1))
            return(false);
        else
            return(true);
    }
    
    private void calcScaleAndOffset()
    {
        int panelHeight = getSize().height;
        int panelWidth = getSize().width;
        int imageHeight = mainPic.getHeight(this);
        int imageWidth = mainPic.getWidth(this);
        
        if(panelHeight >= panelWidth)
        {
            if((imageHeight >= imageWidth) &&
               (((float)imageHeight / (float)imageWidth) >= ((float)panelHeight / (float)panelWidth)))
            {
                puzzleHeight = panelHeight - (PUZZLE_MARGIN * 2);
                puzzleWidth = (int) (imageWidth / ((float)imageHeight / puzzleHeight));
            }
            else
            {
                puzzleWidth = panelWidth - (PUZZLE_MARGIN * 2);
                puzzleHeight = (int) (imageHeight / ((float)imageWidth / puzzleWidth));
            }
        }
        else
        {
            if((imageHeight < imageWidth) &&
               (((float)imageWidth / (float)imageHeight) >= ((float)panelWidth / (float)panelHeight)))
            {
                puzzleWidth = panelWidth - (PUZZLE_MARGIN * 2);
                puzzleHeight = (int) (imageHeight / ((float)imageWidth / puzzleWidth));
            }
            else
            {
                puzzleHeight = panelHeight - (PUZZLE_MARGIN * 2);
                puzzleWidth = (int) (imageWidth / ((float)imageHeight / puzzleHeight));
            }
        }
        
        scaledSegSizeX = (int)(puzzleWidth/numSegsX);
        scaledSegSizeY = (int)(puzzleHeight/numSegsY);
        
        offSetX = (getSize().width / 2) - (puzzleWidth / 2);
        offSetY = (getSize().height / 2) - (puzzleHeight / 2);
    }
    
    private void jumbleSegs(int numMoves)
    {
        int u = 0;int d = 0;int l = 0;int right = 0;
        
        for(int i=0; i<numMoves;i++)
        {
            double r = java.lang.Math.random()*10;
            
            if(r <= 2.5)
            {
                swapSeg(blankIndexX, blankIndexY-1, false);
            }
            else if( (r > 2.5) && (r <= 5))
            {
                swapSeg(blankIndexX, blankIndexY+1, false);
            }
            else if((r > 5) && (r <= 7.5))
            {
                swapSeg(blankIndexX-1, blankIndexY, false);
            }
            else if((r > 7.5) && (r <= 10))
            {
                swapSeg(blankIndexX+1, blankIndexY, false);
            }
        }
    }
    
    private boolean swapSeg(int indexX, int indexY, boolean isPlayer)
    {
        
        if( (indexX < 0 || indexX >= numSegsX) || (indexY < 0 || indexY >= numSegsY) )
        {
            return(false);
        }
        
        // is the selected segment adjacent to the blank one?
        if( ((indexX == blankIndexX) && (java.lang.Math.abs(indexY-blankIndexY) == 1)) ||
            ((indexY == blankIndexY) && (java.lang.Math.abs(indexX-blankIndexX) == 1)) )
        {
            int tempPosX, tempPosY, tempSourceX, tempSourceY, tempBlSourceX, tempBlSourceY;
            
            // swap sources:
            tempSourceX = positions[indexX][indexY].sX;
            tempSourceY = positions[indexX][indexY].sY;
            
            positions[blankIndexX][blankIndexY].sX = tempSourceX;
            positions[blankIndexX][blankIndexY].sY = tempSourceY;
            
            blankIndexX = indexX;
            blankIndexY = indexY;
            
            // keep bl index sources 0 to get through win check
            positions[blankIndexX][blankIndexY].sX = 0;
            positions[blankIndexX][blankIndexY].sY = 0;
            
            if(isPlayer)
            {
                won = checkForWon();
                numMovesMade += 1;
            }
            
            repaint();
        }
        
        return(true);
    }
    
    private boolean checkForWon()
    {
        for(int x = 0; x< numSegsX; x++)
        {
            for(int y= 0; y< numSegsY; y++)
            {
                if(positions[x][y].sX != positions[x][y].correctsX ||
                   positions[x][y].sY != positions[x][y].correctsY)
                {
                    
                    System.out.println("\nChecking win: sX: " + positions[x][y].sX + " correctsX: " +
                                            positions[x][y].correctsX);
                    System.out.println("Checking win: sY: " + positions[x][y].sY + " correctsY: " +
                                            positions[x][y].correctsY);
                    return(false);
                }
            }
        }
        
        return(true);
    }

    @Override
    public void componentHidden(ComponentEvent arg0)
    {
    }

    @Override
    public void componentMoved(ComponentEvent arg0)
    {
    }

    @Override
    public void componentResized(ComponentEvent arg0)
    {
        if(ready)
        {
            calcScaleAndOffset();
            
            for(int x = 0; x< numSegsX; x++)
            {
                for(int y = 0; y< numSegsY; y++)
                {
                    positions[x][y].setActualPos(scaledSegSizeX*x, scaledSegSizeY*y);
                }
            }
        }
        
    }

    @Override
    public void componentShown(ComponentEvent arg0)
    {
    }
}
