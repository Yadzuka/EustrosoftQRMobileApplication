package ru.eustrosoft.androidqr.util.text;

public class BoyerMooreHorspoolSearch implements Searcher {
    private TextSearchParameters parameters;

    public BoyerMooreHorspoolSearch(TextSearchParameters parameters) {
        this.parameters = parameters;
    }

    public BoyerMooreHorspoolSearch() {
        this.parameters = new TextSearchParameters();
    }

    @Override
    public int searchInText(char[] text) {
        int patternSize = parameters.getSearchText().length;
        int textSize = text.length;

        int i = 0, j = 0;

        while ((i + patternSize) <= textSize) {
            j = patternSize - 1;
            while (text[i + j] == parameters.getSearchText()[j]) {
                j--;
                if (j < 0)
                    return i;
            }
            i++;
        }
        return -1;
    }

    public int BoyerMooreHorspoolSearch(char[] pattern, char[] text) {
        int shift[] = new int[256];

        for (int k = 0; k < 256; k++) {
            shift[k] = pattern.length;
        }

        for (int k = 0; k < pattern.length - 1; k++) {
            shift[pattern[k]] = pattern.length - 1 - k;
        }

        int i = 0, j = 0;

        while ((i + pattern.length) <= text.length) {
            j = pattern.length - 1;

            while (text[i + j] == pattern[j]) {
                j -= 1;
                if (j < 0)
                    return i;
            }

            i = i + shift[text[i + pattern.length - 1]];

        }
        return -1;
    }
}
