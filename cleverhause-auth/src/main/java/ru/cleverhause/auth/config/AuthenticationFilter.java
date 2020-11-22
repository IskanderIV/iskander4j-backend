package ru.cleverhause.auth.config;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.ProviderNotFoundException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.util.ClassUtils;
import ru.cleverhause.auth.provider.IdentityProvider;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
public class AuthenticationFilter extends AbstractAuthenticationProcessingFilter implements ApplicationContextAware {

    private final AuthenticationProviderInfoResolver resolver;

    private ApplicationContext context;

    public AuthenticationFilter(String defaultFilterProcessesUrl, AuthenticationProviderInfoResolver resolver) {
        super(defaultFilterProcessesUrl);
        this.resolver = resolver;
    }

    @SneakyThrows
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        var providerInfo = resolver.resolveProvider(request);
        if (providerInfo != null
                && StringUtils.isNotBlank(providerInfo.getProviderClassName())
                && StringUtils.isNotBlank(providerInfo.getResponseType()) ) {
            String providerClassName = providerInfo.getProviderClassName();
            try {
                Class<?> providerType = ClassUtils.resolveClassName(providerClassName, this.getClass().getClassLoader());
                IdentityProvider resolvedProvider = (IdentityProvider) context.getBean(providerType);
                return resolvedProvider.authenticate(request, providerInfo);
            } catch (Exception e) {
                log.error(e.getLocalizedMessage(), e);
                throw new ProviderNotFoundException("There are no such kind of authorization for that type");
            }
        } else {
            throw new ProviderNotFoundException("There are no such kind of authorization for that type");
        }
    }

    @Override
    public void setApplicationContext(@NonNull ApplicationContext applicationContext) throws BeansException {
        this.context = applicationContext;
    }
}
