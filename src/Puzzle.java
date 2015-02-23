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

    //Threading stuff
    Thread timer = new Thread(this);
    MediaTracker track = new MediaTracker(this);
    //Time timeElapsed in seconds
    int timeElapsed = 0;
    
    //Time display
    Panel p1 = new Panel();
    Label timeText = new Label();
    
    int numMovesMade = 0;
    boolean loaded = false;
    boolean ready = false;
    boolean won = false;

    public Puzzle(VirtualPuzzleApp parent)
    {
        parentVPuzzle = parent;
    }
    
    public boolean load(String picName, int numSegsX, int numSegsY, int picOffsetX, int picOffsetY, int limit)
    {
        offSetX = picOffsetX;
        offSetY = picOffsetY;
        puzzleWidthLimit = limit;
        puzzleHeightLimit = limit;
        timeText.setAlignment(Label.LEFT);
        p1.add(timeText);
        parentVPuzzle.add(p1, BorderLayout.SOUTH);

        loaded = loadImage(picName);
        if(loaded)
        {
            this.numSegsX = numSegsX;
            picSegSizeX = (mainPic.getWidth(this) / numSegsX);
            this.numSegsY = numSegsY;
            picSegSizeY = (mainPic.getHeight(this) / numSegsY);
            
            positions = new PuzzleSegment[numSegsX][numSegsY];//Create the PuzzleSegment array
            scaleImage(mainPic);// set scaled segsizes for Puzzle
            
            for(int x = 0; x< numSegsX; x++)   //initialise each element
                for(int y = 0; y< numSegsY; y++)
                {
                    positions[x][y] = new PuzzleSegment(picSegSizeX*x, picSegSizeY*y, picSegSizeX, picSegSizeY);
                    //put actual positions of segs into array:
                    positions[x][y].setActualPos(scaledSegSizeX*x, scaledSegSizeY*y);
                }    
            
            //set position of blank seg as lower left
            blankIndexX = numSegsX;
            blankIndexY = numSegsY;
            blankIndexX--; blankIndexY--;
            
            //keep blank index source positions 0 to get through won check
            positions[blankIndexX][blankIndexY].correctsX = 0;
            positions[blankIndexX][blankIndexY].correctsY = 0;
            
            ready = true; //ready for user to play Puzzle
            //show();
            jumbleSegs(1000);//jumble up Puzzle before they start
            timer.start();// start the clock
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
        
        while(true)
        {
            timeElapsed +=1;
            timeText.setText(String.valueOf(timeElapsed));
            try{
                timer.sleep(1000);
                }catch(InterruptedException e){return;}
        }
    }

    public void paint(Graphics g)
    {
        g.setColor(Color.blue);
        
        if(ready)
        {
            //draw each seg in array with a nested for loop:
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
            //then draw a blank box at blank indexes
            g.fillRect(offSetX+positions[blankIndexX][blankIndexY].dX, 
                        offSetY+positions[blankIndexX][blankIndexY].dY, 
                        scaledSegSizeX,scaledSegSizeY);
        }
        if(won)
        {
            parentVPuzzle.updateUserScore(timeElapsed, numMovesMade);
            parentVPuzzle.showWinScreen(timeElapsed, numMovesMade);
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
        System.out.println("offSetX - "+offSetX);
        System.out.println("offSetY - "+offSetY);
        
        indexX = x/scaledSegSizeX;
        indexY = y/scaledSegSizeY;
        System.out.println("Found X - "+indexX);
        System.out.println("Found Y - "+indexY);
        swapSeg(indexX, indexY, true);
    }

    private boolean loadImage(String picName)
    {
        mainPic = toolkit.getImage(picName);
        track.addImage(mainPic, 0);
        try{
            track.waitForID(0);
            }catch(InterruptedException e){return(false);}
        if((mainPic.getWidth(this)== -1) && (mainPic.getHeight(this)== -1))
            return(false);
        else
            return(true);

    }

    private void scaleImage(Image pic1)
    {//scales the pic segment size by altering scaledSegSizeY & scaledSegSizeX

        if(loaded)
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
        
    }
    
    private void jumbleSegs(int numMoves)
    {
        int u = 0;int d = 0;int l = 0;int right = 0;
        for(int i=0; i<numMoves;i++)
        {
            double r = java.lang.Math.random()*10;
            
            if(r <= 2.5)
            {
                //System.out.println("up");    
                swapSeg(blankIndexX, blankIndexY-1, false);    
            }else            
            if( (r > 2.5) && (r <= 5))
            {
                //System.out.println("down");    
                swapSeg(blankIndexX, blankIndexY+1, false);    
            }else            
            if((r > 5) && (r <= 7.5))
            {
                //System.out.println("left");    
                swapSeg(blankIndexX-1, blankIndexY, false);    
            }else            
            if((r > 7.5) && (r <= 10))
            {
                //System.out.println("right");    
                swapSeg(blankIndexX+1, blankIndexY, false);    
            }
        }
    }
    
    private boolean swapSeg(int indexX, int indexY, boolean isPlayer)
    {                        // indexes of tile to swap with blank
        
        if( (indexX<0 || indexX>=numSegsX) || (indexY<0 || indexY>=numSegsY) )
        {// this is a check for invalid indexes being passed in
            //System.out.println("Out of bounds");
            return(false);
        }
        
        if( ((indexX == blankIndexX) && (java.lang.Math.abs(indexY-blankIndexY) == 1))
            || ((indexY == blankIndexY) && (java.lang.Math.abs(indexX-blankIndexX) == 1)) )
        {    //above is a check to make sure the indexes are adjecent to the blank indexes

            int tempPosX, tempPosY, tempSourceX, tempSourceY, tempBlSourceX, tempBlSourceY;

            //swap sources:
            tempSourceX = positions[indexX][indexY].sX;
            tempSourceY = positions[indexX][indexY].sY;
            
            positions[blankIndexX][blankIndexY].sX = tempSourceX;
            positions[blankIndexX][blankIndexY].sY = tempSourceY;
            
            blankIndexX = indexX;
            blankIndexY = indexY;
            
            //keep bl index sources 0 to get through win check
            positions[blankIndexX][blankIndexY].sX = 0;
            positions[blankIndexX][blankIndexY].sY = 0;
            
            if(isPlayer)
            {
                won = checkForWon();
                numMovesMade += 1;
            }
            
            repaint();
            
        }
        return(true); //success!
    }//end of swapSeg
    
    private boolean checkForWon()
    {
        for(int x = 0; x< numSegsX; x++)
        {
            for(int y= 0; y< numSegsY; y++)
            {
                if(positions[x][y].sX != positions[x][y].correctsX ||
                   positions[x][y].sY != positions[x][y].correctsY     )
                {
                    
                    System.out.println("\nChecking win: sX: "+positions[x][y].sX+" correctsX: "
                    +positions[x][y].correctsX);
                    System.out.println("Checking win: sY: "+positions[x][y].sY+" correctsY: "
                    +positions[x][y].correctsY);
                    return(false);
                }
            }
        }
        return(true);
    }        
    
}
