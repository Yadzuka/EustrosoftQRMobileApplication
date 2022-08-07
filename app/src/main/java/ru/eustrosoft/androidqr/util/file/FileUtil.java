package ru.eustrosoft.androidqr.util.file;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;

public final class FileUtil {

    private FileUtil() {

    }

    public static void copyFile(File source, File target) throws IOException {
        if (!source.exists()) {
            return;
        }
        FileChannel sourceChannel = new FileInputStream(source).getChannel();
        FileChannel destinationChannel = new FileOutputStream(target).getChannel();
        if (destinationChannel != null && sourceChannel != null) {
            destinationChannel.transferFrom(sourceChannel, 0, sourceChannel.size());
        }
        if (sourceChannel != null) {
            sourceChannel.close();
        }
        if (destinationChannel != null) {
            destinationChannel.close();
        }
    }

    public static void deleteFileTree(File file) throws IOException {
        File[] entries = file.listFiles();
        if (entries != null) {
            for (File entry : entries) {
                deleteFileTree(entry);
            }
        }
        if (!file.delete()) {
            throw new IOException("Failed to delete " + file);
        }
    }

}
