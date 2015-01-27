import java.awt.*;
import java.awt.event.*;
import java.io.*;

class userImageRecFileHandler extends baseFileHandler
{// extends class containing string handling methods
	long numRecs;
	String filename;
	UserImageRec ImageRec = new UserImageRec();
	
	public userImageRecFileHandler(String tempFileName) throws IOException
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
			
			ImageRec.picID = picID;
			
			ImageRec.picSetID = picSetID;
			
			ImageRec.completed = completed;
			ImageRec.diffLevel = 0;
			ImageRec.timeTaken = 0;
			ImageRec.pointsAwarded  = 0;
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
		{
			ImageRec.picID = 0;
			ImageRec.picSetID = "";
			ImageRec.completed = false;
			ImageRec.diffLevel = 0;
			ImageRec.timeTaken = 0;
			ImageRec.pointsAwarded = 0;
			writeUserRec(number);
		}
	}

	static void readRec(int number) throws IOException
	{
		RandomAccessFile rf = new RandomAccessFile(filename,"rw");
		long position = number*progrec.length;
		rf.seek(position);
		ImageRec.picID = rf.readInt();
		ImageRec.picSetID = rf.rafReadString(rf);
		
		ImageRec.completed = rf.readBoolean();
		ImageRec.diffLevel = rf.readInt();
		ImageRec.timeTaken = rf.readInt();
		ImageRec.pointsAwarded = rf.readInt();
		
		rf.close();
		
	
	}

	static void writeRec(int number) throws IOException
	{
		RandomAccessFile rf = new RandomAccessFile(filename,"rw");
		long position = number*progrec.length;
		rf.seek(position);
		
		rf.writeInt(ImageRec.picID);
		rafWriteString(rf,ImageRec.picSetID,40);
		//rafWriteString(rf,progrec.lName,40);
		rf.writeBoolean(ImageRec.completed);
		rf.writeInt(ImageRec.diffLevel);
		rf.writeInt(ImageRec.timeTaken);
		rf.writeInt(progrec.pupilImageFile);
		rf.writeChar(ImageRec.pointsAwarded);
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
	