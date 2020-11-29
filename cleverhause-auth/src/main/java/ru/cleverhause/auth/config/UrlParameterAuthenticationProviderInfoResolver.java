package ru.cleverhause.auth.config;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

@Component
@RequiredArgsConstructor
public class UrlParameterAuthenticationProviderInfoResolver implements AuthenticationProviderInfoResolver {
    public static final String PROVIDER_TYPE_KEY = "type";

    private final ExistentProvidersMap providersMap;

    public ExistentProvidersMap.AuthenticationProviderInfo resolveProvider(HttpServletRequest request) {
        String type = request.getParameter(PROVIDER_TYPE_KEY);
        return providersMap.getByType(type);
    }
}
