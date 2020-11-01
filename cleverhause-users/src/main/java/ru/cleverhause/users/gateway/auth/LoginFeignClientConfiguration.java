package ru.cleverhause.users.gateway.auth;

import feign.RequestInterceptor;
import feign.Retryer;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.security.oauth2.client.resource.OAuth2ProtectedResourceDetails;
import ru.cleverhause.users.config.properties.LoginFeignClientProperties;

import java.util.Base64;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@EnableConfigurationProperties({LoginFeignClientProperties.class})
public class LoginFeignClientConfiguration {

    private static final String BASIC = "Basic ";

    @Bean
    public RequestInterceptor interceptor(LoginFeignClientProperties auth2FeignClientsProperties) {
        return template -> {
            OAuth2ProtectedResourceDetails resource = auth2FeignClientsProperties.getOauth2().getResource();
            byte[] clientIdAndSecret = (resource.getClientId() + ":" + resource.getClientSecret()).getBytes();
            template.header(AUTHORIZATION, BASIC + Base64.getEncoder().encodeToString(clientIdAndSecret));
        };
    }

    @Bean
    protected Retryer getRetryer(LoginFeignClientProperties auth2FeignClientsProperties) {
        LoginFeignClientProperties.FeignClientRetryerProperties retryerProperties =
                auth2FeignClientsProperties.getRetryer();
        return new Retryer.Default(retryerProperties.getPeriod(),
                retryerProperties.getMaxPeriod(), retryerProperties.getMaxAttempts());
    }
}
