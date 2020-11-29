package ru.cleverhause.auth.config;

import lombok.Data;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.Assert;

import java.util.HashMap;
import java.util.Map;

@Data
@Configuration
@ConfigurationProperties("security.authentication")
public class ExistentProvidersMap implements InitializingBean {

    private Map<String, AuthenticationProviderInfo> providers = new HashMap<>();

    public AuthenticationProviderInfo getByType(String type) {
        return providers.get(type);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        // todo check availability of all needed classes. If not then do not include it in providers map
        Assert.notEmpty(providers, "There are no one providers configurations found");
    }

    @Data
    public static class AuthenticationProviderInfo {
        private String providerClassName;
        private String responseType;
        private String providerUrl;
    }
}
