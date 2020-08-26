package ru.cleverhause.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.security.oauth2.client.OAuth2ClientProperties;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.MediaType;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.util.Base64Utils;
import ru.cleverhause.auth.config.SuccessOAuth2LoginHandler;
import ru.cleverhause.auth.config.properties.OAuth2OuterClientProperties;

import java.util.UUID;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationResponseType.CODE;
import static org.springframework.security.web.authentication.www.BasicAuthenticationConverter.AUTHENTICATION_SCHEME_BASIC;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Slf4j
@ExtendWith({SpringExtension.class})
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@AutoConfigureMockMvc
class CleverhauseAuthApplicationTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private SuccessOAuth2LoginHandler successOAuth2LoginHandler;
    @Autowired
    private OAuth2ClientProperties authClientProperties;
    @Autowired
    private OAuth2OuterClientProperties authOuterClientProperties;

    @Value("${spring.security.oauth2.client.redirectUrl}")
    private String redirectUrl;

    @LocalServerPort
    int currentPort;

    @BeforeEach
    public void init() {
        String redirectUrlWithLocalPort = StringUtils.replaceOnce(redirectUrl, "{port}", Integer.toString(currentPort));
        successOAuth2LoginHandler.setRedirectUrl(redirectUrlWithLocalPort);
    }

    @Test
    void healthCheck() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/actuator/health")
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(TestUtils.fromFile("responses/actuator_health_ok.json")));
    }

    @Test
    void getTokenWithFormLogin() throws Exception {
        OAuth2OuterClientProperties.Client client = authOuterClientProperties.getClients().get("clever-ui");
        String getTokenUrl = "/oauth/token";
        String checkTokenUrl = "/oauth/check_token";
        byte[] creds = (client.getClientId() + ":secret").getBytes();
        String encodedCreds = new String(Base64Utils.encode(creds));
        String authHeader = AUTHENTICATION_SCHEME_BASIC + " " + encodedCreds;
        String tokenResponse = mockMvc.perform(MockMvcRequestBuilders.post(getTokenUrl)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .accept(MediaType.APPLICATION_JSON)
                .header(AUTHORIZATION, authHeader)
                .param("grant_type", AuthorizationGrantType.PASSWORD.getValue())
                .param("scope", "read")
                .param("username", "username")
                .param("password", "password"))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        OAuth2AccessToken token = new ObjectMapper().readValue(tokenResponse, DefaultOAuth2AccessToken.class);

        mockMvc.perform(MockMvcRequestBuilders.get(checkTokenUrl)
                .contentType(MediaType.TEXT_HTML)
                .header(AUTHORIZATION, authHeader)
                .accept(MediaType.APPLICATION_JSON)
                .param("token", token.getValue()))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void getTokenGitHubLogin() throws Exception {
        OAuth2ClientProperties.Registration reg = authClientProperties.getRegistration().get("github");
        String tokenUrl = "/oauth/authorize";
        String response = mockMvc.perform(MockMvcRequestBuilders.post(tokenUrl)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("response_type", CODE.getValue())
                .param("state", UUID.randomUUID().toString())
                .param("client_id", reg.getClientId())
//                .param("scope", reg.getScope().toArray()[0].toString())
                .param("redirect_uri", reg.getRedirectUri())
                .param("username", "username")
                .param("password", "password"))
                .andExpect(status().is3xxRedirection())
                .andReturn().getResponse().getRedirectedUrl();
        log.info("Redirect url:{}", response);
    }
}