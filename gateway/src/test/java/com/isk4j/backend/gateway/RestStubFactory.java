package com.isk4j.backend.gateway;

import com.github.tomakehurst.wiremock.client.WireMock;
import org.springframework.http.MediaType;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.containing;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static org.springframework.http.HttpHeaders.CONTENT_TYPE;

public class RestStubFactory {

    public static RestStub postGitHubTokenOkStub() {
        return postStubWithContentType("json/gitHub_get_token_OK.json",
                "/login/oauth/access_token", MediaType.APPLICATION_FORM_URLENCODED_VALUE);
    }

    public static RestStub postStubWithContentType(String initResponseFilePath, String endpoint, String contentType) {
        return new RestStub(aResponse()
                .withBodyFile(initResponseFilePath)
                .withHeader(CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE),
                WireMock.post(urlEqualTo(endpoint)).withHeader(CONTENT_TYPE, containing(contentType)));
    }
}
