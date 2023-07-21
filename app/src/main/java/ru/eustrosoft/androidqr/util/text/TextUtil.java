package ru.eustrosoft.androidqr.util.text;

public final class TextUtil {

    private TextUtil() {

    }

    public static String getCroppedText(String text, int max, boolean needDots) {
        if (isEmptyString(text)) {
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

    public static String getChangedSymbolsText(String text, String replacement, int cropLength) {
        if (isEmptyString(text)) {
            return "";
        }
        return getCroppedText(
                text.replaceAll(".", replacement),
                cropLength,
                false
        );
    }

    private static String getEmptyStringIfEmpty(String str) {
        if (str == null || str.isEmpty()) {
            return "";
        }
        return str;
    }

    private static boolean isEmptyString(String str) {
        return str == null || str.length() == 0;
    }
}
