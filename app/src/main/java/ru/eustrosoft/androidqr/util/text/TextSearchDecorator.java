package ru.eustrosoft.androidqr.util.text;

public class TextSearchDecorator {
    private Searcher searcher;
    private boolean ignoreCase;
    private char[] patternToSearch;

    public TextSearchDecorator() {
        this.searcher = new KnuthMorrisSearch();
        this.ignoreCase = false;
        this.patternToSearch = new char[0];
    }

    public TextSearchDecorator(boolean ignoreCase, char[] patternToSearch) {
        this.searcher = new KnuthMorrisSearch();
        this.ignoreCase = ignoreCase;
        this.patternToSearch = patternToSearch;
    }

    public TextSearchDecorator(boolean ignoreCase, char[] patternToSearch, Searcher searcher) {
        this.searcher = searcher;
        this.ignoreCase = ignoreCase;
        this.patternToSearch = patternToSearch;
    }

    public int searchInText(char[] text) {
        return this.searcher.searchInText(text, this);
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

    public Searcher getSearcher() {
        return searcher;
    }

    public void setSearcher(Searcher searcher) {
        this.searcher = searcher;
    }
}
