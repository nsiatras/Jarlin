package jarlin.AppPackaging.Linux;

import jarlin.AppPackaging.AppPackager;
import jarlin.Utilities.FileUtilities;
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

    public LinuxAppPackager(String jreDownloadURL, String jarPath, String outputPath)
    {
        super(jreDownloadURL, jarPath, outputPath);
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

        // Copy the Jar
        Path sourcePath = Path.of(super.getJarPath());
        Path targetPath = Path.of(fOutputDir + File.separator + super.getJarFileName());
        Files.copy(sourcePath, targetPath);

        // Extract the JRE
    }

}
