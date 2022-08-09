package ru.eustrosoft.androidqr.util.inet;

public enum HttpContentType {
    APPLICATION_JSON("application/json"),
    APPLICATION_XML("application/xml");

    private String value;

    private HttpContentType(String value) {
        this.value = value;
    }

    public String getValue() {
        return this.value;
    }
}
