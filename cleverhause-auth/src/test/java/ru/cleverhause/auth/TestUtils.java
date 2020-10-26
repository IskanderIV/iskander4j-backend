package ru.cleverhause.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.security.oauth2.core.endpoint.OAuth2AccessTokenResponse;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class TestUtils {

    public static String fromFile(String path) throws Exception {
        Path path2File = Paths.get(TestUtils.class.getClassLoader().getResource(path).toURI());
        return Files.readString(path2File);
    }

    @Test
    public void responseMappingTest() throws Exception {
        new ObjectMapper().readValue(TestUtils.fromFile("__files/json/gitHub_get_token_OK.json"), OAuth2AccessTokenResponse.class);
    }
}
