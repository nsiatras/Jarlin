package jarlin.Utilities;

import java.io.*;


public class Unzipper
{

    private static final int TAR_BLOCK_SIZE = 512;

    /**
     * Extracts a .zip or a .tar.gz/.tgz archive to the given directory.
     *
     * @param archivePath path to the .zip or .tar.gz/.tgz file
     * @param destDir directory to extract into
     * @throws IOException on error
     */
    public static void extract(String archivePath, String destDir) throws IOException
    {
        String lower = archivePath.toLowerCase();
        if (lower.endsWith(".zip"))
        {
            unzip(archivePath, destDir);
        }
        else if (lower.endsWith(".tar.gz") || lower.endsWith(".tgz"))
        {
            untarGz(archivePath, destDir);
        }
        else
        {
            throw new IllegalArgumentException("Unsupported archive type: " + archivePath);
        }
    }

    private static void unzip(String zipFilePath, String destDir) throws IOException
    {
        File dir = new File(destDir);
        if (!dir.exists() && !dir.mkdirs())
        {
            throw new IOException("Cannot create output directory: " + destDir);
        }

        try (FileInputStream fis = new FileInputStream(zipFilePath); java.util.zip.ZipInputStream zis = new java.util.zip.ZipInputStream(fis))
        {

            java.util.zip.ZipEntry entry;
            byte[] buffer = new byte[4096];
            while ((entry = zis.getNextEntry()) != null)
            {
                File outFile = new File(destDir, entry.getName());
                if (entry.isDirectory())
                {
                    outFile.mkdirs();
                }
                else
                {
                    outFile.getParentFile().mkdirs();
                    try (FileOutputStream fos = new FileOutputStream(outFile))
                    {
                        int len;
                        while ((len = zis.read(buffer)) > 0)
                        {
                            fos.write(buffer, 0, len);
                        }
                    }
                }
                zis.closeEntry();
            }
        }
    }

    private static void untarGz(String tarGzPath, String destDir) throws IOException
    {
        File dir = new File(destDir);
        if (!dir.exists() && !dir.mkdirs())
        {
            throw new IOException("Cannot create output directory: " + destDir);
        }

        try (FileInputStream fis = new FileInputStream(tarGzPath); java.util.zip.GZIPInputStream gis = new java.util.zip.GZIPInputStream(fis); BufferedInputStream bis = new BufferedInputStream(gis))
        {

            byte[] headerBuf = new byte[TAR_BLOCK_SIZE];
            while (true)
            {
                // Read header block
                int read = bis.read(headerBuf);
                if (read < TAR_BLOCK_SIZE)
                {
                    break; // truncated
                }
                boolean allZero = true;
                for (byte b : headerBuf)
                {
                    if (b != 0)
                    {
                        allZero = false;
                        break;
                    }
                }
                if (allZero)
                {
                    break;  // end of archive
                }
                String name = parseName(headerBuf);
                long size = parseOctal(headerBuf, 124, 12);

                File outFile = new File(destDir, name);
                if (name.endsWith("/"))
                {
                    // directory entry
                    outFile.mkdirs();
                }
                else
                {
                    // file entry
                    outFile.getParentFile().mkdirs();
                    try (FileOutputStream fos = new FileOutputStream(outFile))
                    {
                        copy(bis, fos, size);
                    }
                }

                // skip padding to next 512-byte boundary
                long skipBytes = ((size + TAR_BLOCK_SIZE - 1) / TAR_BLOCK_SIZE) * TAR_BLOCK_SIZE - size;
                if (skipBytes > 0)
                {
                    long skipped = bis.skip(skipBytes);
                    while (skipped < skipBytes)
                    {
                        long s = bis.skip(skipBytes - skipped);
                        if (s <= 0)
                        {
                            break;
                        }
                        skipped += s;
                    }
                }
            }
        }
    }

    private static String parseName(byte[] header) throws UnsupportedEncodingException
    {
        // name is bytes 0–99, null-terminated
        int len = 0;
        while (len < 100 && header[len] != 0)
        {
            len++;
        }
        return new String(header, 0, len, "UTF-8");
    }

    private static long parseOctal(byte[] header, int offset, int length)
    {
        long result = 0;
        int end = offset + length;
        int i = offset;
        // skip leading spaces
        while (i < end && header[i] == ' ')
        {
            i++;
        }
        for (; i < end && header[i] >= '0' && header[i] <= '7'; i++)
        {
            result = (result << 3) + (header[i] - '0');
        }
        return result;
    }

    private static void copy(InputStream in, OutputStream out, long bytesToCopy) throws IOException
    {
        byte[] buffer = new byte[4096];
        long remaining = bytesToCopy;
        while (remaining > 0)
        {
            int toRead = (int) Math.min(buffer.length, remaining);
            int read = in.read(buffer, 0, toRead);
            if (read == -1)
            {
                break;
            }
            out.write(buffer, 0, read);
            remaining -= read;
        }
    }
}
