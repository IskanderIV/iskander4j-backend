package ru.cleverhause.auth.config;

import javax.servlet.http.HttpServletRequest;

public interface AuthenticationProviderInfoResolver {

    ExistentProvidersMap.AuthenticationProviderInfo resolveProvider(HttpServletRequest request);
}
