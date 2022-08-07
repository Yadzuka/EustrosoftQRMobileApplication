package ru.eustrosoft.androidqr.util.text;

public class SimpleTextSearch implements Searcher {
    private TextSearchParameters parameters;

    public SimpleTextSearch(TextSearchParameters parameters) {
        this.parameters = parameters;
    }

    public SimpleTextSearch() {
        this.parameters = new TextSearchParameters();
    }

    @Override
    public int searchInText(char[] text) {
        char[] pattern = parameters.getSearchText();
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
