package ru.eustrosoft.androidqr.util.text;

public class SimpleTextSearch implements Searcher {
    public SimpleTextSearch() {
    }

    @Override
    public int searchInText(char[] text, TextSearchDecorator decorator) {
        char[] pattern = decorator.getPatternToSearch();
        int patternSize = pattern.length;
        int textSize = text.length;

        int i = 0;

        while ((i + patternSize) <= textSize) {
            int j = 0;
            while (text[i + j] == pattern[j]) {
                j += 1;
                if (j >= patternSize)
                    return i;
            }
            i += 1;
        }

        return -1;
    }
}
