package ru.cleverhause.model;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Set;

/**
 * Created by
 *
 * @author Aleksandr_Ivanov1
 * @date 5/28/2018.
 */
@Entity
@Table(name = "board")
public class Board implements Serializable {

    @Id
    @Column(name = "id")
    @GeneratedValue
    private Long id;

    @Column(name = "boardUID")
    private String boardUID;

    @Column(name = "boardname")
    private String boardName;

    @OneToOne(mappedBy = "board", cascade = CascadeType.ALL)
    private BoardStructure structure;

    @OneToMany(mappedBy = "board", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Set<BoardSavedData> savedData;

    @OneToOne(mappedBy = "board", cascade = CascadeType.ALL)
    private BoardControlData controlData;

    @ManyToOne
    @JoinTable(name = "user_boards", joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "board_id"))
    private User user;

    public Board(Long id, String boardUID, String boardName, BoardStructure structure, Set<BoardSavedData> savedData, BoardControlData controlData, User user) {
        this.id = id;
        this.boardUID = boardUID;
        this.boardName = boardName;
        this.structure = structure;
        this.savedData = savedData;
        this.controlData = controlData;
        this.user = user;
    }

    public Board() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getBoardUID() {
        return boardUID;
    }

    public void setBoardUID(String boardUID) {
        this.boardUID = boardUID;
    }

    public String getBoardName() {
        return boardName;
    }

    public void setBoardName(String boardName) {
        this.boardName = boardName;
    }

    public BoardStructure getStructure() {
        return structure;
    }

    public void setStructure(BoardStructure structure) {
        this.structure = structure;
    }

    public Set<BoardSavedData> getSavedData() {
        return savedData;
    }

    public void setSavedData(Set<BoardSavedData> savedData) {
        this.savedData = savedData;
    }

    public BoardControlData getControlData() {
        return controlData;
    }

    public void setControlData(BoardControlData controlData) {
        this.controlData = controlData;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public String toString() {
        return "Board{" +
                "id=" + id +
                ", boardUID='" + boardUID + '\'' +
                ", boardName='" + boardName + '\'' +
                ", structure=" + structure +
                ", savedData=" + savedData +
                ", controlData=" + controlData +
                ", user=" + user +
                '}';
    }
}
