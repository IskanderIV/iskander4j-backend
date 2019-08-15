package ru.cleverhause.api.persist.entities;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by
 *
 * @author Aleksandr_Ivanov1
 * @date 5/28/2018.
 */
@Entity
@Table(name = "clever_schema.board")
public class Board implements Serializable {
    private Long id;
    private Long boardUID;
    private String boardName;
    private BoardStructure structure;
    private List<BoardSavedData> savedData = new ArrayList<>();
    private BoardControlData controlData;
    private User user;

    public Board(Long id, Long boardUID, String boardName, BoardStructure structure, List<BoardSavedData> savedData, BoardControlData controlData, User user) {
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

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY) //generator = "board_id_seq"
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

    @OneToOne(mappedBy = "board", cascade = CascadeType.ALL)
    public BoardStructure getStructure() {
        return structure;
    }

    public void setStructure(BoardStructure structure) {
        this.structure = structure;
    }

    @OneToMany(mappedBy = "board", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    public List<BoardSavedData> getSavedData() {
        return savedData;
    }

    public void setSavedData(List<BoardSavedData> savedData) {
        this.savedData = savedData;
    }

    @OneToOne(mappedBy = "board", cascade = CascadeType.ALL)
    public BoardControlData getControlData() {
        return controlData;
    }

    public void setControlData(BoardControlData controlData) {
        this.controlData = controlData;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

}
