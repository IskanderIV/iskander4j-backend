package ru.cleverhause.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.security.oauth2.client.OAuth2ClientProperties;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.util.Base64Utils;
import org.springframework.util.MultiValueMap;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;
import org.springframework.web.util.UriUtils;
import ru.cleverhause.auth.config.SuccessOAuth2LoginHandler;
import ru.cleverhause.auth.config.properties.OAuth2OuterClientProperties;

import java.net.URI;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationResponseType.CODE;
import static org.springframework.security.web.authentication.www.BasicAuthenticationConverter.AUTHENTICATION_SCHEME_BASIC;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.cleverhause.auth.RestStubFactory.getUserInfoOkStub;
import static ru.cleverhause.auth.RestStubFactory.postGitHubTokenOkStub;

@Slf4j
@ExtendWith({SpringExtension.class})
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWireMock(port = 0)
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
    @Autowired
    private TestRestTemplate restTemplate;

    @Value("${spring.security.oauth2.client.redirectUrl}")
    private String redirectUrl;

    private final RestStub gitHubGetToken = postGitHubTokenOkStub();
    private final RestStub gitHubGetUserInfo = getUserInfoOkStub();

    @LocalServerPort
    int currentPort;

    @BeforeEach
    public void init() {
        RestStub.resetAll();
        String redirectUrlWithLocalPort = StringUtils.replaceOnce(redirectUrl, "{port}", Integer.toString(currentPort));
        successOAuth2LoginHandler.setRedirectUrl(redirectUrlWithLocalPort);
    }

    @Test
    void healthCheck() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/actuator/health")
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(TestUtils.fromFile("responses/json/actuator_health_ok.json")));
    }

    @Test
    void getTokenWithFormLoginWithOauth2PasswordFlow() throws Exception {
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
                .param(OAuth2ParameterNames.GRANT_TYPE, AuthorizationGrantType.PASSWORD.getValue())
                .param(OAuth2ParameterNames.SCOPE, "read")
                .param(OAuth2ParameterNames.USERNAME, "username")
                .param(OAuth2ParameterNames.PASSWORD, "password"))
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

    @Disabled("Disabled because it is a wrong way. Butt I still want to fix th whole path of getting token!")
    @Test
    void getTokenThroughGitHubLoginWithRealGHServer() throws Exception {
        String provider = "github";
        OAuth2ClientProperties.Registration reg = authClientProperties.getRegistration().get(provider);
        // 1. init procedure. get github login redirection from auth server
        String tokenUrl = "/oauth2/authorization/" + provider;
        String gitHubOauthLoginRedirectUrl = mockMvc.perform(MockMvcRequestBuilders.get(tokenUrl))
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andReturn().getResponse().getRedirectedUrl();

        assertNotNull(gitHubOauthLoginRedirectUrl);

        // 2. redirect to gitHub /https://github.com/login/oauth/authorize
        RequestEntity<Void> request = RequestEntity
                .get(URI.create(gitHubOauthLoginRedirectUrl))
                .accept(MediaType.APPLICATION_JSON)
                .acceptCharset(Charset.defaultCharset())
                .build();
//        restTemplate = new TestRestTemplate(ENABLE_REDIRECTS);
        ResponseEntity<Void> ghAuthorizeResponse = restTemplate.exchange(request, Void.class);

        // 3. redirect to GH GET login page. create GH session
        URI ghLoginRedirectUri = ghAuthorizeResponse.getHeaders().getLocation();

        assertNotNull(ghLoginRedirectUri);

        RequestEntity<Void> ghGetLoginRequest = RequestEntity
                .get(ghLoginRedirectUri)
                .accept(MediaType.TEXT_HTML)
                .acceptCharset(Charset.defaultCharset())
                .build();
        ResponseEntity<Void> ghGetLoginResponse = restTemplate.exchange(ghGetLoginRequest, Void.class);

        log.info("Result url:{}", ghAuthorizeResponse);
    }

    @Test
    void getTokenThroughGitHubLoginWithMockedGitHubServer() throws Exception {
        String provider = "github";
        String code = RandomStringUtils.randomAlphanumeric(10);
        OAuth2ClientProperties.Registration reg = authClientProperties.getRegistration().get(provider);
        // 1. init procedure. get github login redirection from auth server
        String getTokenInitUrl = "/oauth2/authorization/" + provider;
        String getTokenByCodeUrl = "/login/oauth2/code/" + provider;
        MockHttpSession session = new MockHttpSession();
        MockHttpServletResponse gitHubOauthLoginRedirectResponse =
                mockMvc.perform(MockMvcRequestBuilders.get(getTokenInitUrl)
                        .session(session))
                        .andDo(print())
                        .andExpect(status().is3xxRedirection())
                        .andReturn().getResponse();

        String gitHubOauthLoginRedirectUrl = gitHubOauthLoginRedirectResponse.getRedirectedUrl();
        assertNotNull(gitHubOauthLoginRedirectUrl);

        URI gitHubOauthLoginRedirectUri = URI.create(gitHubOauthLoginRedirectUrl);
        UriComponents uriComponents = UriComponentsBuilder.fromUri(gitHubOauthLoginRedirectUri).build();
        MultiValueMap<String, String> gitHubOauthLoginRedirectUriQueryParams =
                uriComponents.getQueryParams();
        String state = gitHubOauthLoginRedirectUriQueryParams.get(OAuth2ParameterNames.STATE).get(0);
        state = UriUtils.decode(state, StandardCharsets.UTF_8);

        // 2. form request to app to get GH token and send redirect for getting auth token
        MockHttpServletResponse redirectGetAppTokenResponse = mockMvc.perform(MockMvcRequestBuilders.get(getTokenByCodeUrl)
                .param(OAuth2ParameterNames.CODE, code)
                .param(OAuth2ParameterNames.STATE, state)
                .session(session)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andReturn().getResponse();
        String redirectGetAppTokenUrl = redirectGetAppTokenResponse.getRedirectedUrl();
        assertNotNull(redirectGetAppTokenUrl);

        log.info("Session attributes: {}, redirect to get app token cookies: {}, headers: {}",
                session.getAttributeNames(), redirectGetAppTokenResponse.getCookies(), redirectGetAppTokenResponse.getHeaderNames());
        // 3. get App token
        String tokenResponse = mockMvc.perform(MockMvcRequestBuilders.get(redirectGetAppTokenUrl)
                .session(session)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
//        OAuth2AccessToken token = new ObjectMapper().readValue(tokenResponse, DefaultOAuth2AccessToken.class);
        log.info("Result url:{}", tokenResponse);
    }

    @Disabled("Redundant")
    @Test
    void aaa() throws Exception {
        String provider = "github";
        OAuth2ClientProperties.Registration reg = authClientProperties.getRegistration().get(provider);
        // init procedure. get github login redirection
        String tokenUrl = "/oauth2/authorization/github";
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