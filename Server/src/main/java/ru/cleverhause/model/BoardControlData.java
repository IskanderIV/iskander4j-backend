package ru.cleverhause.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.MapsId;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import java.util.Date;

/**
 * Created by
 *
 * @author Aleksandr_Ivanov1
 * @date 6/10/2018.
 */
@Entity
@Table(name = "boardControlData")
public class BoardControlData {
    @Id
    @Column(name = "id")
    @GeneratedValue
    private Long id;

    @Column(name = "data")
    private String data;

    @Column(name = "created")
    private Date created;

    @OneToOne
    @MapsId
    @JoinColumn(name = "board_id")
    private Board board;

    public BoardControlData(Long id, String data, Date created, Board board) {
        this.id = id;
        this.data = data;
        this.created = created;
        this.board = board;
    }

    public BoardControlData() {
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

    @Override
    public String toString() {
        return "DeviceControl{" +
                "id=" + id +
                ", data='" + data + '\'' +
                ", created=" + created +
                ", board=" + board +
                '}';
    }
}
