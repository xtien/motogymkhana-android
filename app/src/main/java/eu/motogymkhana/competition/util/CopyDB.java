package eu.motogymkhana.competition.util;

import android.content.Context;
import android.os.Environment;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;

/**
 * Created by christine on 7-12-15.
 */
public class CopyDB {

    public static void copyDB(Context context, String dbName, String exportDirName) {

        File dbFile = new File(Environment.getDataDirectory() + "/data/" + context.getPackageName()
                + "/databases/" + dbName);

        if (!dbFile.exists()) {
            return;
        }

        File exportDir = new File("/sdcard/" + exportDirName);
        if (!exportDir.exists()) {
            exportDir.mkdirs();
        }

        File exportFile = new File(exportDir.getPath() + "/" + dbName);
        if (exportFile.exists()) {
            exportFile.delete();
        }

        try {
            exportFile.createNewFile();
            copyFile(dbFile, exportFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void restoreDB(Context context, String dbName, String importDirName) {

        File dbFile = new File(Environment.getDataDirectory() + "/data/" + context.getPackageName()
                + "/databases/" + dbName);

        if (!dbFile.exists()) {
            return;
        }

        File exportDir = new File("/sdcard/" + importDirName);
        if (!exportDir.exists()) {
            exportDir.mkdirs();
        }

        File importFile = new File(exportDir.getPath() + "/" + dbName);

        try {
            copyFile(importFile, dbFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void copyFile(File src, File dst) throws IOException {

        FileChannel inChannel = new FileInputStream(src).getChannel();
        FileChannel outChannel = new FileOutputStream(dst).getChannel();

        try {
            inChannel.transferTo(0, inChannel.size(), outChannel);

        } finally {
            if (inChannel != null) {
                inChannel.close();
            }
            if (outChannel != null) {
                outChannel.close();
            }
        }
    }
}
