package ru.cleverhause.auth.config;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.HashMap;
import java.util.Map;

@Data
@ConfigurationProperties("security.authentication")
public class ExistentProvidersMap {

    private Map<String, AuthenticationProviderInfo> providers = new HashMap<>();

    public AuthenticationProviderInfo getByType(String type) {
        return providers.get(type);
    }

    @Data
    @AllArgsConstructor
    public static class AuthenticationProviderInfo {
        private String providerClassName;
        private String responseConverterClassName;
    }
}
