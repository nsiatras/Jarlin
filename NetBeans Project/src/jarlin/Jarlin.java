package jarlin;

import jarlin.AppPackaging.Linux.LinuxAppPackager;
import jarlin.Downloads.Events.IDownloadEventListener;
import jarlin.Downloads.FileDownloader;
import jarlin.Utilities.FileUtilities;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 *
 * @author Nikos Siatras
 */
public class Jarlin
{

    private static LinuxAppPackager fLinuxAppPackager;

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws Exception
    {
        final String JarPath = "C:\\Program Files\\SourceRabbit\\RabbitCAM\\RabbitCAM.jar";
        final String JREDownloadURL = "https://github.com/adoptium/temurin21-binaries/releases/download/jdk-21.0.7%2B6/OpenJDK21U-jre_x64_linux_hotspot_21.0.7_6.tar.gz";
        final String exportPath = "C:\\Users\\nsiat\\Desktop\\Test";

        boolean exportDirCreated = CreateExportDirectory(exportPath);
        if (!exportDirCreated)
        {
            return;
        }

        // Ask LinuxAppPacker to Pack the App for Linux
        fLinuxAppPackager = new LinuxAppPackager(JREDownloadURL, JarPath, exportPath);
        fLinuxAppPackager.PackTheApp();
    }

    private static boolean CreateExportDirectory(String exportPath)
    {
        // Check if savepath directory exists.
        // If not, then create it
        if (!FileUtilities.CheckIfDirectoryExists(exportPath))
        {
            // Create savepath directory
            try
            {
                Files.createDirectories(Paths.get(exportPath));
                return true;
            }
            catch (IOException ex)
            {
                System.err.println("Unable to create save path!");
                System.err.println("-- Save path: " + exportPath);
                System.err.println("-- Error: " + ex.getMessage());
            }
        }
        else
        {
            // ToDo: Check if dir is empty!
            return true;
        }

        return false;
    }

}
