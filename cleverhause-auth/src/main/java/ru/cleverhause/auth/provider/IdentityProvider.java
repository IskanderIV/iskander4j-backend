package ru.cleverhause.auth.provider;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import ru.cleverhause.auth.config.ExistentProvidersMap;

import javax.servlet.http.HttpServletRequest;

public interface IdentityProvider {

    Authentication authenticate(HttpServletRequest request,
                                ExistentProvidersMap.AuthenticationProviderInfo providerInfo) throws AuthenticationException;
}
