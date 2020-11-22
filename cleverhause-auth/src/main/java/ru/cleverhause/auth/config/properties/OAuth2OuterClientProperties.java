package ru.cleverhause.auth.config.properties;

import lombok.Data;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Configuration
@ConfigurationProperties(value = "security.oauth2")
@Data
public class OAuth2OuterClientProperties implements InitializingBean {
    private Map<String, Client> clients = new HashMap<>();

    @Override
    public void afterPropertiesSet() throws Exception {
        Assert.notEmpty(clients, "There are no one clients configuration");
    }

    @Data
    public static class Client {
        private Set<String> resourceIds;
        private String clientId;
        private String secret;
        private String redirectUri;
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

        public Set<String> getResourceIds() {
            return resourceIds != null ? new HashSet<>(resourceIds) : null;
        }

        public void setResourceIds(Set<String> resourceIds) {
            this.resourceIds = resourceIds != null ? new HashSet<>(resourceIds) : null;
        }
    }
}
