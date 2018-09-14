package ru.cleverhause.persist.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * Created by
 *
 * @author Aleksandr_Ivanov1
 * @date 8/10/2018.
 */
@Entity
@Table(name = "clever_schema.user_new_board")
public class NewBoardUID implements Serializable {
    private Long id;
    private Long boardUID;
    private String boardName;
    private User user;

    public NewBoardUID(Long id, Long boardUID, String boardName, User user) {
        this.id = id;
        this.boardUID = boardUID;
        this.boardName = boardName;
        this.user = user;
    }

    public NewBoardUID() {
    }

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Column(name = "boardUID")
    public Long getBoardUID() {
        return boardUID;
    }

    public void setBoardUID(Long boardUID) {
        this.boardUID = boardUID;
    }

    @Column(name = "boardname")
    public String getBoardName() {
        return boardName;
    }

    public void setBoardName(String boardName) {
        this.boardName = boardName;
    }

    @OneToOne
    @JoinColumn(name = "user_id")
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
