package ru.cleverhause.device.dto;

import java.io.Serializable;

/**
 * Created by
 *
 * @author Aleksandr_Ivanov1
 * @date 6/12/2018.
 */

public class BoardUidDto implements Serializable {
    private String boardUID;

    public BoardUidDto(String boardUID) {
        this.boardUID = boardUID;
    }

    public String getBoardUID() {
        return boardUID;
    }

    public void setBoardUID(String boardUID) {
        this.boardUID = boardUID;
    }

    @Override
    public String toString() {
        return "BoardUidDto{" +
                "boardUID='" + boardUID + '\'' +
                '}';
    }
}
