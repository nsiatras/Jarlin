package jarlin.AppPackaging.Linux;

import jarlin.AppPackaging.AppPackager;
import jarlin.Utilities.FileUtilities;
import jarlin.Utilities.Unzipper;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 *
 * @author Nikos Siatras
 */
public class LinuxAppPackager extends AppPackager
{

    private String fOutputDir;

    public LinuxAppPackager(String jreDownloadURL, String jarPath, String exportPath)
    {
        super(jreDownloadURL, jarPath, exportPath);
        fOutputDir = super.getOutputPath() + File.separator + "linux-pack";
    }

    @Override
    public void PackTheApp() throws Exception
    {
        super.DownloadJRE();

        Path outputDir = Paths.get(super.getOutputPath() + File.separator + "linux-pack");
        Files.deleteIfExists(outputDir);
        Files.createDirectories(outputDir);

        // Create run.sh
        String scriptContent = "#!/bin/bash\n"
                + "DIR=$(dirname \"$0\")\n"
                + "\"$DIR/jre/bin/java\" -jar \"$DIR/" + super.getJarFileName() + "\"\n";

        FileUtilities.SaveTextFile(fOutputDir + File.separator + "run.sh", scriptContent);

        // Copy the Jar to export path
        Path sourcePath = Path.of(super.getJarPath());
        Path targetPath = Path.of(fOutputDir + File.separator + super.getJarFileName());
        Files.copy(sourcePath, targetPath);

        // Extract the Downloaded JRE to fOutputDir/jre
        String extractedJREPath = fOutputDir + File.separator + "jre";
        Unzipper.Extract(super.getDownloadedJREPath(), extractedJREPath);

        // Find "bin" path and move parent dir contents to "jre" 
        String parentOfBin = FindParentOfBin(new File(fOutputDir + File.separator + "jre"));
        if (!parentOfBin.equals(extractedJREPath))
        {
            FileUtilities.MoveDirContentsToOtherDir(parentOfBin, extractedJREPath);
        }

    }

}
