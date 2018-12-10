package ru.cleverhause.api.persist.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.io.Serializable;
import java.util.Date;

/**
 * Created by
 *
 * @author Aleksandr_Ivanov1
 * @date 6/7/2018.
 */
@Entity
@Table(name = "clever_schema.boardSavedData")
public class BoardSavedData implements Serializable {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "data")
    private String data;

    @Temporal(TemporalType.DATE)
    @Column(name = "created")
    private Date created;

    @ManyToOne
    @JoinColumn(name = "board_id")
    private Board board;

    public BoardSavedData(String data, Date created, Board board) {
//        this.id = id;
        this.data = data;
        this.created = created;
        this.board = board;
    }

    public BoardSavedData() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public Board getBoard() {
        return board;
    }

    public void setBoard(Board board) {
        this.board = board;
    }

//    @Override
//    public String toString() {
//        return "BoardSavedData{" +
//                "id=" + id +
//                ", data='" + data + '\'' +
//                ", created=" + created +
//                ", board=" + board +
//                '}';
//    }
}
