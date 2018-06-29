package ru.cleverhause.persist.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
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
    private Long id;
    private String structure;
    private Board board;

    public BoardStructure(String structure, Board board) {
        this.structure = structure;
        this.board = board;
    }

    public BoardStructure() {
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

    @Column(name = "structure")
    public String getStructure() {
        return structure;
    }

    public void setStructure(String structure) {
        this.structure = structure;
    }

    @OneToOne
    @MapsId
    @JoinColumn(name = "board_id")
    public Board getBoard() {
        return board;
    }

    public void setBoard(Board board) {
        this.board = board;
    }

//    @Override
//    public String toString() {
//        return "BoardStructure{" +
//                "id=" + id +
//                ", structure='" + structure + '\'' +
//                ", board=" + board +
//                '}';
//    }
}
