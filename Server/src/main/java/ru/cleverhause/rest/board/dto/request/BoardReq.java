package ru.cleverhause.rest.board.dto.request;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

/**
 * Created by
 *
 * @author Aleksandr_Ivanov1
 * @date 12/1/2017.
 */
@Getter
@Setter
public class BoardReq<T extends Serializable> implements Serializable {
    private String username;
    private String password;
    private String boardPID;
    private List<T> devices;

    public BoardReq(String username, String password, String boardPID, List<T> devices) {
        this.username = username;
        this.password = password;
        this.boardPID = boardPID;
        this.devices = devices;
    }

    public BoardReq() {
    }

    @Override
    public String toString() {
        return "BoardReq{" +
                "username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", boardPID='" + boardPID + '\'' +
                ", devices=" + devices +
                '}';
    }
}
