package ru.eustrosoft.androidqr.util.text;

public class TextSearchDecorator {
    private TextSearch textSearch;
    private boolean ignoreCase;
    private char[] patternToSearch;

    public TextSearchDecorator() {
        this.textSearch = new KnuthMorrisSearch();
        this.ignoreCase = false;
        this.patternToSearch = new char[0];
    }

    public TextSearchDecorator(boolean ignoreCase, char[] patternToSearch) {
        this.textSearch = new KnuthMorrisSearch();
        this.ignoreCase = ignoreCase;
        this.patternToSearch = patternToSearch;
    }

    public TextSearchDecorator(boolean ignoreCase, char[] patternToSearch, TextSearch textSearchEngine) {
        this.textSearch = textSearchEngine;
        this.ignoreCase = ignoreCase;
        this.patternToSearch = patternToSearch;
    }

    public int searchInText(char[] text) {
        return this.textSearch.searchInText(text, this);
    }

    public boolean isIgnoreCase() {
        return ignoreCase;
    }

    public void setIgnoreCase(boolean ignoreCase) {
        this.ignoreCase = ignoreCase;
    }

    public char[] getPatternToSearch() {
        return patternToSearch;
    }

    public void setPatternToSearch(char[] patternToSearch) {
        this.patternToSearch = patternToSearch;
    }

    public TextSearch getTextSearch() {
        return textSearch;
    }

    public void setTextSearch(TextSearch textSearch) {
        this.textSearch = textSearch;
    }
}
