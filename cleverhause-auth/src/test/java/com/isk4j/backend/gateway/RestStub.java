package com.isk4j.backend.gateway;

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
    private static final List<RestStub> restStubs = new ArrayList<>(DEFAULT_WAS_AMOUNT);

    private final MappingBuilder initMappingBuilder;
    private final ResponseDefinitionBuilder initResponseBuilder;

    RestStub(ResponseDefinitionBuilder initResponseBuilder, MappingBuilder initMappingBuilder) {
        this.initResponseBuilder = initResponseBuilder;
        this.initMappingBuilder = initMappingBuilder.willReturn(initResponseBuilder);
        WireMock.stubFor(this.initMappingBuilder);
        restStubs.add(this);
    }

    public static void resetAll() {
        for (RestStub restStub : restStubs) {
            restStub.reset();
        }
    }

    public ResponseDefinitionBuilder response() {
        return ResponseDefinitionBuilder.like(initResponseBuilder.build());
    }

    public RestStub withRequestBody(String xpathToElement, String elementValue) {
        WireMock.editStub(this.initMappingBuilder.withRequestBody(matchingXPath(xpathToElement, containing(elementValue))));
        return this;
    }

    public RestStub withRequestBodyContainingJson(String jsonPath, String elementValue) {
        WireMock.editStub(this.initMappingBuilder.withRequestBody(matchingJsonPath(jsonPath, containing(elementValue))));
        return this;
    }

    public RestStub editResponse(ResponseDefinitionBuilder responseBuilder) {
        WireMock.editStub(this.initMappingBuilder.willReturn(responseBuilder));
        return this;
    }

    private void reset() {
        WireMock.editStub(this.initMappingBuilder.willReturn(initResponseBuilder));
    }
}
