package ru.cleverhause.users.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import java.time.Instant;
import java.util.Set;

@Entity
@Data
@IdClass(OAuth2AuthorizedClientId.class)
@NoArgsConstructor
@Table(name = "oauth2_authorized_client", schema = "clever_schema")
public class OAuth2AuthorizedClientEntity {
    @Id
    @Column(name = "client_registration_id", unique = true, nullable = false)
    private String clientRegistrationId;

    @Id
    @Column(name = "principal_name", unique = true, nullable = false)
    private String principalName;

    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private UserEntity user;

    @Column(name = "access_token_type", nullable = false)
    private String accessTokenType;

    @Column(name = "access_token_value", nullable = false)
    private String accessTokenValue;

    @Column(name = "access_token_issued_at", nullable = false)
    private Instant accessTokenIssuedAt;

    @Column(name = "access_token_expires_at", nullable = false)
    private Instant accessTokenExpiresAt;

    @Column(name = "access_token_scopes")
    private Set<String> accessTokenScopes;

    @Column(name = "refresh_token_value")
    private String refreshTokenValue;

    @Column(name = "refresh_token_issued_at")
    private Instant refreshTokenIssuedAt;

    @Column(name = "created_at")
    private Instant createdAt;

    @Builder
    @JsonCreator
    public OAuth2AuthorizedClientEntity(@JsonProperty("client_registration_id") String clientRegistrationId,
                                        @JsonProperty("principal_name") String principalName,
                                        @JsonProperty("user_id") UserEntity user,
                                        @JsonProperty("access_token_type") String accessTokenType,
                                        @JsonProperty("access_token_value") String accessTokenValue,
                                        @JsonProperty("access_token_expires_at") Instant accessTokenIssuedAt,
                                        @JsonProperty("access_token_expires_at") Instant accessTokenExpiresAt,
                                        @JsonProperty("access_token_scopes") Set<String> accessTokenScopes,
                                        @JsonProperty("refresh_token_value") String refreshTokenValue,
                                        @JsonProperty("refresh_token_issued_at") Instant refreshTokenIssuedAt,
                                        @JsonProperty("created_at") Instant createdAt) {
        this.clientRegistrationId = clientRegistrationId;
        this.principalName = principalName;
        this.user = user;
        this.accessTokenType = accessTokenType;
        this.accessTokenValue = accessTokenValue;
        this.accessTokenIssuedAt = accessTokenIssuedAt;
        this.accessTokenExpiresAt = accessTokenExpiresAt;
        this.accessTokenScopes = accessTokenScopes;
        this.refreshTokenValue = refreshTokenValue;
        this.refreshTokenIssuedAt = refreshTokenIssuedAt;
        this.createdAt = createdAt;
    }
}
