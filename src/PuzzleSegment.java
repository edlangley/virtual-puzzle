import java.awt.*;
import java.awt.event.*;
import java.awt.Toolkit.*;
import java.awt.image.*;
import java.util.*;

class PuzzleSegment
{
    int sX, sY, dX, dY;
    int correctsX, correctsY;
    
    Toolkit toolkit = Toolkit.getDefaultToolkit();
    
    public PuzzleSegment(int posX, int posY, int width, int height)
    {
        sX = posX;
        sY = posY;
        correctsX = sX;
        correctsY = sY;
    }

    public void setActualPos(int x, int y)
    {
        dX = x;
        dY = y;
    }
}
