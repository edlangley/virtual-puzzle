import java.awt.*;
import java.awt.event.*;
import java.io.*;

class userProgRecFileHandler extends baseFileHandler
{// extends class containing string handling methods
	long numRecs;
	String filename;
	userProgRec progrec = new userProgRec();
	
	public userProgFileHandler(String tempFileName) throws IOException
	{
		// name of file passed down from global variable
		filename = tempFileName;
		RandomAccessFile f1 = new RandomAccessFile(filename,"rw");
		numRecs = (f1.length()/progrec.length);
	}
	
	static void addRec(String firstName, String lastName) throws IOException
	{
		
		numRecs++;
		int number = numRecs;//increments numrecs
		
		if (recordInUse(number))
		{
			
			addRec(); //reccurs until numRecs is unused
		}
		else
		{
			
			progrec.fName = firstName;
			
			progrec.lName = lastName;
			progrec.status = 'U'; //U for in use/Used
			progrec.points  = 0;
			progrec.diffLevel = 0;
			progrec.numPuzzlesDone = 0;
			progrec.pupilImageFile = 0;
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
			progrec.fName = "";
			progrec.lName = "";
			progrec.points = 0;
			progrec.diffLevel = 0;
			progrec.numPuzzlesDone = 0;
			progrec.pupilImageFile = 0;
			progrec.status = 'E';
			writeUserRec(number);
		}
	}

	static void readUserRec(int number) throws IOException
	{
		RandomAccessFile rf = new RandomAccessFile(filename,"rw");
		long position = number*progrec.length;
		rf.seek(position);
		progrec.number = rf.readInt();
		progrec.fName = rf.rafReadString(rf);
		progrec.lName = rf.rafReadString(rf);
		progrec.points = rf.readInt();
		progrec.diffLevel = rf.readInt();
		progrec.numPuzzlesDone = rf.readInt();
		progrec.pupilImageFile = rf.readInt();
		progrec.status = rf.readChar();
		rf.close();
	}

	static void writeUserRec(int number) throws IOException
	{
		RandomAccessFile rf = new RandomAccessFile(filename,"rw");
		long position = number*progrec.length;
		rf.seek(position);
		rf.writeInt(progrec.number);
		rafWriteString(rf,progrec.fName,40);
		rafWriteString(rf,progrec.lName,40);
		rf.writeInt(progrec.points);
		rf.writeInt(progrec.diffLevel);
		rf.writeInt(progrec.numPuzzlesDone);
		rf.writeInt(progrec.pupilImageFile);
		rf.writeChar(progrec.status);
		
		rf.close();
	}
		

	public static boolean recordInUse(int n) throws IOException
	{
		if (n > numRecs)
			return false;
		readUserRec(n);
		if(progrec.status == 'E')
			return false;
		else
			return true;
	}
}
	