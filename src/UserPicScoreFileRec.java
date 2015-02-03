import java.awt.*;
import java.awt.event.*;
import java.io.*;

class UserPicScoreFileRec
{
    int picID = 0; //2 bytes
    String picSetID = "Empty"; //40 bytes + 2 for header
    boolean completed = false;//1 byte
    int diffLevel = 0; //2 bytes
    int timeTaken = 0; //2 bytes
    int pointsAwarded  = 0; //2 bytes
    int length = 51;// bytes
}
