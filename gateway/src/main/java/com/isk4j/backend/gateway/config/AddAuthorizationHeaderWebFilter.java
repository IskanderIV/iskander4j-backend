package com.isk4j.backend.gateway.config;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.web.server.WebSessionServerOAuth2AuthorizedClientRepository;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class AddAuthorizationHeaderWebFilter implements WebFilter {

    private final WebSessionServerOAuth2AuthorizedClientRepository authorizedClientRepository =
            new WebSessionServerOAuth2AuthorizedClientRepository();
    private final String registrationId;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        return ReactiveSecurityContextHolder.getContext()
                .map(SecurityContext::getAuthentication)
                .flatMap(authentication -> this.authorizedClientRepository.<OAuth2AuthorizedClient>loadAuthorizedClient(registrationId, authentication, exchange))
                .map(authorizedClient -> exchange.getRequest().mutate()
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + authorizedClient.getAccessToken().getTokenValue())
                        .build())
                .then(chain.filter(exchange));
    }
}
