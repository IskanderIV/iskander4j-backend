package ru.cleverhause.auth.config;

import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static org.springframework.security.oauth2.core.AuthorizationGrantType.CLIENT_CREDENTIALS;

@Setter
@Component(value = "successOAuth2LoginHandler")
@RequiredArgsConstructor
public class SuccessOAuth2LoginHandler extends SimpleUrlAuthenticationSuccessHandler {

    @Value("${spring.security.oauth2.client.redirectUrl}")
    private String redirectUrl;

    @Override
    protected void handle(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        String enrichedRedirectUri = redirectUrl + "?grant_type=" + CLIENT_CREDENTIALS.getValue();
        setDefaultTargetUrl(enrichedRedirectUri);
        super.handle(request, response, authentication);
    }
}
