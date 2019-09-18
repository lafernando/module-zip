package org.laf.libzip;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

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
           return new String[0];
        } finally {
            if (zf != null) {
                try {
                    zf.close();
                } catch (IOException ignore) { }
            }
        }
    }
    
}
