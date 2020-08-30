package ru.cleverhause.auth;

import com.github.tomakehurst.wiremock.client.MappingBuilder;
import com.github.tomakehurst.wiremock.client.ResponseDefinitionBuilder;
import com.github.tomakehurst.wiremock.client.WireMock;

import java.util.ArrayList;
import java.util.List;

import static com.github.tomakehurst.wiremock.client.WireMock.containing;
import static com.github.tomakehurst.wiremock.client.WireMock.matchingJsonPath;
import static com.github.tomakehurst.wiremock.client.WireMock.matchingXPath;

public class RestStub {
    private static final int DEFAULT_WAS_AMOUNT = 2;
    private static List<RestStub> restStubs = new ArrayList<>(DEFAULT_WAS_AMOUNT);

    private final MappingBuilder mappingBuilder;
    private final ResponseDefinitionBuilder initResponseBuilder;

    RestStub(ResponseDefinitionBuilder initResponseBuilder, MappingBuilder mappingBuilder) {
        this.initResponseBuilder = initResponseBuilder;
        this.mappingBuilder = mappingBuilder.willReturn(initResponseBuilder);
        WireMock.stubFor(this.mappingBuilder);
        restStubs.add(this);
    }

    public static void resetAll() {
        for (RestStub restStub : restStubs) {
            restStub.init();
        }
    }

    public ResponseDefinitionBuilder response() {
        return ResponseDefinitionBuilder.like(initResponseBuilder.build());
    }

    public RestStub withRequestBody(String xpathToElement, String elementValue) {
        WireMock.editStub(this.mappingBuilder.withRequestBody(matchingXPath(xpathToElement, containing(elementValue))));
        return this;
    }
    public RestStub withRequestBodyContainingJson(String jsonPath, String elementValue) {
        WireMock.editStub(this.mappingBuilder.withRequestBody(matchingJsonPath(jsonPath, containing(elementValue))));
        return this;
    }

    public MappingBuilder editRequest() {
        return this.mappingBuilder;
    }

    public RestStub editResponse(ResponseDefinitionBuilder responseBuilder) {
        WireMock.editStub(this.mappingBuilder.willReturn(responseBuilder));
        return this;
    }

    private void init() {
        WireMock.editStub(this.mappingBuilder.willReturn(initResponseBuilder));
    }
}
