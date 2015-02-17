import java.awt.*;
import java.awt.event.*;
import java.io.*;

class PuzzlesFileRec implements Serializable
{
//    String picSetID = "";         // 40 bytes + 2 for header
//    int picID = 0;                //  2 bytes
    String picName = "";          // 40 bytes + 2 for header
    String picFileName = "";      // 40 bytes + 2 for header
//    String picDirectory = "";     // 40 bytes + 2 for header
//    int length = 170;             // bytes
    int numSegmentsX = 2;
    int numSegmentsY = 2;
    
    PuzzlesFileRec()
    {
        
    }
    
    PuzzlesFileRec(PuzzlesFileRec other)
    {
//        this.picSetID = new String(other.picSetID);
//        this.picID = other.picID;
        this.picName = new String(other.picName);
        this.picFileName = new String(other.picFileName);
//        this.picDirectory = new String(other.picDirectory);
//        this.length = other.length;
        this.numSegmentsX = other.numSegmentsX;
        this.numSegmentsY = other.numSegmentsY;
    }
}
