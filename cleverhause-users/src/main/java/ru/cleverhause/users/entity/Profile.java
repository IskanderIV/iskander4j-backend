package ru.cleverhause.users.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

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
@Data
@NoArgsConstructor
@Table(name = "profile", schema = "clever_schema")
public class Profile {
    @Id
    @Column(name = "user_id")
    private Long id;

    @OneToOne(mappedBy = "profile", fetch = FetchType.LAZY)
    @MapsId
    @JoinColumn(name = "user_id")
    private UserEntity user;

    @Column(name = "session_id")
    private String sessionId;

    @Column(name = "is_expired")
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
}
