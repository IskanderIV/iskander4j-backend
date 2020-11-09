package ru.cleverhause.users.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.Objects;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.MapsId;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import java.time.Instant;

@Entity
@Getter
@Setter
@EqualsAndHashCode(exclude="user")
@ToString(exclude = "user")
@NoArgsConstructor
@Table(name = "profile", schema = "clever_schema")
public class Profile {
    @Id
    @Column(name = "user_id")
    private Long id;

    /**
     * Shared PK
     */
    @OneToOne(fetch = FetchType.EAGER)
    @MapsId
    @JoinColumn(name = "user_id")
    private UserEntity user;

    @Column(name = "session_id")
    private String sessionId;

    @Column(name = "is_expired", nullable = false)
    private Boolean isExpired;

    @Column(name = "is_logged_in")
    private Boolean isLoggedIn;

    @Column(name = "address")
    private String address;

    @Column(name = "phone")
    private String phone;

    @Column(name = "created_at")
    @CreationTimestamp
    private Instant createdAt;

    @Column(name = "last_login_time")
    private Instant lastLoginTime;

    @Builder
    @JsonCreator
    public Profile(@JsonProperty("id") Long id,
                   @JsonProperty("user") UserEntity user,
                   @JsonProperty("sessionId") String sessionId,
                   @JsonProperty("isExpired") Boolean isExpired,
                   @JsonProperty("isLoggedIn") Boolean isLoggedIn,
                   @JsonProperty("address") String address,
                   @JsonProperty("phone") String phone,
                   @JsonProperty("lastLoginTime") Instant lastLoginTime) {
        this.id = id;
        this.user = user;
        this.sessionId = sessionId;
        this.isExpired = isExpired;
        this.isLoggedIn = isLoggedIn;
        this.address = address;
        this.phone = phone;
        this.createdAt = Instant.now();
        this.lastLoginTime = lastLoginTime;
    }
}
