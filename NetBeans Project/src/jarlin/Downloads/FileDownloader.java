package jarlin.Downloads;

import java.io.BufferedOutputStream;
import java.io.File;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import jarlin.Downloads.Events.IDownloadEventListener;

/**
 *
 * @author Nikos Siatras
 */
public class FileDownloader
{

    private final String fURL;
    private final String fTargetDirectory;
    private boolean fKeepDownloadThreadRunning = false;
    private double fProgress = 0;

    private final ArrayList<IDownloadEventListener> fDownloadEventListeners;

    /**
     * Initialize a new File Downloader
     *
     * @param url the URL of the file to download
     * @param targetDirectory the directory's path to download the file
     */
    public FileDownloader(String url, String targetDirectory)
    {
        fURL = url;
        fTargetDirectory = targetDirectory;
        fDownloadEventListeners = new ArrayList<>();
    }

    /**
     * Download the file
     *
     */
    public void Download()
    {
        try
        {
            // Download Started !
            // Inform event listeners
            for (IDownloadEventListener listener : fDownloadEventListeners)
            {
                listener.onDownloadStarted();
            }

            fKeepDownloadThreadRunning = true;
            fProgress = 0;

            URL url = new URI(fURL).toURL();
            final String targetFilePath = fTargetDirectory + File.separator + fURL.substring(fURL.lastIndexOf("/")).replace("/", "");

            HttpURLConnection httpConnection = (HttpURLConnection) (url.openConnection());
            long completeFileSize = httpConnection.getContentLength();
            try (java.io.BufferedInputStream in = new java.io.BufferedInputStream(httpConnection.getInputStream()))
            {
                java.io.FileOutputStream fos = new java.io.FileOutputStream(targetFilePath);
                try (java.io.BufferedOutputStream bout = new BufferedOutputStream(fos, 1024))
                {
                    byte[] data = new byte[1024];
                    long downloadedFileSize = 0;

                    int x1 = 0;
                    while (fKeepDownloadThreadRunning && (x1 = in.read(data, 0, 1024)) >= 0)
                    {
                        downloadedFileSize += x1;

                        // Update Progress
                        fProgress = Math.round((((double) downloadedFileSize * 100.00d) / (double) completeFileSize) * 100.0) / 100.0;

                        // Inform event listeners for download progress
                        for (IDownloadEventListener listener : fDownloadEventListeners)
                        {
                            listener.onDownloadProgress(fProgress);
                        }
                        bout.write(data, 0, x1);
                    }
                }
            }

            // Download finished !
            // Inform event listeners
            for (IDownloadEventListener listener : fDownloadEventListeners)
            {
                listener.onDownloadComplete();
            }
        }
        catch (Exception ex)
        {
            // Download failed !
            // Inform event listeners 
            for (IDownloadEventListener listener : fDownloadEventListeners)
            {
                listener.onDownloadFailed(ex);
            }
        }
    }

    /**
     * Stops the download
     */
    public void Stop()
    {
        fKeepDownloadThreadRunning = false;
    }

    /**
     * Add a download event listener
     *
     * @param listener the listener that handles the events of the download
     */
    public void RegisterDownloadEventListener(IDownloadEventListener listener)
    {
        if (!fDownloadEventListeners.contains(listener))
        {
            fDownloadEventListeners.add(listener);
        }
    }

    /**
     * Remove a DownloadEventListener
     *
     * @param listener the listener to be removed
     */
    public void UnregisterDownloadEventListener(IDownloadEventListener listener)
    {
        if (fDownloadEventListeners.contains(listener))
        {
            fDownloadEventListeners.remove(listener);
        }
    }

    /**
     * Returns the downloads progress
     *
     * @return
     */
    public double GetDownloadProgress()
    {
        return fProgress;
    }
}
