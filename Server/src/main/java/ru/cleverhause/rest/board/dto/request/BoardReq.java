package ru.cleverhause.rest.board.dto.request;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by
 *
 * @author Aleksandr_Ivanov1
 * @date 12/1/2017.
 */
public class BoardReq<T extends Serializable> implements Serializable {
    private String username;
    private String password;
    private String boardUID;
    private List<T> devices = new ArrayList<>();

    public BoardReq(String userName, String pass, String boardUID, List<T> devs) {
        this.username = userName;
        this.password = pass;
        this.boardUID = boardUID;
        this.devices = devs;
    }

    public BoardReq() {
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getBoardUID() {
        return boardUID;
    }

    public void setBoardUID(String boardUID) {
        this.boardUID = boardUID;
    }

    public List<T> getDevices() {
        return devices;
    }

    public void setDevices(List<T> devices) {
        this.devices = devices;
    }

    @Override
    public String toString() {
        return "BoardReq{" +
                "username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", boardUID='" + boardUID + '\'' +
                ", devices=" + devices +
                '}';
    }
}
