import java.awt.*;
import java.awt.event.*;
import java.awt.Toolkit.*;
import java.awt.image.*;
import java.util.*;

class picSegment
{
	//int itsPosX, itsPosY;
	//int sourcePosX, sourcePosY;
	int sX,sY,dX,dY;
	int correctsX, correctsY;
	
	Toolkit toolkit = Toolkit.getDefaultToolkit();
	
	public picSegment(int posX, int posY, int width, int height)
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

	/* move to keydown event handler
	public void moveSegment(int direction)
	{
		switch(direction)
		{
			case(1):
					System.out.println("Up");break;
			case(2):
				System.out.println("Down");break;
			case(3):
				System.out.println("Left");break;
			case(4):
				System.out.println("Right");break;
			default:System.out.println("You shouldn't get here");
		}
	}*/
}//end of picSegment
	