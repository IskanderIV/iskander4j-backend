package ru.cleverhause.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Set;

/**
 * Simple JavaBean object which represents role of {@link User}
 * <p>
 * Created by
 *
 * @author Aleksandr_Ivanov1
 * @version 1.0.0
 * @date 3/2/2018
 */

@Entity
@Table(name = "roles")
public class Role implements Serializable {

    private static final long serialVersionUID = -6734364190420371719L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // learn about diff types of strategies
    private Long id;

    @Column(name = "rolename")
    private String rolename;

    @ManyToMany(mappedBy = "roles")
    private Set<User> users;

    public Role() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRolename() {
        return rolename;
    }

    public void setRolename(String rolename) {
        this.rolename = rolename;
    }

    public Set<User> getUsers() {
        return users;
    }

    public void setUsers(Set<User> users) {
        this.users = users;
    }

    @Override
    public String toString() {
        return "Role{" +
                "id=" + id +
                ", rolename='" + rolename + '\'' +
                ", users=" + users +
                '}';
    }
}
