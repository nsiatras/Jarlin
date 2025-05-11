package jarlin.AppPackaging;

import java.io.File;

/**
 *
 * @author Nikos Siatras
 */
public abstract class AppPackager
{

    private final String fJarPath, fOutputPath;
    private final String fJarFileName;

    public AppPackager(String jarPath, String outputPath)
    {
        fJarPath = jarPath;
        fOutputPath = outputPath;
        fJarFileName = fJarPath.substring(fJarPath.lastIndexOf(File.separator)).replace(File.separator, "");
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
