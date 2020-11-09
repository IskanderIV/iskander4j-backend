package ru.cleverhause.users.config.properties;

import lombok.Data;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.security.oauth2.client.token.grant.client.ClientCredentialsResourceDetails;
import org.springframework.util.Assert;

import static java.util.concurrent.TimeUnit.SECONDS;

@Data
@ConfigurationProperties(prefix = "feign.clients.auth-server")
public class LoginFeignClientProperties implements InitializingBean {
    private String name;
    private String url;
    private String path;
    private OAuth2FeignClientProperties oauth2 = new OAuth2FeignClientProperties();
    private FeignClientRetryerProperties retryer = new FeignClientRetryerProperties();

    @Override
    public void afterPropertiesSet() {
        Assert.notNull(name, "Login feign client does not have name property");
        Assert.notNull(url, "Login feign client does not have url property");
        Assert.notNull(path, "Login feign client does not have path property");
        Assert.isTrue(oauth2.isEnabled(), "Login feign client oauth2 properties have to be always true");

        String loginClientId = oauth2.getResource().getClientId();
        Assert.notNull(oauth2.getResource().getClientId(),
                "Login feign client '" + loginClientId + "' has to have clientId in properties");
        Assert.notNull(oauth2.getResource().getClientSecret(),
                "Login feign client '" + loginClientId + "' has to have secret in properties");
        Assert.notNull(oauth2.getResource().getAccessTokenUri(),
                "Login feign client '" + loginClientId + "' has to have accessTokenUri in properties");
        Assert.notNull(oauth2.getResource().getScope(),
                "Login feign client '" + loginClientId + "' has to have scope in properties");

        Assert.isTrue(retryer.getMaxAttempts() == 1,
                "Login feign client '" + loginClientId + "' has to have only one attempting for getting token");
    }

    @Data
    public static class OAuth2FeignClientProperties {
        private boolean enabled = true;
        private ClientCredentialsResourceDetails resource = new ClientCredentialsResourceDetails();
    }

    @Data
    public static class FeignClientRetryerProperties {
        private int period = 1000;
        private long maxPeriod = SECONDS.toMillis(1);
        private int maxAttempts = 1;
    }
}
