package ru.eustrosoft.androidqr.util;

import android.annotation.SuppressLint;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Objects;

public final class FileSize {

    private FileSize() {

    }

    @SuppressLint("DefaultLocale")
    public static String getPropertyFileSize(File file) throws FileNotFoundException {
        if (file == null) {
            throw new NullPointerException();
        }
        if (!file.exists()) {
            throw new FileNotFoundException();
        }
        long fileSize = file.length();
        float actualFileSize = fileSize;
        int position = 0;
        while (hasOneInSize(actualFileSize)) {
            actualFileSize = actualFileSize / 1024;
            position++;
        }
        String title = Objects.requireNonNull(FileSizeTitles.BYTES.getTitleByPosition(position)).value;
        return String.format("%,.2f %s", actualFileSize, title);
    }

    private static boolean hasOneInSize(float size) {
        float one = size / 1024;
        return one >= 1;
    }

    enum FileSizeTitles {
        BYTES("bytes", 0),
        KILOBYTE("kilobytes", 1),
        MEGABYTE("megabytes", 2),
        GIGABYTE("gigabytes", 3),
        PETABYTE("petabytes", 4);

        public String value;
        public int position;

        FileSizeTitles(String value, int position) {
            this.value = value;
            this.position = position;
        }

        public FileSizeTitles getTitleByPosition(int pos) {
            if (pos < 0 || pos > 4) {
                return null;
            }
            for (FileSizeTitles title : FileSizeTitles.values()) {
                if (title.position == pos) {
                    return title;
                }
            }
            return null;
        }
    }
}
