package ru.eustrosoft.androidqr.util;

import java.text.SimpleDateFormat;
import java.util.Date;

public final class DateUtil {
    private static final SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

    public static String getFormattedTime(Date time) {
        return timeFormat.format(time);
    }

    public static String getFormattedDate(Date date) {
        return dateFormat.format(date);
    }
}
