package ru.cleverhause.auth.provider;

import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import ru.cleverhause.auth.config.ExistentProvidersMap;
import ru.cleverhause.auth.dto.request.AuthenticationRequest;
import ru.cleverhause.auth.dto.request.FormLoginAuthenticationRequest;
import ru.cleverhause.auth.dto.response.FormLoginAuthenticationResponse;

import javax.servlet.http.HttpServletRequest;
import java.net.URI;
import java.nio.file.attribute.UserPrincipalNotFoundException;

@Component
@RequiredArgsConstructor
public class FormLoginIdentityProvider implements IdentityProvider {

    public static final String USERNAME_KEY = "username";
    public static final String PASSWORD_KEY = "password";

    private final RestTemplate restTemplate;

    @Override
    public Authentication authenticate(HttpServletRequest request, ExistentProvidersMap.AuthenticationProviderInfo providerInfo) throws AuthenticationException {
        String url = providerInfo.getProviderUrl();
        String username = obtainUsername(request);
        String password = obtainPassword(request);
        FormLoginAuthenticationRequest body = new FormLoginAuthenticationRequest();
        body.setUsername(username);
        body.setPassword(password);
        RequestEntity<AuthenticationRequest> authRequest = RequestEntity
                .post(URI.create(url))
                .contentType(MediaType.APPLICATION_JSON)
                .body(body, FormLoginAuthenticationRequest.class);
        ResponseEntity<FormLoginAuthenticationResponse> response =
                restTemplate.exchange(authRequest, FormLoginAuthenticationResponse.class);

        if (response.getStatusCode().is2xxSuccessful()) {
            return response.getBody().getAuthentication();
        }
        throw new AuthenticationServiceException("Problems!!");
    }

    private String obtainUsername(HttpServletRequest request) {
        return request.getParameter(USERNAME_KEY);
    }

    private String obtainPassword(HttpServletRequest request) {
        return request.getParameter(PASSWORD_KEY);
    }
}
