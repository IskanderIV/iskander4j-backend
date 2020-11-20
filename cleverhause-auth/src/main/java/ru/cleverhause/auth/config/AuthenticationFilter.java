package ru.cleverhause.auth.config;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.ProviderNotFoundException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import ru.cleverhause.auth.dto.AuthenticationBaseDto;
import ru.cleverhause.auth.provider.IdentityProvider;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


public class AuthenticationFilter extends AbstractAuthenticationProcessingFilter implements ApplicationContextAware {

    private final AuthenticationProviderInfoResolver resolver;

    private ApplicationContext context;

    public AuthenticationFilter(String defaultFilterProcessesUrl, SimpleAuthenticationProviderInfoResolver resolver) {
        super(defaultFilterProcessesUrl);
        this.resolver = resolver;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException, IOException, ServletException {
        var providerInfo = resolver.resolveProvider(request);
        if (providerInfo != null
                && StringUtils.isNotBlank(providerInfo.getProviderClassName())
                && StringUtils.isNotBlank(providerInfo.getResponseConverterClassName()) ) {
            String providerClassName = providerInfo.getProviderClassName();
            String responseConverterClassName = providerInfo.getResponseConverterClassName();
            if (context.containsBean(providerClassName) && context.containsBean(responseConverterClassName)) {
                IdentityProvider resolvedProvider = (IdentityProvider) context.getBean(providerClassName);
                ProviderResponseConverter responseConverter = (ProviderResponseConverter) context.getBean(responseConverterClassName);
                AuthenticationBaseDto authenticationBaseDto = resolvedProvider.authenticate(request);
                return responseConverter.convert(authenticationBaseDto);
            } else {
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
