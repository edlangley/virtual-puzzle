import java.awt.*;
import java.awt.event.*;
import java.io.*;

class UserProgressFileRec
{
    int pupilID = 0; //2 bytes
    String fName = "Empty"; //40 bytes + 2 for header
    String lName = "Empty"; //40 bytes + 2 for header
    int points  = 0; //2 bytes
    int diffLevel = 0; //2 bytes
    int numPuzzlesDone = 0; //2 bytes
    int pupilImageFile = 0; //2 bytes
    char status = 'E'; //2 bytes
    int length = 96;// bytes
}
