package ru.eustrosoft.androidqr.util.text;

public class BoyerMooreHorspoolSearch implements Searcher {

    public BoyerMooreHorspoolSearch() {
    }

    @Override
    public int searchInText(char[] text, TextSearchDecorator decorator) {
        char[] pattern = decorator.getPatternToSearch();
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
