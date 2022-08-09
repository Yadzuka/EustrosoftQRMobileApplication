package ru.eustrosoft.androidqr.util.inet;

public class HttpResponse {
    private HttpMethod requestedMethod;
    private String body;
    private boolean ok;

    public HttpResponse() {
    }

    public HttpResponse(HttpMethod requestedMethod, String body, boolean ok) {
        this.requestedMethod = requestedMethod;
        this.body = body;
        this.ok = ok;
    }

    public HttpMethod getRequestedMethod() {
        return requestedMethod;
    }

    public void setRequestedMethod(HttpMethod requestedMethod) {
        this.requestedMethod = requestedMethod;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public boolean isOk() {
        return ok;
    }

    public void setOk(boolean ok) {
        this.ok = ok;
    }

    public <T> T mapResponseToObject() { // TODO
        return null;
    }
}
