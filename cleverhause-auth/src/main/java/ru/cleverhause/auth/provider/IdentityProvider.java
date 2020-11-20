package ru.cleverhause.auth.provider;

import org.springframework.security.core.AuthenticationException;
import ru.cleverhause.auth.dto.AuthenticationBaseDto;

import javax.servlet.http.HttpServletRequest;

public interface IdentityProvider {

    AuthenticationBaseDto authenticate(HttpServletRequest request) throws AuthenticationException;
}
