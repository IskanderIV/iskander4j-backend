package ru.cleverhause.apigateway.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.builders.InMemoryClientDetailsServiceBuilder;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.token.RemoteTokenServices;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.OrRequestMatcher;

@Configuration
@EnableResourceServer
public class ResourceServerConfig extends ResourceServerConfigurerAdapter {

    @Override
    public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
        resources.tokenServices(tokenServices());
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http
                .requestMatcher(new OrRequestMatcher(
                        new AntPathRequestMatcher("/api/**"),
                        new AntPathRequestMatcher("/actuator/beans")
                ))
                .authorizeRequests()
                .antMatchers("/actuator/beans").hasRole("ADMIN")
                .antMatchers("/api/**").access("#oauth2.hasScope('read')")
                .antMatchers("/**").authenticated();
    }

    @Bean
    public TokenStore tokenStore() {
        return new JwtTokenStore(accessTokenConverter());
    }

    @Bean
    public JwtAccessTokenConverter accessTokenConverter() {
        JwtAccessTokenConverter converter = new JwtAccessTokenConverter();
        converter.setSigningKey("123");
        return converter;
    }

    @Bean
    @Primary
    // needs only if you want to you want to check tokens remotely through auth server special endpoint
    public RemoteTokenServices tokenServices() {
        RemoteTokenServices tokenServices = new RemoteTokenServices();
        tokenServices.setAccessTokenConverter(accessTokenConverter());
        tokenServices.setCheckTokenEndpointUrl("http://localhost:8081/oauth/check_token");
        tokenServices.setClientId("client");
        tokenServices.setClientSecret("secret");
        return tokenServices;
    }

    @Bean
    @Primary
    public ClientDetailsService getClientDetailsService() throws Exception {
        InMemoryClientDetailsServiceBuilder clientDetailsServiceBuilder =
                new InMemoryClientDetailsServiceBuilder();
//        for (OAuth2ClientProperties.Client client : authClientProperties.getClients().values()) {
//
//        }
        clientDetailsServiceBuilder.withClient("client")
                .secret("secret");
        return clientDetailsServiceBuilder.build();
    }
}
