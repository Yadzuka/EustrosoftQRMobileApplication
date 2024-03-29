package ru.eustrosoft.androidqr.util.text;

/**
 * Interface for text searchers
 * All search classes must implement this interface
 */
public interface TextSearch {

    /**
     * Method, that needed to be implemented should realize one of search algorithms
     *
     * @param decorator
     * @param text
     * @return 0 or more if passed and -1 if text doesn't passed searching
     */
    int searchInText(char[] text, TextSearchDecorator decorator);
}
