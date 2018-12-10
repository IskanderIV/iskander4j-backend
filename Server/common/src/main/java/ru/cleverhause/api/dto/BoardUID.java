package ru.cleverhause.api.dto;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * Created by
 *
 * @author Aleksandr_Ivanov1
 * @date 6/12/2018.
 */
@Setter
@Getter
public class BoardUID implements Serializable {
    private String boardUID;

    public BoardUID(String boardUID) {
        this.boardUID = boardUID;
    }

    public BoardUID() {
    }

    @Override
    public String toString() {
        return "BoardUID{" +
                "boardUID='" + boardUID + '\'' +
                '}';
    }
}
