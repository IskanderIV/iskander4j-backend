package com.isk4j.backend.gateway.config;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.oauth2.client.web.server.WebSessionServerOAuth2AuthorizedClientRepository;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.authentication.RedirectServerAuthenticationEntryPoint;
import org.springframework.security.web.server.context.NoOpServerSecurityContextRepository;
import org.springframework.security.web.server.context.WebSessionServerSecurityContextRepository;
import org.springframework.security.web.server.util.matcher.OrServerWebExchangeMatcher;
import org.springframework.security.web.server.util.matcher.PathPatternParserServerWebExchangeMatcher;
import org.springframework.web.server.WebFilter;

import static org.springframework.security.config.web.server.SecurityWebFiltersOrder.AUTHORIZATION;

@Configuration
@EnableWebFluxSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    @Order(9)
    @RequiredArgsConstructor
    public static class AuthorizationEndpointsSecurityConfig {

        @Bean
        public SecurityWebFilterChain oauth2SpringSecurityFilterChain(ServerHttpSecurity http) {
            // @formatter:off
            http
                    .securityMatcher(new OrServerWebExchangeMatcher(
                            new PathPatternParserServerWebExchangeMatcher("/oauth2/authorization/**", HttpMethod.GET),
                            new PathPatternParserServerWebExchangeMatcher("/login/**", HttpMethod.GET)
                    ))
                    .oauth2Login(oauth2ClientSpec -> oauth2ClientSpec
                                    .authorizedClientRepository(new WebSessionServerOAuth2AuthorizedClientRepository())
                                    .securityContextRepository(new WebSessionServerSecurityContextRepository())
                    );
            // он должен создавать этот контекст, а не вытаскивать
            http.securityContextRepository(NoOpServerSecurityContextRepository.getInstance());
            http.csrf().disable();
            http.logout().disable();
            // @formatter:on
            return http.build();
        }
    }

    @RequiredArgsConstructor
    @Order(12)
    public static class RoutingEndpointsSecurityConfig {


        @Value("${uri.redirect.unauthorized}")
        public String unauthorizedRedirectUri;
        @Value("${uri.redirect.default.host}")
        public String defaultUrlHost;
        @Value("${uri.redirect.default.path}")
        public String defaultUrlPath;
        @Value("${spring.security.oauth2.client.registration.gateway.client-id}")
        public String gatewayClientId;
        @Value("${spring.security.oauth2.client.provider.keycloak.check-token-uri}")
        public String checkTokenEndpointUrl;

        @Bean
        public SecurityWebFilterChain authorizedRoutingSpringSecurityFilterChain(ServerHttpSecurity http) {
            // @formatter:off
            http
                    .addFilterAfter(webFilter(), AUTHORIZATION)
                    .authorizeExchange(exchanges -> exchanges.anyExchange().hasRole("USER"))
                    .exceptionHandling()
                        .authenticationEntryPoint(new RedirectServerAuthenticationEntryPoint("http://localhost:8090/login"))
                    .and()
                    .securityContextRepository(new WebSessionServerSecurityContextRepository());
            http.csrf().disable();
            // @formatter:on
            return http.build();
        }

        // it should not be a Bean otherwise it will be double added to Filters
        private WebFilter webFilter() {
            return new AddAuthorizationHeaderWebFilter(gatewayClientId);
        }

    }
}
