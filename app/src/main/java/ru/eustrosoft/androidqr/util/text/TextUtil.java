package ru.eustrosoft.androidqr.util.text;

public final class TextUtil {

    private TextUtil() {

    }

    public static String getCroppedText(String text, int max, boolean needDots) {
        if (text == null || text.isEmpty()) {
            return "";
        }
        StringBuilder builder = new StringBuilder();
        int length = Math.min(text.length(), max);
        builder.append(text.substring(0, length));
        if (needDots && (text.length() > max)) {
            builder.append("...");
        }
        return builder.toString();
    }
}
