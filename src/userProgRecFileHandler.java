import java.awt.*;
import java.awt.event.*;
import java.io.*;

class userProgRecFileHandler extends baseFileHandler
{// extends class containing string handling methods
	static long numRecs;
	static String filename;
	static userProgRec progRec = new userProgRec();
	
	public userProgRecFileHandler(String tempFileName) throws IOException
	{
		// name of file passed down from global variable
		filename = tempFileName;
		RandomAccessFile f1 = new RandomAccessFile(filename,"rw");
		numRecs = (f1.length()/progRec.length);
	}
	
	static void addRec(String firstName, String lastName) throws IOException
	{
		
		numRecs++;
		int number = (int) numRecs;//increments numrecs
		
		if (recordInUse(number))
		{
			
			addRec(firstName, lastName); //reccurs until numRecs is unused
		}
		else
		{
			
			progRec.fName = firstName;
			
			progRec.lName = lastName;
			progRec.status = 'U'; //U for in use/Used
			progRec.points  = 0;
			progRec.diffLevel = 0;
			progRec.numPuzzlesDone = 0;
			progRec.pupilImageFile = 0;
			writeUserRec(number);
		}
	}

	static void delRec(int number) throws IOException
	{
		
		if (!recordInUse(number))
		{
			System.out.println("that record is empty");
		}
		else
		{// reset all the fields to blank
			progRec.fName = "";
			progRec.lName = "";
			progRec.points = 0;
			progRec.diffLevel = 0;
			progRec.numPuzzlesDone = 0;
			progRec.pupilImageFile = 0;
			progRec.status = 'E';
			writeUserRec(number);
		}
	}

	static void readUserRec(int number) throws IOException
	{
		RandomAccessFile rf = new RandomAccessFile(filename,"rw");
		long position = number*progRec.length;
		rf.seek(position);
		progRec.pupilID = rf.readInt();
		progRec.fName = baseFileHandler.rafReadString(rf);
		progRec.lName = baseFileHandler.rafReadString(rf);
		progRec.points = rf.readInt();
		progRec.diffLevel = rf.readInt();
		progRec.numPuzzlesDone = rf.readInt();
		progRec.pupilImageFile = rf.readInt();
		progRec.status = rf.readChar();
		rf.close();
	}

	static void writeUserRec(int number) throws IOException
	{
		RandomAccessFile rf = new RandomAccessFile(filename,"rw");
		long position = number*progRec.length;
		rf.seek(position);
		rf.writeInt(progRec.pupilID);
		rafWriteString(rf,progRec.fName,40);
		rafWriteString(rf,progRec.lName,40);
		rf.writeInt(progRec.points);
		rf.writeInt(progRec.diffLevel);
		rf.writeInt(progRec.numPuzzlesDone);
		rf.writeInt(progRec.pupilImageFile);
		rf.writeChar(progRec.status);
		
		rf.close();
	}
		

	public static boolean recordInUse(int n) throws IOException
	{
		if (n > numRecs)
			return false;
		readUserRec(n);
		if(progRec.status == 'E')
			return false;
		else
			return true;
	}
}
	