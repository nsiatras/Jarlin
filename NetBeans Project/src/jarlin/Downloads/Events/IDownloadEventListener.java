package jarlin.Downloads.Events;

/**
 *
 * @author Nikos Siatras
 */
public interface IDownloadEventListener
{

    void onDownloadComplete();

    void onDownloadStarted();

    void onDownloadFailed(Exception ex);

    void onDownloadProgress(double progress);
}
