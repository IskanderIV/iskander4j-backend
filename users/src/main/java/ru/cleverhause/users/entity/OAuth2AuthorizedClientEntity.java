package ru.cleverhause.users.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.CollectionUtils;

import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import java.time.Instant;
import java.util.Arrays;
import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;

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

    /**
     * FK.
     */
    @OneToOne(fetch = FetchType.EAGER)
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
    private String accessTokenScopes;

    @Column(name = "refresh_token_value")
    private String refreshTokenValue;

    @Column(name = "refresh_token_issued_at")
    private Instant refreshTokenIssuedAt;

    @Column(name = "created_at")
    private Instant createdAt;

    @Builder
    @JsonCreator
    public OAuth2AuthorizedClientEntity(@JsonProperty("clientRegistrationId") String clientRegistrationId,
                                        @JsonProperty("principalName") String principalName,
                                        @JsonProperty("user") UserEntity user,
                                        @JsonProperty("accessTokenType") String accessTokenType,
                                        @JsonProperty("accessTokenValue") String accessTokenValue,
                                        @JsonProperty("accessTokenIssuedAt") Instant accessTokenIssuedAt,
                                        @JsonProperty("accessTokenExpiresAt") Instant accessTokenExpiresAt,
                                        @JsonProperty("accessTokenScopes") Set<String> accessTokenScopes,
                                        @JsonProperty("refreshTokenValue") String refreshTokenValue,
                                        @JsonProperty("refreshTokenIssuedAt") Instant refreshTokenIssuedAt,
                                        @JsonProperty("createdAt") Instant createdAt) {
        this.clientRegistrationId = clientRegistrationId;
        this.principalName = principalName;
        this.user = user;
        this.accessTokenType = accessTokenType;
        this.accessTokenValue = accessTokenValue;
        this.accessTokenIssuedAt = accessTokenIssuedAt;
        this.accessTokenExpiresAt = accessTokenExpiresAt;
        setAccessTokenScopes(accessTokenScopes);
        this.refreshTokenValue = refreshTokenValue;
        this.refreshTokenIssuedAt = refreshTokenIssuedAt;
        this.createdAt = createdAt;
    }

    public Set<String> getAccessTokenScopes() {
        if (StringUtils.isBlank(accessTokenScopes)) {
            return Collections.emptySet();
        } else {
            return Arrays.stream(StringUtils.split(accessTokenScopes, ",")).collect(Collectors.toSet());
        }
    }

    public void setAccessTokenScopes(Set<String> accessTokenScopes) {
        if (CollectionUtils.isEmpty(accessTokenScopes)) {
            this.accessTokenScopes =  StringUtils.EMPTY;
        } else {
            this.accessTokenScopes = StringUtils.join(accessTokenScopes, ",");
        }
    }
}
