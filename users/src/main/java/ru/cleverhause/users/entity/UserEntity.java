package ru.cleverhause.users.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.Objects;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import static javax.persistence.CascadeType.DETACH;
import static javax.persistence.CascadeType.MERGE;
import static javax.persistence.CascadeType.PERSIST;
import static javax.persistence.CascadeType.REFRESH;

@Entity
@Getter
@Setter
@EqualsAndHashCode(exclude = {"profile", "roles"})
@ToString(exclude = {"profile", "roles"})
@NoArgsConstructor
@Table(name = "users", schema = "clever_schema")
public class UserEntity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_ID")
    @SequenceGenerator(name = "SEQ_ID", sequenceName = "users_id_seq", schema = "clever_schema", allocationSize = 1)
    private Long id;
    @Column(name = "username", unique = true)
    private String username;
    @Column(name = "email")
    private String email;
    @Column(name = "password")
    private String password;

    @OneToOne(mappedBy = "user", cascade = {PERSIST, DETACH, MERGE, REFRESH}, fetch = FetchType.EAGER)
    @PrimaryKeyJoinColumn
    private Profile profile;

    @JsonManagedReference
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "clever_schema.user_roles", joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<RoleEntity> roles = new HashSet<>();

    @Builder
    @JsonCreator
    public UserEntity(@JsonProperty("id") Long id,
                      @JsonProperty("username") String username,
                      @JsonProperty("email") String email,
                      @JsonProperty("password") String password,
                      @JsonProperty("profile") Profile profile,
                      @JsonProperty("roles") Set<RoleEntity> roles) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.password = password;
        this.roles = roles;
        this.profile = profile;
    }
}