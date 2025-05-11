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

    public LinuxAppPackager(String jarPath, String outputPath)
    {
        super(jarPath, outputPath);
        fOutputDir = super.getOutputPath() + File.separator + "linux-pack";
    }

    @Override
    public void PackTheApp() throws Exception
    {

        Path outputDir = Paths.get(super.getOutputPath() + File.separator + "linux-pack");
        Files.deleteIfExists(outputDir);
        Files.createDirectories(outputDir);

        // Create run.sh
        String scriptContent = "#!/bin/bash\n"
                + "DIR=$(dirname \"$0\")\n"
                + "\"$DIR/jre/bin/java\" -jar \"$DIR/" + super.getJarFileName() + "\"\n";

        FileUtilities.SaveTextFile(fOutputDir + File.separator + "run.sh", scriptContent);

    }

}
