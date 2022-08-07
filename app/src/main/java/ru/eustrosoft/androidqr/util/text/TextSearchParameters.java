package ru.eustrosoft.androidqr.util.text;

public class TextSearchParameters {
    private boolean ignoreCase;
    private char[] searchText;

    public TextSearchParameters() {
        this.ignoreCase = false;
        this.searchText = new char[0];
    }

    public TextSearchParameters(boolean ignoreCase, char[] searchText) {
        this.ignoreCase = ignoreCase;
        this.searchText = searchText;
    }

    public boolean isIgnoreCase() {
        return ignoreCase;
    }

    public void setIgnoreCase(boolean ignoreCase) {
        this.ignoreCase = ignoreCase;
    }

    public char[] getSearchText() {
        return searchText;
    }

    public void setSearchText(char[] searchText) {
        this.searchText = searchText;
    }
}
