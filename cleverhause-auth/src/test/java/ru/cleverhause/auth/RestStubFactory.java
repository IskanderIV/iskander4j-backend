package ru.cleverhause.auth;

import com.github.tomakehurst.wiremock.client.WireMock;
import org.springframework.http.MediaType;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.containing;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static org.springframework.http.HttpHeaders.CONTENT_TYPE;

public class RestStubFactory {

    public static RestStub postStub(String initResponseFilePath, String endpoint) {
        return new RestStub(aResponse()
                .withBodyFile(initResponseFilePath)
                .withHeader(CONTENT_TYPE, MediaType.APPLICATION_JSON_UTF8_VALUE),
                WireMock.post(urlEqualTo(endpoint)).withHeader(CONTENT_TYPE, containing(MediaType.APPLICATION_JSON_UTF8_VALUE)));
    }

    public static RestStub getStub(String initResponseFilePath, String endpoint) {
        return new RestStub(aResponse()
                .withBodyFile(initResponseFilePath)
                .withHeader(CONTENT_TYPE, MediaType.APPLICATION_JSON_UTF8_VALUE),
                WireMock.get(urlEqualTo(endpoint)).withHeader(CONTENT_TYPE, containing(MediaType.APPLICATION_JSON_UTF8_VALUE)));
    }
}
