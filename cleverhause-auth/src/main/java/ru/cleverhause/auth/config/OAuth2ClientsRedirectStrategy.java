package ru.cleverhause.auth.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.web.RedirectStrategy;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
public class OAuth2ClientsRedirectStrategy implements RedirectStrategy {

//    @Value("${spring.security.oauth2.redirectUrl}")
    private String redirectUrl;

    private final ClientRegistrationRepository clientRegistrationRepository;

    @Override
    public void sendRedirect(HttpServletRequest request, HttpServletResponse response, String url) throws IOException {
//        String registrationId = authentication.getName();
//        ClientRegistration clientRegistration = this.clientRegistrationRepository.findByRegistrationId(registrationId)
//        String redirectUrl = enrichRedirectUrl(request.getContextPath(), url);
//        redirectUrl = response.encodeRedirectURL(redirectUrl);
//        log.debug("Redirecting to '" + redirectUrl + "'");
//        response.sendRedirect(redirectUrl);
    }
}
