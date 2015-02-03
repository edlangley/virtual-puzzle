import java.awt.*;
import java.awt.event.*;
import java.io.*;

class PicFileHandler extends BaseFileHandler
{// extends class containing string handling methods
    static long numRecs;
    static String filename;
    static PicFileRec ImageRec = new PicFileRec();
    
    public PicFileHandler(String tempFileName) throws IOException
    {
        // name of file passed down from global variable
        filename = tempFileName;
        RandomAccessFile f1 = new RandomAccessFile(filename,"rw");
        numRecs = (f1.length()/ImageRec.length);
    }
    
    static void addRec(String picSetID, int picID, String picName, 
        String picFileName, String picDirectory) throws IOException
    {
        
        numRecs++;
        int number = (int) numRecs;//increments numrecs
        
        if (recordInUse(number))
        {
            
            addRec(picSetID, picID, picName, picFileName, picDirectory); //reccurs until numRecs is unused
        }
        else
        {
            
            ImageRec.picSetID = picSetID;
            
            ImageRec.picID = picID;
            ImageRec.picName = picName;
            ImageRec.picFileName = picFileName;
            ImageRec.picDirectory = picDirectory;
            writeRec(number);
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
            ImageRec.picSetID = "";
            
            ImageRec.picID = 0;
            ImageRec.picName = "";
            ImageRec.picFileName = "";
            ImageRec.picDirectory = "";
            writeRec(number);
        }
    }

    static void readRec(int number) throws IOException
    {
        RandomAccessFile rf = new RandomAccessFile(filename,"rw");
        long position = number*ImageRec.length;// find the start of the rec
        rf.seek(position);// go to the start
        
        ImageRec.picSetID = BaseFileHandler.rafReadString(rf);
        ImageRec.picID = rf.readInt();
        ImageRec.picName = BaseFileHandler.rafReadString(rf);
        ImageRec.picFileName = BaseFileHandler.rafReadString(rf);
        ImageRec.picDirectory = BaseFileHandler.rafReadString(rf);
        rf.close();
    }

    static void writeRec(int number) throws IOException
    {
        RandomAccessFile rf = new RandomAccessFile(filename,"rw");
        long position = number*ImageRec.length;
        rf.seek(position);
        
        
        rafWriteString(rf,ImageRec.picSetID,40);
        rf.writeInt(ImageRec.picID);
        rafWriteString(rf,ImageRec.picName,40);
        rafWriteString(rf,ImageRec.picFileName,40);
        rafWriteString(rf,ImageRec.picDirectory,40);
        
        rf.close();
    }


    public static boolean recordInUse(int n) throws IOException
    {
        if (n > numRecs)
            return false;
        readRec(n);
        if(ImageRec.picName == "")
            return false;
        else
            return true;
    }
}
