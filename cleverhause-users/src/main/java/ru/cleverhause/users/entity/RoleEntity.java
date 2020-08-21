package ru.cleverhause.users.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import ru.cleverhause.users.authorities.Role;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Set;

@Entity
@Data
@Table(name = "roles", schema = "clever_schema")
public class RoleEntity implements Serializable {

    public RoleEntity() {
        this.rolename = Role.ROLE_USER;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_ID")
    @SequenceGenerator(name = "SEQ_ID", sequenceName = "roles_id_seq", schema = "clever_schema")
    private Long id;

    @Column(name = "rolename")
    private Role rolename;

    @ManyToMany(mappedBy = "roles")
    private Set<UserEntity> users;
}
