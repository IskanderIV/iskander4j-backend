package ru.cleverhause.it.request;

import com.fasterxml.jackson.databind.util.JSONPObject;

import java.util.HashMap;
import java.util.Map;

public class ArduinoRequest {
    private Map<String, Object> headers;
    private JSONPObject body;

    public ArduinoRequest() {
        this.headers = new HashMap<>();
        this.body = new JSONPObject("devicesState", null);
    }

    public Map<String, Object> getHeaders() {
        return headers;
    }

    public void setHeaders(Map<String, Object> headers) {
        this.headers = headers;
    }

    public JSONPObject getBody() {
        return body;
    }

    public void setBody(JSONPObject body) {
        this.body = body;
    }

    @Override
    public String toString() {
        return "ArduinoRequest{" +
                "headers=" + headers +
                ", body=" + body +
                '}';
    }
}
