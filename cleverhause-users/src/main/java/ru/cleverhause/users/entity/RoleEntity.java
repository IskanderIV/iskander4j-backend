package ru.cleverhause.users.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import ru.cleverhause.users.authorities.Role;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@EqualsAndHashCode(exclude="users")
@ToString(exclude = "users")
@Table(name = "roles", schema = "clever_schema")
public class RoleEntity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_ID")
    @SequenceGenerator(name = "SEQ_ID", sequenceName = "roles_id_seq", schema = "clever_schema", allocationSize = 1)
    private Long id;

    @Column(name = "rolename")
    private String rolename;

    @JsonBackReference
    @ManyToMany(mappedBy = "roles", fetch = FetchType.EAGER)
    private Set<UserEntity> users = new HashSet<>();

    public RoleEntity() {
        this.rolename = Role.ROLE_USER.name();
    }
}
