package ru.cleverhause.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.util.Base64Utils;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;
import org.springframework.web.util.UriUtils;
import ru.cleverhause.auth.config.properties.OAuth2OuterClientProperties;

import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.security.oauth2.core.AuthorizationGrantType.AUTHORIZATION_CODE;
import static org.springframework.security.web.authentication.www.BasicAuthenticationConverter.AUTHENTICATION_SCHEME_BASIC;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.cleverhause.auth.RestStubFactory.getFormLoginProviderStub;
import static ru.cleverhause.auth.config.UrlParameterAuthenticationProviderInfoResolver.PROVIDER_TYPE_KEY;

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
    private OAuth2OuterClientProperties authOuterClientProperties;
    @Autowired
    private ClientDetailsService clientDetailsService;
    @Autowired
    private ObjectMapper objectMapper;

    private final RestStub getFormLoginProvider = getFormLoginProviderStub();

    @LocalServerPort
    int currentPort;

    @BeforeEach
    public void init() {
        RestStub.resetAll();
        Set<String> clients = clientDetailsService.loadClientByClientId("testId").getRegisteredRedirectUri();
        for (int i = 0; i < clients.size(); i++) {
            String url = clients.iterator().next();
            String newUrl = StringUtils.replaceOnce(url, "{port}", Integer.toString(currentPort));
            clients.remove(url);
            clients.add(newUrl);
        }
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
    void tryFormLoginTest() throws Exception {
        // prepare form login request
        String loginUrl = "/oauth/authorize";
        MultiValueMap<String, String> queryCodeParams = new LinkedMultiValueMap<>(Map.of(
                OAuth2ParameterNames.RESPONSE_TYPE, List.of(OAuth2ParameterNames.CODE),
                OAuth2ParameterNames.STATE, List.of(RandomStringUtils.randomAlphanumeric(10)),
                OAuth2ParameterNames.CLIENT_ID, List.of("testId"),
                OAuth2ParameterNames.SCOPE, List.of("read"),
                OAuth2ParameterNames.REDIRECT_URI, List.of("http://localhost:" + currentPort),
                PROVIDER_TYPE_KEY, List.of("form")
        ));
        MockHttpSession session = new MockHttpSession();
        MockHttpServletResponse codeResponse = mockMvc.perform(MockMvcRequestBuilders.post(loginUrl)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .accept(MediaType.APPLICATION_JSON)
                .session(session)
                .queryParams(queryCodeParams)
                .param(OAuth2ParameterNames.USERNAME, "username")
                .param(OAuth2ParameterNames.PASSWORD, "password"))
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andReturn().getResponse();

        String redirectUrl = codeResponse.getRedirectedUrl();
        assertTrue(StringUtils.isNotBlank(redirectUrl));

        URI redirectUri = URI.create(redirectUrl);
        UriComponents uriComponents = UriComponentsBuilder.fromUri(redirectUri).build();
        MultiValueMap<String, String> gitHubOauthLoginRedirectUriQueryParams =
                uriComponents.getQueryParams();
        String state = gitHubOauthLoginRedirectUriQueryParams.get(OAuth2ParameterNames.STATE).get(0);
        state = UriUtils.decode(state, StandardCharsets.UTF_8);
        String code = gitHubOauthLoginRedirectUriQueryParams.get(OAuth2ParameterNames.CODE).get(0);
        code = UriUtils.decode(code, StandardCharsets.UTF_8);


        byte[] creds = ("testId" + ":secret").getBytes();
        String encodedCreds = new String(Base64Utils.encode(creds));
        String authHeader = AUTHENTICATION_SCHEME_BASIC + " " + encodedCreds;

        MultiValueMap<String, String> queryTokenParams = new LinkedMultiValueMap<>(Map.of(
                OAuth2ParameterNames.GRANT_TYPE, List.of(AUTHORIZATION_CODE.getValue()),
                OAuth2ParameterNames.STATE, List.of(state),
                OAuth2ParameterNames.CODE, List.of(code),
                OAuth2ParameterNames.REDIRECT_URI, List.of("http://localhost:" + currentPort)
        ));

        MockHttpServletResponse tokenResponse = mockMvc.perform(MockMvcRequestBuilders.post("http://localhost:" + currentPort + "/oauth/token")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .header(AUTHORIZATION, authHeader)
                .queryParams(queryTokenParams)
                .session(session))
                .andDo(print())
                .andExpect(status().isOk())
//                .andExpect(content().json(TestUtils.fromFile("__files/json/getFormLoginAuthenticationOk.json")))
                .andReturn().getResponse();
//
        OAuth2AccessToken token = objectMapper.readValue(tokenResponse.getContentAsByteArray(), DefaultOAuth2AccessToken.class);
        log.info("Token: {}", token);
    }
// TODO transfer to users ms
//    @Disabled("Disabled because it is a wrong way. Butt I still want to fix th whole path of getting token!")
//    @Test
//    void getTokenThroughGitHubLoginWithRealGHServer() throws Exception {
//        String provider = "github";
//        OAuth2ClientProperties.Registration reg = authClientProperties.getRegistration().get(provider);
//        // 1. init procedure. get github login redirection from auth server
//        String tokenUrl = "/oauth2/authorization/" + provider;
//        String gitHubOauthLoginRedirectUrl = mockMvc.perform(MockMvcRequestBuilders.get(tokenUrl))
//                .andDo(print())
//                .andExpect(status().is3xxRedirection())
//                .andReturn().getResponse().getRedirectedUrl();
//
//        assertNotNull(gitHubOauthLoginRedirectUrl);
//
//        // 2. redirect to gitHub /https://github.com/login/oauth/authorize
//        RequestEntity<Void> request = RequestEntity
//                .get(URI.create(gitHubOauthLoginRedirectUrl))
//                .accept(MediaType.APPLICATION_JSON)
//                .acceptCharset(Charset.defaultCharset())
//                .build();
////        restTemplate = new TestRestTemplate(ENABLE_REDIRECTS);
//        ResponseEntity<Void> ghAuthorizeResponse = restTemplate.exchange(request, Void.class);
//
//        // 3. redirect to GH GET login page. create GH session
//        URI ghLoginRedirectUri = ghAuthorizeResponse.getHeaders().getLocation();
//
//        assertNotNull(ghLoginRedirectUri);
//
//        RequestEntity<Void> ghGetLoginRequest = RequestEntity
//                .get(ghLoginRedirectUri)
//                .accept(MediaType.TEXT_HTML)
//                .acceptCharset(Charset.defaultCharset())
//                .build();
//        ResponseEntity<Void> ghGetLoginResponse = restTemplate.exchange(ghGetLoginRequest, Void.class);
//
//        log.info("Result url:{}", ghAuthorizeResponse);
//    }
//
//    @Disabled
//    @Test
//    void getTokenThroughGitHubLoginWithMockedGitHubServer() throws Exception {
//        String provider = "github";
//        String code = RandomStringUtils.randomAlphanumeric(10);
//        OAuth2ClientProperties.Registration reg = authClientProperties.getRegistration().get(provider);
//        // 1. init procedure. get github login redirection from auth server
//        String getTokenInitUrl = "/oauth2/authorization/" + provider;
//        String getTokenByCodeUrl = "/login/oauth2/code/" + provider;
//        MockHttpSession session = new MockHttpSession();
//        MockHttpServletResponse gitHubOauthLoginRedirectResponse =
//                mockMvc.perform(MockMvcRequestBuilders.get(getTokenInitUrl)
//                        .session(session))
//                        .andDo(print())
//                        .andExpect(status().is3xxRedirection())
//                        .andReturn().getResponse();
//
//        String gitHubOauthLoginRedirectUrl = gitHubOauthLoginRedirectResponse.getRedirectedUrl();
//        assertNotNull(gitHubOauthLoginRedirectUrl);
//
//        URI gitHubOauthLoginRedirectUri = URI.create(gitHubOauthLoginRedirectUrl);
//        UriComponents uriComponents = UriComponentsBuilder.fromUri(gitHubOauthLoginRedirectUri).build();
//        MultiValueMap<String, String> gitHubOauthLoginRedirectUriQueryParams =
//                uriComponents.getQueryParams();
//        String state = gitHubOauthLoginRedirectUriQueryParams.get(OAuth2ParameterNames.STATE).get(0);
//        state = UriUtils.decode(state, StandardCharsets.UTF_8);
//
//        // 2. form request to app to get GH token and send redirect for getting auth token
//        MockHttpServletResponse redirectGetAppTokenResponse = mockMvc.perform(MockMvcRequestBuilders.get(getTokenByCodeUrl)
//                .param(OAuth2ParameterNames.CODE, code)
//                .param(OAuth2ParameterNames.STATE, state)
//                .session(session)
//                .accept(MediaType.APPLICATION_JSON))
//                .andDo(print())
//                .andExpect(status().is3xxRedirection())
//                .andReturn().getResponse();
//        String redirectGetAppTokenUrl = redirectGetAppTokenResponse.getRedirectedUrl();
//        assertNotNull(redirectGetAppTokenUrl);
//
//        log.info("Session attributes: {}, redirect to get app token cookies: {}, headers: {}",
//                session.getAttributeNames(), redirectGetAppTokenResponse.getCookies(), redirectGetAppTokenResponse.getHeaderNames());
//        // 3. get App token
//        String tokenResponse = mockMvc.perform(MockMvcRequestBuilders.get(redirectGetAppTokenUrl)
//                .session(session)
//                .accept(MediaType.APPLICATION_JSON))
//                .andDo(print())
//                .andExpect(status().isOk())
//                .andReturn().getResponse().getContentAsString();
////        OAuth2AccessToken token = new ObjectMapper().readValue(tokenResponse, DefaultOAuth2AccessToken.class);
//        log.info("Result url:{}", tokenResponse);
//    }
//
//    @Disabled("Redundant")
//    @Test
//    void aaa() throws Exception {
//        String provider = "github";
//        OAuth2ClientProperties.Registration reg = authClientProperties.getRegistration().get(provider);
//        // init procedure. get github login redirection
//        String tokenUrl = "/oauth2/authorization/github";
//        String response = mockMvc.perform(MockMvcRequestBuilders.post(tokenUrl)
//                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
//                .param("response_type", CODE.getValue())
//                .param("state", UUID.randomUUID().toString())
//                .param("client_id", reg.getClientId())
////                .param("scope", reg.getScope().toArray()[0].toString())
//                .param("redirect_uri", reg.getRedirectUri())
//                .param("username", "username")
//                .param("password", "password"))
//                .andExpect(status().is3xxRedirection())
//                .andReturn().getResponse().getRedirectedUrl();
//        log.info("Redirect url:{}", response);
//    }
}