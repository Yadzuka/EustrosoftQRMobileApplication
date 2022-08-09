package ru.eustrosoft.androidqr.util.inet;

import android.accounts.NetworkErrorException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Map;

public class HttpRequest {
    private URL url;
    private HttpMethod httpMethod = HttpMethod.GET;
    private HttpContentType contentType = HttpContentType.APPLICATION_JSON;
    private String body;
    private Map<String, String> headers;

    private HttpRequest() {

    }

    public static HttpRequestBuilder builder(URL url) {
        return new HttpRequestBuilder(url);
    }

    public HttpResponse request() throws NetworkErrorException {
        if (this.url == null) {
            throw new NetworkErrorException("URL is not set");
        }
        HttpResponse httpResponse = new HttpResponse();
        httpResponse.setRequestedMethod(httpMethod);
        try {
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod(httpMethod.name());
            urlConnection.setDoInput(true);
            headers.entrySet().forEach((entity) ->
                    urlConnection.setRequestProperty(entity.getKey(), entity.getValue())
            );
            urlConnection.setRequestProperty("Content-Type", contentType.getValue());
            urlConnection.setRequestProperty("Accept", contentType.getValue());
            if (!httpMethod.equals(HttpMethod.GET)) {
                urlConnection.setDoOutput(true);
                try (OutputStream os = urlConnection.getOutputStream()) {
                    byte[] input = body.getBytes(StandardCharsets.UTF_8);
                    os.write(input, 0, input.length);
                }
            }
            try (BufferedReader reader = new BufferedReader(
                    new InputStreamReader(urlConnection.getInputStream(), StandardCharsets.UTF_8))) {
                String responseLine = null;
                StringBuilder response = new StringBuilder();
                while ((responseLine = reader.readLine()) != null) {
                    response.append(responseLine.trim());
                }
                httpResponse.setBody(response.toString());
            }
            httpResponse.setOk(true);
        } catch (IOException ex) {
            httpResponse.setOk(false);
        } finally {
            return httpResponse;
        }
    }

    public static class HttpRequestBuilder {
        private HttpRequest httpRequest;

        private HttpRequestBuilder(URL url) {
            httpRequest.url = url;
        }

        public HttpRequestBuilder method(HttpMethod httpMethod) {
            httpRequest.httpMethod = httpMethod;
            return this;
        }

        public HttpRequestBuilder contentType(HttpContentType contentType) {
            httpRequest.contentType = contentType;
            return this;
        }

        public HttpRequestBuilder headers(Map<String, String> headers) {
            httpRequest.headers = headers;
            return this;
        }

        public HttpRequestBuilder body(String body) {
            httpRequest.body = body;
            return this;
        }

        public HttpRequest build() {
            return this.httpRequest;
        }
    }
}
