package ru.cleverhause.auth.provider;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import ru.cleverhause.auth.config.ExistentProvidersMap;
import ru.cleverhause.auth.service.FormLoginProviderFeignClient;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@Component
@RequiredArgsConstructor
public class FormLoginIdentityProvider implements IdentityProvider {

    public static final String USERNAME_KEY = "username";
    public static final String PASSWORD_KEY = "password";

    private final FormLoginProviderFeignClient providerFeignClient;

    @Override
    public Authentication authenticate(HttpServletRequest request, ExistentProvidersMap.AuthenticationProviderInfo providerInfo) throws AuthenticationException {
        String username = obtainUsername(request);
        String password = obtainPassword(request);
        Authentication authentication = new UsernamePasswordAuthenticationToken(username, password);
        log.info("Launching form login provider with authorization: {}", authentication);
        Authentication result = providerFeignClient.authenticateUserByUsername(authentication);
        if (result != null && result.isAuthenticated()) {
            updateSecurityContext(result);
            return result;
        } else {
            throw new AuthenticationServiceException("Problems!!");
        }
    }

    private void updateSecurityContext(Authentication authentication) {
        SecurityContextHolder.clearContext();
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(authentication);
        SecurityContextHolder.setContext(context);
    }

    private String obtainUsername(HttpServletRequest request) {
        return request.getParameter(USERNAME_KEY);
    }

    private String obtainPassword(HttpServletRequest request) {
        return request.getParameter(PASSWORD_KEY);
    }
}
