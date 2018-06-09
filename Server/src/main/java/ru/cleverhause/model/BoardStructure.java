package ru.cleverhause.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.MapsId;
import javax.persistence.OneToOne;
import javax.persistence.Table;

/**
 * Created by
 *
 * @author Aleksandr_Ivanov1
 * @date 6/10/2018.
 */
@Entity
@Table(name = "boardStructure")
public class BoardStructure {
    @Id
    @Column(name = "id")
    @GeneratedValue
    private Long id;

    @Column(name = "structure")
    private String structure;

    @OneToOne
    @MapsId
    @JoinColumn(name = "board_id")
    private Board board;

    public BoardStructure(Long id, String structure, Board board) {
        this.id = id;
        this.structure = structure;
        this.board = board;
    }

    public BoardStructure() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getStructure() {
        return structure;
    }

    public void setStructure(String structure) {
        this.structure = structure;
    }

    public Board getBoard() {
        return board;
    }

    public void setBoard(Board board) {
        this.board = board;
    }

    @Override
    public String toString() {
        return "BoardStructure{" +
                "id=" + id +
                ", structure='" + structure + '\'' +
                ", board=" + board +
                '}';
    }
}
