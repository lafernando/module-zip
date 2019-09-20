package org.laf.libzip;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

/**
 * Class to handle zip utility operations.
 */
public class ZipUtils {

    public static String[] listEntries(String path) {
        List<String> result = new ArrayList<String>();
        ZipFile zf = null;
        try {
            zf = new ZipFile(path);
            Enumeration<? extends ZipEntry> entries = zf.entries();
            while (entries.hasMoreElements()) {
                ZipEntry ze = entries.nextElement();
                result.add(ze.getName());
            }
            return result.toArray(new String[result.size()]);
        } catch (Exception e) {
           throw new RuntimeException(e);
        } finally {
            if (zf != null) {
                try {
                    zf.close();
                } catch (IOException ignore) { }
            }
        }
    }

    public static void zip(String sourceDir, String targetZipFile) {
        File file = new File(sourceDir);
        if (!file.isDirectory()) {
            return;
        }
        FileOutputStream fileOut = null;
        ZipOutputStream zipOut = null;
        try {
            fileOut = new FileOutputStream(targetZipFile);
            zipOut = new ZipOutputStream(fileOut);
            for (File child : file.listFiles()) {
                addZipEntry(child, child.getName(), zipOut);
            }
            zipOut.close();
            fileOut.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            if (zipOut != null) {
                try {
                    zipOut.close();
                } catch (IOException ignore) { }
            }
            if (fileOut != null) {
                try {
                    fileOut.close();
                } catch (IOException ignore) { }
            }
        }
    }

    private static void addZipEntry(File file, String targetPath, ZipOutputStream zipOut) throws IOException {
        if (file.isDirectory()) {
            if (!targetPath.endsWith("/")) {
                targetPath += "/";
            }
            zipOut.putNextEntry(new ZipEntry(targetPath));
            zipOut.closeEntry();
            for (File child : file.listFiles()) {
                addZipEntry(child, targetPath + child.getName(), zipOut);
            }
        } else {
            FileInputStream fileIn = new FileInputStream(file);
            try {
                ZipEntry ze = new ZipEntry(targetPath);
                zipOut.putNextEntry(ze);
                byte[] buff = new byte[1024];
                int i;
                while ((i = fileIn.read(buff)) > 0) {
                    zipOut.write(buff, 0, i);
                }
            } finally {
                fileIn.close();
            }
        }
    }
    
    public static void unzip(String sourceZipFile, String targetDir) {
        File targetDirFile = new File(targetDir);
        if (targetDirFile.isFile()) {
            throw new RuntimeException("Target must be a directory for unzip");
        }
        if (!targetDirFile.exists()) {
            targetDirFile.mkdirs();
        }
        ZipInputStream zipIn = null;
        try {
            zipIn = new ZipInputStream(new FileInputStream(sourceZipFile));
            ZipEntry ze = zipIn.getNextEntry();
            while (ze != null) {
                File file = createFile(targetDirFile, ze);
                if (ze.isDirectory()) {
                    file.mkdirs();
                } else {
                    FileOutputStream fileOut = new FileOutputStream(file);
                    int i;
                    byte[] buff = new byte[1024];
                    while ((i = zipIn.read(buff)) > 0) {
                        fileOut.write(buff, 0, i);
                    }
                    fileOut.close();
                }
                ze = zipIn.getNextEntry();
            }
            zipIn.closeEntry();
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        } finally {
            if (zipIn != null) {
                try {
                    zipIn.close();
                } catch (IOException ignore) { }
            }
        }
    }
    
    private static File createFile(File dir, ZipEntry ze) throws IOException {
        File file = new File(dir, ze.getName());
        String dirPath = dir.getCanonicalPath();
        String filePath = file.getCanonicalPath();
        if (!filePath.startsWith(dirPath + File.separator)) {
            throw new IOException("Invalid zip entry: " + ze.getName());
        }
        return file;
    }

}
