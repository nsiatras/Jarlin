
package jarlin.Utilities;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

/**
 *
 * @author Nikos Siatras
 */
public class FileUtilities
{
    public FileUtilities()
    {

    }

    /**
     * Returns true if the directory exists
     *
     * @param path
     * @return
     */
    public static boolean CheckIfDirectoryExists(String path)
    {
        File dir = new File(path);
        return dir.exists();
    }

    /**
     * Returns true if the directory exists and it is empty
     *
     * @param path
     * @return
     */
    public static boolean CheckIfDirectoryExistsAndItIsEmpty(String path)
    {
        File dir = new File(path);
        boolean dirIsEmpty = false;
        if (dir.exists())
        {
            dirIsEmpty = dir.listFiles().length == 0;
        }

        return dirIsEmpty;
    }

    /**
     *
     * @param sourceDirPath
     * @param destinationDirPath
     * @return
     */
    public static boolean MoveDirContentsToOtherDir(String sourceDirPath, String destinationDirPath)
    {
        File sourceFile = new File(sourceDirPath);
        File destFile = new File(destinationDirPath);

        if (sourceFile.isDirectory())
        {
            String destinationDir = sourceFile.getAbsolutePath().replace(sourceDirPath, destinationDirPath);
            File tempDestDir = new File(destinationDir);
            tempDestDir.mkdir();

            for (File file : sourceFile.listFiles())
            {
                MoveDirContentsToOtherDir(file.getAbsolutePath(), destinationDir + "\\" + file.getName());
            }

            try
            {
                Files.delete(Paths.get(sourceFile.getPath()));
            }
            catch (IOException ex)
            {
                return false;
            }
        }
        else
        {
            try
            {
                Files.move(Paths.get(sourceFile.getPath()), Paths.get(destFile.getPath()), StandardCopyOption.REPLACE_EXISTING);
                return true;
            }
            catch (IOException e)
            {
                return false;
            }
        }
        return false;
    }

    public static String ReadTextFile(String filePath) throws IOException
    {
        String result = "";
        try (FileReader fr = new FileReader(filePath))
        {
            int i;
            while ((i = fr.read()) != -1)
            {
                result += String.valueOf((char) i);
            }
            fr.close();
        }
        catch (FileNotFoundException ex)
        {

        }
        return result;
    }

    public static void SaveTextFile(String filePath, String data) throws IOException
    {
        try (FileWriter myWriter = new FileWriter(filePath, false))
        {
            myWriter.write(data);
        }
    }
}
