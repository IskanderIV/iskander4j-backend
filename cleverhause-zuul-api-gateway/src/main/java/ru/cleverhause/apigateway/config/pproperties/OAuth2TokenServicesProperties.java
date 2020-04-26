package ru.cleverhause.apigateway.config.pproperties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Configuration
@ConfigurationProperties(value = "oauth2")
@Data
public class OAuth2TokenServicesProperties {
    private Map<String, Client> clients = new HashMap<>();

    @Data
    public static class Client {
        private String clientId;
        private String secret;
        private String checkTokenEndpointUrl;
        private Set<String> grandTypes;
        private Set<String> scopes;
        private int accessTokenLifeSecond;
        private int refreshTokenLifeSecond;

        public Set<String> getGrandTypes() {
            return grandTypes != null ? new HashSet<>(grandTypes) : null;
        }

        public void setGrandTypes(Set<String> grandTypes) {
            this.grandTypes = grandTypes != null ? new HashSet<>(grandTypes) : null;
        }

        public Set<String> getScopes() {
            return scopes != null ? new HashSet<>(scopes) : null;
        }

        public void setScopes(Set<String> scopes) {
            this.scopes = scopes != null ? new HashSet<>(scopes) : null;
        }
    }
}
