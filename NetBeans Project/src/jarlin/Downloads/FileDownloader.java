package jarlin.Downloads;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

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
    }

    /**
     * Download the file
     *
     * @throws URISyntaxException
     * @throws MalformedURLException
     * @throws IOException
     */
    public void Download() throws URISyntaxException, MalformedURLException, IOException
    {
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
                    fProgress = (((double) downloadedFileSize * 100.00d) / (double) completeFileSize);
                    bout.write(data, 0, x1);
                }
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
     * Returns the downloads progress
     *
     * @return
     */
    public double GetDownloadProgress()
    {
        return fProgress;
    }
}
