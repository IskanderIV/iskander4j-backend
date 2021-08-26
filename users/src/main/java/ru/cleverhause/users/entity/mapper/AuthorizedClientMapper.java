package ru.cleverhause.users.entity.mapper;

import org.springframework.dao.DataRetrievalFailureException;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.OAuth2RefreshToken;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import ru.cleverhause.users.entity.OAuth2AuthorizedClientEntity;

import java.time.Instant;
import java.util.Set;

@Component
public class AuthorizedClientMapper {

    public OAuth2AuthorizedClientEntity from(final OAuth2AuthorizedClient authorizedClient, final Authentication principal) {
        ClientRegistration clientRegistration = authorizedClient.getClientRegistration();
        OAuth2AccessToken accessToken = authorizedClient.getAccessToken();
        OAuth2RefreshToken refreshToken = authorizedClient.getRefreshToken();

        String refreshTokenValue = null;
        Instant refreshTokenIssuedAt = null;
        if (refreshToken != null) {
            refreshTokenValue = refreshToken.getTokenValue();
            if (refreshToken.getIssuedAt() != null) {
                refreshTokenIssuedAt = refreshToken.getIssuedAt();
            }
        }
        return OAuth2AuthorizedClientEntity.builder()
                .clientRegistrationId(clientRegistration.getRegistrationId())
                .principalName(principal.getName())
                .accessTokenType(accessToken.getTokenType().getValue())
                .accessTokenValue(accessToken.getTokenValue())
                .accessTokenScopes(accessToken.getScopes())
                .accessTokenExpiresAt(accessToken.getExpiresAt())
                .accessTokenIssuedAt(accessToken.getIssuedAt())
                .createdAt(Instant.now())
                .refreshTokenValue(refreshTokenValue)
                .refreshTokenIssuedAt(refreshTokenIssuedAt)
                .build();
    }

    @SuppressWarnings("unchecked")
    public <T extends OAuth2AuthorizedClient> T from(final OAuth2AuthorizedClientEntity entity,
                                                     final ClientRegistrationRepository clientRegistrationRepository) {
        ClientRegistration clientRegistration = getClientRegistration(entity, clientRegistrationRepository);
        OAuth2AccessToken accessToken = getOAuth2AccessToken(entity);
        OAuth2RefreshToken refreshToken = getOAuth2RefreshToken(entity);
        String principalName = entity.getPrincipalName();
        return (T) new OAuth2AuthorizedClient(clientRegistration, principalName, accessToken, refreshToken);
    }

    private ClientRegistration getClientRegistration(OAuth2AuthorizedClientEntity entity, ClientRegistrationRepository clientRegistrationRepository) {
        String clientRegistrationId = entity.getClientRegistrationId();
        ClientRegistration clientRegistration = clientRegistrationRepository.findByRegistrationId(clientRegistrationId);
        if (clientRegistration == null) {
            throw new DataRetrievalFailureException("The ClientRegistration with id '" +
                    clientRegistrationId + "' exists in the data source, " +
                    "however, it was not found in the ClientRegistrationRepository.");
        }
        return clientRegistration;
    }

    private OAuth2AccessToken getOAuth2AccessToken(OAuth2AuthorizedClientEntity entity) {
        OAuth2AccessToken.TokenType tokenType = null;
        if (OAuth2AccessToken.TokenType.BEARER.getValue().equalsIgnoreCase(entity.getAccessTokenType())) {
            tokenType = OAuth2AccessToken.TokenType.BEARER;
        }
        String tokenValue = entity.getAccessTokenValue();
        Instant issuedAt = entity.getAccessTokenIssuedAt();
        Instant expiresAt = entity.getAccessTokenExpiresAt();
        Set<String> scopes = Set.copyOf(entity.getAccessTokenScopes());
        return new OAuth2AccessToken(
                tokenType, tokenValue, issuedAt, expiresAt, scopes);
    }

    private OAuth2RefreshToken getOAuth2RefreshToken(OAuth2AuthorizedClientEntity entity) {
        OAuth2RefreshToken refreshToken = null;
        String refreshTokenValue = entity.getRefreshTokenValue();
        if (StringUtils.hasText(refreshTokenValue)) {
            refreshToken = new OAuth2RefreshToken(refreshTokenValue, entity.getRefreshTokenIssuedAt());
        }
        return refreshToken;
    }
}
