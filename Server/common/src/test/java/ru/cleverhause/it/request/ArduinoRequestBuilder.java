package ru.cleverhause.it.request;

import com.fasterxml.jackson.databind.util.JSONPObject;
import java.util.Map;

public class ArduinoRequestBuilder {
    private ArduinoRequest request = new ArduinoRequest();

    private ArduinoRequestBuilder() {}

    public static ArduinoRequestBuilder create() {
        return new ArduinoRequestBuilder();
    }

    public ArduinoRequestBuilder setHeaders(Map<String, Object> headers) {
        this.request.setHeaders(headers);
        return this;
    }

    public ArduinoRequestBuilder setBody(JSONPObject body) {
        this.request.setBody(body);
        return this;
    }

    public ArduinoRequest build() {
        return request;
    }
}
