package ru.cleverhause.auth.provider;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import ru.cleverhause.auth.dto.AuthenticationBaseDto;

import javax.servlet.http.HttpServletRequest;

@Component
@RequiredArgsConstructor
public class FormLoginIdentityProvider implements IdentityProvider {

    private final RestTemplate restTemplate;

    @Override
    public AuthenticationBaseDto authenticate(HttpServletRequest request) throws AuthenticationException {
        return null;
    }
}
