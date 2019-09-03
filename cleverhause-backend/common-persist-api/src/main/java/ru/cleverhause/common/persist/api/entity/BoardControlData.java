package ru.cleverhause.common.persist.api.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

/**
 * Created by
 *
 * @author Aleksandr_Ivanov1
 * @date 6/10/2018.
 */
@Entity
@Table(name = "clever_schema.boardControlData")
public class BoardControlData implements Serializable {
    private Long id;
    private String data;
    private Date created;
    private Board board;

    public BoardControlData(Long id, String data, Date created, Board board) {
        this.id = id;
        this.data = data;
        this.created = created;
        this.board = board;
    }

    public BoardControlData() {
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

    @Column(name = "data")
    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    @Column(name = "created")
    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    @OneToOne
    @JoinColumn(name = "board_id")
    public Board getBoard() {
        return board;
    }

    public void setBoard(Board board) {
        this.board = board;
    }

    //    @Override
//    public String toString() {
//        return "DeviceControl{" +
//                "id=" + id +
//                ", data='" + data + '\'' +
//                ", created=" + created +
//                ", boardId=" + boardId +
//                '}';
//    }
}
