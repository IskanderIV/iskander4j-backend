package ru.cleverhause.auth.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.config.annotation.builders.InMemoryClientDetailsServiceBuilder;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.CompositeTokenGranter;
import org.springframework.security.oauth2.provider.OAuth2RequestFactory;
import org.springframework.security.oauth2.provider.TokenGranter;
import org.springframework.security.oauth2.provider.client.ClientCredentialsTokenGranter;
import org.springframework.security.oauth2.provider.code.AuthorizationCodeTokenGranter;
import org.springframework.security.oauth2.provider.code.InMemoryAuthorizationCodeServices;
import org.springframework.security.oauth2.provider.password.ResourceOwnerPasswordTokenGranter;
import org.springframework.security.oauth2.provider.request.DefaultOAuth2RequestFactory;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.TokenEnhancerChain;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;
import ru.cleverhause.auth.config.properties.OAuth2ClientProperties;

import java.util.Arrays;
import java.util.Collections;

@Configuration
@EnableAuthorizationServer
@RequiredArgsConstructor
public class Oauth2ServerConfig extends AuthorizationServerConfigurerAdapter {

    private final AuthenticationManager authenticationManager;

    private final OAuth2ClientProperties authClientProperties;

    private final UserDetailsService userDetailsService;

    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        endpoints
                .tokenStore(tokenStore())
                .tokenEnhancer(tokenEnhancerChain())
                .accessTokenConverter(accessTokenConverter())
                .tokenGranter(getMixedTokenGranter())
                .authenticationManager(authenticationManager)
                .userDetailsService(userDetailsService)
                .tokenServices(tokenServices())
                .allowedTokenEndpointRequestMethods(HttpMethod.POST, HttpMethod.GET)
                .requestFactory(getRequestFactory())
                .setClientDetailsService(getClientDetailsService());
    }

    @Override
    public void configure(AuthorizationServerSecurityConfigurer oauthServer) {
        oauthServer
                .tokenKeyAccess("denyAll()")
                .checkTokenAccess("isAuthenticated()");
    }

//    @Override
//    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
//        for (OAuth2ClientProperties.Client client : authClientProperties.getClients().values()) {
//            clients.inMemory().withClient(client.getClientId())
//                    .secret(client.getSecret())
//                    .authorizedGrantTypes(client.getGrandTypes().toArray(new String[0]))
//                    .scopes(client.getScopes().toArray(new String[0]))
//                    .accessTokenValiditySeconds(client.getAccessTokenLifeSecond());
//        }
//    }

    @Bean
    @Primary
    public ClientDetailsService getClientDetailsService() throws Exception {
        InMemoryClientDetailsServiceBuilder clientDetailsServiceBuilder =
                new InMemoryClientDetailsServiceBuilder();
        for (OAuth2ClientProperties.Client client : authClientProperties.getClients().values()) {
            clientDetailsServiceBuilder.withClient(client.getClientId())
                    .secret(client.getSecret())
                    .authorizedGrantTypes(client.getGrandTypes().toArray(new String[0]))
                    .scopes(client.getScopes().toArray(new String[0]))
                    .accessTokenValiditySeconds(client.getAccessTokenLifeSecond())
                    .refreshTokenValiditySeconds(client.getRefreshTokenLifeSecond());
        }
        return clientDetailsServiceBuilder.build();
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
    // needs only if you want to have refresh_token with grant_type=client_credentials
    public DefaultTokenServices tokenServices() {
        DefaultTokenServices tokenServices = new DefaultTokenServices();
        tokenServices.setSupportRefreshToken(true);
        tokenServices.setTokenStore(tokenStore());
        tokenServices.setRefreshTokenValiditySeconds(24 * 60 * 60);
        tokenServices.setTokenEnhancer(tokenEnhancerChain());
        return tokenServices;
    }

    @Bean
    public TokenGranter getMixedTokenGranter() throws Exception {
        return new CompositeTokenGranter(Arrays.asList(getClientCredentialsTokenGranter(), getPasswordTokenGranter()));
    }

    @Bean
    public ClientCredentialsTokenGranter getClientCredentialsTokenGranter() throws Exception {
        ClientCredentialsTokenGranter tokenGranter = new ClientCredentialsTokenGranter(tokenServices(), getClientDetailsService(), getRequestFactory());
        tokenGranter.setAllowRefresh(true);
        return tokenGranter;
    }

    @Bean
    public ResourceOwnerPasswordTokenGranter getPasswordTokenGranter() throws Exception {
        return new ResourceOwnerPasswordTokenGranter(authenticationManager,
                tokenServices(), getClientDetailsService(), getRequestFactory());
    }

    @Bean
    public AuthorizationCodeTokenGranter getAuthorizationCodeTokenGranter() throws Exception {
        return new AuthorizationCodeTokenGranter(tokenServices(), getAuthorizationCodeServices(), getClientDetailsService(), getRequestFactory());
    }

    @Bean
    public InMemoryAuthorizationCodeServices getAuthorizationCodeServices() {
        return new InMemoryAuthorizationCodeServices();
    }

    @Bean
    public OAuth2RequestFactory getRequestFactory() throws Exception {
        return new DefaultOAuth2RequestFactory(getClientDetailsService());
    }

    @Bean
    @Primary
    public TokenEnhancerChain tokenEnhancerChain() {
        TokenEnhancerChain tokenEnhancerChain = new TokenEnhancerChain();
        tokenEnhancerChain.setTokenEnhancers(
                Collections.singletonList(accessTokenConverter()));
        return tokenEnhancerChain;
    }
}
