package ru.cleverhause.auth.config;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.security.oauth2.client.endpoint.OAuth2AccessTokenResponseClient;
import org.springframework.security.oauth2.client.endpoint.OAuth2AuthorizationCodeGrantRequest;
import org.springframework.security.oauth2.client.endpoint.OAuth2AuthorizationCodeGrantRequestEntityConverter;
import org.springframework.security.oauth2.client.filter.OAuth2ClientContextFilter;
import org.springframework.security.oauth2.client.http.OAuth2ErrorResponseErrorHandler;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableOAuth2Client;
import org.springframework.security.oauth2.core.OAuth2AuthorizationException;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.core.endpoint.OAuth2AccessTokenResponse;
import org.springframework.security.oauth2.core.http.converter.OAuth2AccessTokenResponseHttpMessageConverter;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestOperations;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;

@Configuration
@RequiredArgsConstructor
public class OAuth2ClientConfig {
//
//    @Bean
//    public RestTemplate restTemplate() {
//        return new RestTemplate();
//    }

//    @Bean
//    public OAuth2AuthorizationCodeAccessTokenResponseClient getOAuth2AuthorizationCodeAccessTokenResponseClient() {
//        return new OAuth2AuthorizationCodeAccessTokenResponseClient();
//    }
//
//    public static class OAuth2AuthorizationCodeAccessTokenResponseClient
//            implements OAuth2AccessTokenResponseClient<OAuth2AuthorizationCodeGrantRequest> {
//        private static final String INVALID_TOKEN_RESPONSE_ERROR_CODE = "invalid_token_response";
//
//        private final Converter<OAuth2AuthorizationCodeGrantRequest, RequestEntity<?>> requestEntityConverter =
//                new OAuth2AuthorizationCodeGrantRequestEntityConverter();
//
//        private final RestOperations restOperations;
//
//        public OAuth2AuthorizationCodeAccessTokenResponseClient() {
//            RestTemplate restTemplate = new RestTemplate(Arrays.asList(
//                    new FormHttpMessageConverter(), new OAuth2AccessTokenResponseHttpMessageConverter()));
//            restTemplate.setErrorHandler(new OAuth2ErrorResponseErrorHandler());
//            this.restOperations = restTemplate;
//        }
//
//        @Override
//        public OAuth2AccessTokenResponse getTokenResponse(OAuth2AuthorizationCodeGrantRequest clientCredentialsGrantRequest) {
//            Assert.notNull(clientCredentialsGrantRequest, "clientCredentialsGrantRequest cannot be null");
//
//            RequestEntity<?> request = this.requestEntityConverter.convert(clientCredentialsGrantRequest);
//
//            ResponseEntity<OAuth2AccessTokenResponse> response;
//            try {
//                response = this.restOperations.exchange(request, OAuth2AccessTokenResponse.class);
//            } catch (RestClientException ex) {
//                OAuth2Error oauth2Error = new OAuth2Error(INVALID_TOKEN_RESPONSE_ERROR_CODE,
//                        "An error occurred while attempting to retrieve the OAuth 2.0 Access Token Response: " + ex.getMessage(), null);
//                throw new OAuth2AuthorizationException(oauth2Error, ex);
//            }
//
//            OAuth2AccessTokenResponse tokenResponse = response.getBody();
//
//            if (CollectionUtils.isEmpty(tokenResponse.getAccessToken().getScopes())) {
//                // As per spec, in Section 5.1 Successful Access Token Response
//                // https://tools.ietf.org/html/rfc6749#section-5.1
//                // If AccessTokenResponse.scope is empty, then default to the scope
//                // originally requested by the client in the Token Request
//                tokenResponse = OAuth2AccessTokenResponse.withResponse(tokenResponse)
//                        .scopes(clientCredentialsGrantRequest.getClientRegistration().getScopes())
//                        .build();
//            }
//
//            return tokenResponse;
//        }
//    }

//    @Bean
//    public AuthorizationCodeResourceDetails remote() {
//        AuthorizationCodeResourceDetails details = new AuthorizationCodeResourceDetails();
//        details.setClientAuthenticationScheme(AuthenticationScheme.header);
//        details.setUserAuthorizationUri("https://github.com/login/oauth/authorize");
//        details.setClientId("b4ccc96cfd49bff036a6");
//        details.setClientSecret("178119e291c3b07a4684d1b31c35bb94b058329d");
//        details.setAccessTokenUri("https://github.com/login/oauth/access_token");
//        details.setScope(Collections.singletonList("user"));
//        return details;
//    }
//    @Bean
//    public FilterRegistrationBean<OAuth2ClientContextFilter> oAuth2ClientFilterRegistration(OAuth2ClientContextFilter oAuth2ClientContextFilter)
//    {
//        FilterRegistrationBean<OAuth2ClientContextFilter> registration = new FilterRegistrationBean<>();
//        registration.setFilter(oAuth2ClientContextFilter);
//        registration.setOrder(-100);
//        return registration;
//    }
//
//    private Filter ssoFilter()
//    {
//        GOOGLE
//        OAuth2ClientAuthenticationProcessingFilter googleFilter = new OAuth2ClientAuthenticationProcessingFilter("/login/google");
//        OAuth2RestTemplate googleTemplate = new OAuth2RestTemplate(google(), oAuth2ClientContext);
//        googleFilter.setRestTemplate(googleTemplate);
//        RemoteTokenServices tokenServices = new CustomUserInfoTokenServices(googleResource().getUserInfoUri(), google().getClientId());
//        tokenServices.setRestTemplate(googleTemplate);
//        googleFilter.setTokenServices(tokenServices);
//        tokenServices.setUserRepo(userRepo);
//        tokenServices.setPasswordEncoder(passwordEncoder);
//        return googleFilter;
//    }
}
