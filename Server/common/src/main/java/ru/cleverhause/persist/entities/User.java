package ru.cleverhause.persist.entities;

import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

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
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.io.Serializable;
import java.util.Collections;
import java.util.Set;

/**
 * Simple JavaBean object that represents a User
 * <p>
 * Created by
 *
 * @author Aleksandr_Ivanov1
 * @date 3/2/2018.
 */

@Entity
@Table(name = "clever_schema.users")
public class User implements Serializable {
    private Long id;
    private String username;
    private String password;
    private String confirmPassword;
    private Set<Role> roles = Collections.emptySet();
    private Set<Board> boards = Collections.emptySet();
    private NewBoardUID newBoardUID;

    public User(Long id, String username, String password, String confirmPassword, Set<Role> roles, Set<Board> boards, NewBoardUID newBoardUID) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.confirmPassword = confirmPassword;
        this.roles = roles;
        this.boards = boards;
        this.newBoardUID = newBoardUID;
    }

    public User() {
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Column(name = "username")
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Column(name = "password")
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Transient
    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }

    @ManyToMany
    @JoinTable(name = "clever_schema.user_roles", joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    @LazyCollection(value = LazyCollectionOption.FALSE)
    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @LazyCollection(value = LazyCollectionOption.FALSE)
    public Set<Board> getBoards() {
        return boards;
    }

    public void setBoards(Set<Board> boards) {
        this.boards = boards;
    }

    @OneToOne(mappedBy = "user")
    //if I use "cascade = CascadeType.ALL" than no deleting on delete(NewBoardUID). If mappedBy than User is not controller on bidirectional link
    public NewBoardUID getNewBoardUID() {
        return newBoardUID;
    }

    public void setNewBoardUID(NewBoardUID newBoardUID) {
        this.newBoardUID = newBoardUID;
    }
}