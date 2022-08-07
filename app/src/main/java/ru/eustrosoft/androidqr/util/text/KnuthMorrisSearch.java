package ru.eustrosoft.androidqr.util.text;

public class KnuthMorrisSearch implements Searcher {
    private TextSearchParameters parameters;

    public KnuthMorrisSearch(TextSearchParameters parameters) {
        this.parameters = parameters;
    }

    public KnuthMorrisSearch() {
        this.parameters = new TextSearchParameters();
    }

    private int[] KnuthMorrisPrattShift(char[] pattern) {
        int patternSize = pattern.length;

        int[] shift = new int[patternSize];
        shift[0] = 1;

        int i = 1, j = 0;

        while ((i + j) < patternSize) {
            if (pattern[i + j] == pattern[j]) {
                shift[i + j] = i;
                j++;
            } else {
                if (j == 0)
                    shift[i] = i + 1;

                if (j > 0) {
                    i = i + shift[j - 1];
                    j = Math.max(j - shift[j - 1], 0);
                } else {
                    i = i + 1;
                    j = 0;
                }
            }
        }
        return shift;
    }

    @Override
    public int searchInText(char[] text) {
        char[] pattern = parameters.getSearchText();
        if (parameters.isIgnoreCase()) {
            pattern = new String(pattern).toLowerCase().toCharArray();
            text = new String(text).toLowerCase().toCharArray();
        }
        int patternSize = pattern.length; // m
        int textSize = text.length; // n

        int i = 0, j = 0;

        int[] shift = KnuthMorrisPrattShift(pattern);

        while ((i + patternSize) <= textSize) {
            while (text[i + j] == pattern[j]) {
                j += 1;
                if (j >= patternSize)
                    return i;
            }

            if (j > 0) {
                i += shift[j - 1];
                j = Math.max(j - shift[j - 1], 0);
            } else {
                i++;
                j = 0;
            }
        }
        return -1;
    }
}
