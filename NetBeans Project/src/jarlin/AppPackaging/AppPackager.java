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
    protected String fDownloadedJREPath = "";
    private final String fJarPath, fOutputPath, fJarFileName;

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
                // Set the fDownloadedJREPath
                fDownloadedJREPath = fOutputPath + File.separator + fJREDownloadURL.substring(fJREDownloadURL.lastIndexOf("/") + 1);

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
     * Returns the parent directory path, of the "bin" directory
     *
     * @param dir
     * @return
     */
    public static String FindParentOfBin( File dir)
    {

        if (!dir.exists() || !dir.isDirectory())
        {
            return null;
        }

        File[] files = dir.listFiles();
        if (files == null)
        {
            return null;
        }

        for (File file : files)
        {
            if (file.isDirectory())
            {
                if (file.getName().equals("bin"))
                {
                    return file.getParent();
                }
                else
                {
                    String result = FindParentOfBin(file);
                    if (result != null)
                    {
                        return result;
                    }
                }
            }
        }
        return null;
    }

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

    /**
     * Returns the downloaded JRE path. This string is empty if the JRE has not
     * been downloaded yet!
     *
     * @return
     */
    public String getDownloadedJREPath()
    {
        return fDownloadedJREPath;
    }
}
