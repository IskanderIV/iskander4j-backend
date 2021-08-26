package ru.cleverhause.users.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;

@Data
@ConfigurationProperties("spring.security.oauth2.client")
public class ClientRegistrationMainAttributeTags {
    private Map<String, MainAttributes> registration = new HashMap<>();

    @Nullable
    public Map<AuthorizedClientsAttributes, String> getByClientId(String clientRegistrationId) {
        MainAttributes clientMainAttributes = registration.get(clientRegistrationId);
        if (clientMainAttributes != null) {
            return clientMainAttributes.getAttributes();
        }
        return null;
    }

    @Data
    private static class MainAttributes {
        Map<AuthorizedClientsAttributes, String> attributes = new HashMap<>();
    }
}
