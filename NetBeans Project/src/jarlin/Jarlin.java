package jarlin;

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
    public static void main(String[] args) throws Exception
    {
        final String JDKPath = "https://github.com/adoptium/temurin21-binaries/releases/download/jdk-21.0.7%2B6/OpenJDK21U-jre_x64_linux_hotspot_21.0.7_6.tar.gz";
        final String savePath = "C:\\Users\\nsiat\\Desktop\\Test";

        FileDownloader downloader = new FileDownloader(JDKPath, savePath);
        downloader.Download();
    }

}
