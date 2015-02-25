import java.awt.*;
import java.awt.event.*;
import java.awt.Toolkit.*;
import java.awt.image.*;
import java.util.*;

class Puzzle extends Panel implements Runnable
{
    VirtualPuzzleApp parentVPuzzle;
    
    private int puzzleWidthLimit, puzzleHeightLimit, offSetX, offSetY; // passed in
    private int puzzleWidth, puzzleHeight, numSegsX, numSegsY, picSegSizeX, picSegSizeY; // calculated
    private int scaledSegSizeX, scaledSegSizeY;
    
    private int blankIndexX, blankIndexY;
    PuzzleSegment[][] positions;
    Image mainPic = null;
    
    Toolkit toolkit = Toolkit.getDefaultToolkit();
    
    Thread timer;
    MediaTracker track = new MediaTracker(this);
    
    int timeElapsedSecs = 0;
    Panel timePanel = new Panel();
    Label timeText = new Label();
    
    int numMovesMade = 0;
    boolean ready = false;
    boolean timerRunning = false;
    boolean won = false;
    
    public Puzzle(VirtualPuzzleApp parent)
    {
        parentVPuzzle = parent;
    }
    
    public boolean load(String picName, int numSegsX, int numSegsY, int picOffsetX, int picOffsetY, int limit)
    {
        ready = false;
        won = false;
        numMovesMade = 0;
        timeElapsedSecs = 0;
        timeText.setText(String.valueOf(timeElapsedSecs));
        timer = new Thread(this);
        
        offSetX = picOffsetX;
        offSetY = picOffsetY;
        puzzleWidthLimit = limit;
        puzzleHeightLimit = limit;
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
            scaleImage(mainPic);
            
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
            jumbleSegs(1000);
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
            if(numTicks++ >= 10)
            {
                timeElapsedSecs +=1;
                timeText.setText(String.valueOf(timeElapsedSecs));
                
                numTicks = 0;
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
        g.setColor(Color.blue);
        
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
            parentVPuzzle.updateUserScore(timeElapsedSecs, numMovesMade);
            parentVPuzzle.showWinScreen(timeElapsedSecs, numMovesMade);
        }
    }  
    
    public void findSegKeyboard(KeyEvent e)
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
    
    public void findSegMouse(int x, int y)
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

    private void scaleImage(Image pic1)
    {
        if (pic1.getHeight(this) > pic1.getWidth(this))
        {
            if(pic1.getHeight(this) > puzzleHeightLimit)
                puzzleWidth = pic1.getWidth(this)/(pic1.getHeight(this)/puzzleHeightLimit);
            else
                puzzleWidth = pic1.getWidth(this)*(puzzleHeightLimit/pic1.getHeight(this));    
            puzzleHeight = puzzleHeightLimit;
            
        }
        else if (pic1.getHeight(this) < pic1.getWidth(this))
        {
            if(pic1.getWidth(this) > puzzleWidthLimit)
                puzzleHeight = pic1.getHeight(this)/(pic1.getWidth(this)/puzzleWidthLimit);
            else
                puzzleHeight = pic1.getHeight(this)*(puzzleWidthLimit/pic1.getWidth(this));
            puzzleWidth = puzzleWidthLimit;
        }
        else if (pic1.getHeight(this) == pic1.getWidth(this))
        {
            puzzleHeight = puzzleHeightLimit;
            puzzleWidth = puzzleWidthLimit;
        }
        
        scaledSegSizeX = (int)(puzzleWidth/numSegsX);
        scaledSegSizeY = (int)(puzzleHeight/numSegsY);
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
}
