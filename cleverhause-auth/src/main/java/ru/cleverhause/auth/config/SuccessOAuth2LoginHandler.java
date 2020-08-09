package ru.cleverhause.auth.config;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.security.web.header.Header;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.support.RequestContextUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.http.HttpHeaders;
import java.security.Principal;
import java.util.Arrays;
import java.util.Base64;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.security.oauth2.core.AuthorizationGrantType.CLIENT_CREDENTIALS;

@Component(value = "successOAuth2LoginHandler")
@RequiredArgsConstructor
public class SuccessOAuth2LoginHandler extends SimpleUrlAuthenticationSuccessHandler {

    @Value("${spring.security.oauth2.redirectUrl}")
    private String redirectUrl;

    private final ClientRegistrationRepository clientRegistrationRepository;

    @Override
    protected void handle(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
//        String registrationId = authentication.getName();
//        ClientRegistration clientRegistration = this.clientRegistrationRepository.findByRegistrationId(registrationId);
        String enrichedRedirectUri = redirectUrl + "?grant_type=" + CLIENT_CREDENTIALS.getValue();
        setDefaultTargetUrl(enrichedRedirectUri);
        super.handle(request, response, authentication);
    }
}
