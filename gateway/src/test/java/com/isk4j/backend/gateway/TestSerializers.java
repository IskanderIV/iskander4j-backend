package com.isk4j.backend.gateway;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.DefaultSerializerProvider;
import org.assertj.core.util.Files;
import org.junit.jupiter.api.Test;
import org.springframework.security.jackson2.CoreJackson2Module;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessTokenJackson2Serializer;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;

import static java.nio.charset.StandardCharsets.UTF_8;

public class TestSerializers {

    @Test
    public void testJsonMapperSerializers() throws JsonProcessingException, IOException {
        ObjectMapper mapper = new ObjectMapper();
        SimpleModule module1 = new SimpleModule();
        SimpleModule module2 = new CoreJackson2Module();
        module1.addSerializer(OAuth2AccessToken.class, (JsonSerializer<OAuth2AccessToken>)new OAuth2AccessTokenJackson2Serializer());
        module1.addAbstractTypeMapping(OAuth2AccessToken.class, DefaultOAuth2AccessToken.class);
//        module2.addSerializer(OAuth2AccessToken.class, (JsonSerializer<OAuth2AccessToken>)new OAuth2AccessTokenJackson2Serializer());
//        module2.addKeySerializer(OAuth2AccessToken.class, (JsonSerializer<OAuth2AccessToken>)new OAuth2AccessTokenJackson2Serializer());
//        module.setMixInAnnotation(OAuth2AccessToken.class, OAuth2AccessTokenMixin.class);
//        module2.setMixInAnnotation(OAuth2AccessToken.class, OAuth2AccessTokenMixin.class);
        mapper.registerModules(module2);
        mapper.addMixIn(OAuth2AccessToken.class, OAuth2AccessTokenMixin.class);

//        SimpleSerializers serializers = new SimpleSerializers();
//        serializers.addSerializer(new OAuth2AccessTokenJackson2Serializer());
//        mapper.getSerializerFactory().withAdditionalSerializers(serializers);
        Class<?> mixinClassForOAuth2AccessToken = mapper.getSerializationConfig().findMixInClassFor(OAuth2AccessToken.class);
        OAuth2AccessToken oAuth2AccessToken = new DefaultOAuth2AccessToken("eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJleHAiOjE2MDY2MTk5NjQsInVzZXJfbmFtZSI6InVzZXJuYW1lIiwiYXV0aG9yaXRpZXMiOlsiVVNFUiJdLCJqdGkiOiJhN2ZhMTA3Ny0wMDE3LTRiOWMtYTVjNC0zNzRiNTQ0Y2EyMzYiLCJjbGllbnRfaWQiOiJ0ZXN0SWQiLCJzY29wZSI6WyJyZWFkIl19.ZCO_FWDZhDMPBPTaygmnyb-GC_1yLNsZ5qZQRf42Cp8");

        try (Writer writer = new FileWriter(Files.newTemporaryFile(), UTF_8)) {
            new OAuth2AccessTokenJackson2Serializer().serialize(oAuth2AccessToken, mapper.getFactory().createGenerator(writer), new DefaultSerializerProvider.Impl());
        } catch (Exception e) {
            e.printStackTrace();
        }
        String token = mapper.writeValueAsString(oAuth2AccessToken);
    }

    @JsonTypeInfo(use = JsonTypeInfo.Id.NONE)
    @JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY, getterVisibility = JsonAutoDetect.Visibility.NONE,
            isGetterVisibility = JsonAutoDetect.Visibility.NONE)
    @JsonIgnoreProperties(ignoreUnknown = true)
    abstract static class OAuth2AccessTokenMixin {

        @JsonCreator
        OAuth2AccessTokenMixin(
                @JsonProperty("tokenType") String tokenType,
                @JsonProperty("tokenValue") String tokenValue
//                @JsonProperty("issuedAt") Instant issuedAt,
//                @JsonProperty("expiresAt") Date expiration,
//                @JsonProperty("scopes") Set<String> scopes
        ) {
        }
    }
}
