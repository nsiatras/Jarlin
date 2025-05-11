package jarlin;

import jarlin.Downloads.Events.IDownloadEventListener;
import jarlin.Downloads.FileDownloader;

/**
 *
 * @author Nikos Siatras
 */
public class Jarlin
{

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args)
    {
        final String JDKPath = "https://github.com/adoptium/temurin21-binaries/releases/download/jdk-21.0.7%2B6/OpenJDK21U-jre_x64_linux_hotspot_21.0.7_6.tar.gz";
        final String savePath = "C:\\Users\\nsiat\\Desktop\\Test";

        FileDownloader downloader = new FileDownloader(JDKPath, savePath);

        // Initialize a download event listener
        IDownloadEventListener listener = new IDownloadEventListener()
        {
            @Override
            public void onDownloadStarted()
            {
                System.out.println("Downloading JRE...");
            }

            @Override
            public void onDownloadComplete()
            {
                System.out.println("");
                System.out.println("JRE Download finished!");

                // Java Runtime is not downloaded.
                // Begin the App Packaging
                BeginAppPackaging();
            }

            @Override
            public void onDownloadFailed(Exception ex)
            {
                // ToDo
            }

            @Override
            public void onDownloadProgress(double progress)
            {
                System.out.print("\rProgress: " + progress + " %");
            }
        };

        // Register the listener to the downloader.
        // The listener will guide the process from now on
        downloader.RegisterDownloadEventListener(listener);

        downloader.Download();
    }

    /**
     * ToDo
     */
    private static void BeginAppPackaging()
    {
        
    }

}
