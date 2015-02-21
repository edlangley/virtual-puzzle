import java.io.EOFException;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;


public class FileHandler<T>
{
    private String fileName;
    private List<T> list = new ArrayList<>();
    
    public FileHandler(String recFileName)
    {
        fileName = new String(recFileName);
        ObjectInputStream objInStream = null;
        
        try
        {
            objInStream = new ObjectInputStream(new FileInputStream(fileName));
            
        }
        catch(FileNotFoundException fileNotFoundException)
        {
            System.err.println("File " + fileName + " not found, will be created on saving.");
            return;
        }
        catch(IOException ioException)
        {
            System.err.println("IO error opening file " + fileName + ".");
        }
        
        try
        {
            while(true)
            {
                T fRec = (T) objInStream.readObject();
                list.add(fRec);
            }
        }
        catch(EOFException eofException)
        {
            return;
        }
        catch(ClassNotFoundException classNotFoundException)
        {
            System.err.println("Object creation failed.");
        }
        catch(IOException ioException)
        {
            System.err.println("IO error reading file " + fileName + ".");
        }
        finally
        {
            try
            {
                if(objInStream != null)
                    objInStream.close();
            }
            catch(IOException ioException)
            {
                System.err.println("Error closing file " + fileName + ".");
            }
        }
    }
    
    public T readRec(int recIx)
    {
        return list.get(recIx);
    }
    
    public void addRec(T rec)
    {
        list.add(rec);
        outputToFile();
    }
    
    public void updateRec(int recIx, T rec)
    {
        list.set(recIx, rec);
        outputToFile();
    }
    
    public void removeRec(int recIx)
    {
        list.remove(recIx);
        outputToFile();
    }
    
    public int numRecords()
    {
        return list.size();
    }
    
    private void outputToFile()
    {
        ObjectOutputStream objOutStream = null;
        FileOutputStream fileOutStream = null;
        try
        {
            fileOutStream = new FileOutputStream(fileName);
        }
        catch(FileNotFoundException fileNotFoundException)
        {
            System.err.println("Creating file " + fileName + ".");
            try
            {
                Path path = FileSystems.getDefault().getPath(fileName);
                fileOutStream = (FileOutputStream)Files.newOutputStream(path);
                for(T rec : list)
                {
                    objOutStream.writeObject(rec);
                }
            }
            catch(IOException ioException)
            {
                System.err.println("IO error creating file " + fileName + ".");
            }
        }
        
        try
        {
            objOutStream = new ObjectOutputStream(fileOutStream);
            for(T rec : list)
            {
                objOutStream.writeObject(rec);
            }
        }
        catch(IOException ioException)
        {
            System.err.println("IO error writing to file " + fileName + ".");
            ioException.printStackTrace();
        }
        catch(NoSuchElementException noSuchElementException)
        {
            System.err.println("Invalid input.");
        }
        finally
        {
            try
            {
                if(objOutStream != null)
                    objOutStream.close();
            }
            catch(IOException ioException)
            {
                System.err.println("IO error closing file " + fileName + ".");
            }
        }
    }
}
