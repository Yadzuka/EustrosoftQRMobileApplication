package ru.eustrosoft.androidqr.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

public final class DateUtil {
    private static final SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
    private static final SimpleDateFormat dateTimeFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");

    public static String getFormattedTime(Date time) {
        Objects.requireNonNull(time);
        return timeFormat.format(time);
    }

    public static String getFormattedDate(Date date) {
        Objects.requireNonNull(date);
        return dateFormat.format(date);
    }

    public static String getFormattedDateAndTime(Date dateTime) {
        Objects.requireNonNull(dateTime);
        return dateTimeFormat.format(dateTime);
    }
}
