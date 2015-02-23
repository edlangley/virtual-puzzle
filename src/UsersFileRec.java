import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

class UsersFileRec implements Serializable
{
    String fName = "Empty";
    String lName = "Empty";
    int diffLevel = 0;
    int numPuzzlesDone = 0;
    List<UserPuzzleScoreRec> puzzleScores = new ArrayList<>();
}
