package ru.cleverhause.api.dto.page;

import java.io.Serializable;

/**
 * Created by
 *
 * @author Aleksandr_Ivanov1
 * @date 8/9/2018.
 */

public class BoardDto_MyBoardsJsp implements Serializable {
    private Long boardUID;
    private String name;
    private String numOfDevices;

    public BoardDto_MyBoardsJsp(Long boardUID, String name, String numOfDevices) {
        this.boardUID = boardUID;
        this.name = name;
        this.numOfDevices = numOfDevices;
    }

    public BoardDto_MyBoardsJsp() {
    }

    public Long getBoardUID() {
        return boardUID;
    }

    public void setBoardUID(Long boardUID) {
        this.boardUID = boardUID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNumOfDevices() {
        return numOfDevices;
    }

    public void setNumOfDevices(String numOfDevices) {
        this.numOfDevices = numOfDevices;
    }
}
