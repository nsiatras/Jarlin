package jarlin.AppPackaging;

import jarlin.Downloads.Events.IDownloadEventListener;
import jarlin.Downloads.FileDownloader;
import jarlin.Threading.ManualResetEvent;
import java.io.File;

/**
 *
 * @author Nikos Siatras
 */
public abstract class AppPackager
{

    private final String fJREDownloadURL;
    private final String fJarPath, fOutputPath;
    private final String fJarFileName;

    public AppPackager(String jreDownloadURL, String jarPath, String outputPath)
    {
        fJREDownloadURL = jreDownloadURL;
        fJarPath = jarPath;
        fOutputPath = outputPath;
        fJarFileName = fJarPath.substring(fJarPath.lastIndexOf(File.separator)).replace(File.separator, "");
    }

    /**
     * Download the appropriate JRE
     */
    public void DownloadJRE()
    {
        ManualResetEvent evt = new ManualResetEvent(false);
        
        FileDownloader downloader = new FileDownloader(fJREDownloadURL, fOutputPath);

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
                evt.Set();
            }

            @Override
            public void onDownloadFailed(Exception ex)
            {
                // ToDo
                System.err.println("JRE Download Failed!");
                System.err.println("Error: " + ex.getMessage());
                evt.Set();
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
        evt.WaitOne();
    }

    /**
     * Pack the App !
     *
     * @throws java.lang.Exception
     */
    public abstract void PackTheApp() throws Exception;

    /**
     * Returns the JAR path
     *
     * @return
     */
    public String getJarPath()
    {
        return fJarPath;
    }

    /**
     * Returns the output path
     *
     * @return
     */
    public String getOutputPath()
    {
        return fOutputPath;
    }

    /**
     * Returns the JAR's File name
     *
     * @return
     */
    public String getJarFileName()
    {
        return fJarFileName;
    }
}
