import java.awt.*;
import java.awt.event.*;
import java.io.*;

class baseFileHandler
{// thse are the string handlers used by the other file handlers
    public static void rafWriteString(RandomAccessFile raf, String s,
        int fldSize)throws IOException
    {
        int strlen = s.length();
        if(fldSize > 255) fldSize = 255;
        if(strlen>fldSize) strlen = fldSize;
        int dataSize = fldSize+2;
        byte[] dest = new byte[dataSize];
        dest[0] = (byte)fldSize; //size of the field
        dest[1] = (byte)strlen;  //length of the string
        s.getBytes(0,strlen,dest,2);
        raf.write(dest,0,dataSize);
    }

    public static String rafReadString(RandomAccessFile raf)
    throws IOException
    {
        int size = raf.read();
        int strlen = raf.read();
        byte[] src = new byte[size];
        raf.read(src);
        String s = new String(src,0,strlen);
        return s;
    }
}
