package ru.cleverhause.rest.frontend.dto.request;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by
 *
 * @author Aleksandr_Ivanov1
 * @date 12/1/2017.
 */
public class UIRequest<T extends Serializable> implements Serializable {
    private String username;
    private String password;
    private Long boardUID;
    private T input;

    public UIRequest(String userName, String pass, Long boardUID, T devs) {
        this.username = userName;
        this.password = pass;
        this.boardUID = boardUID;
        this.input = devs;
    }

    public UIRequest() {
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

    public Long getBoardUID() {
        return boardUID;
    }

    public void setBoardUID(Long boardUID) {
        this.boardUID = boardUID;
    }

    public T getInput() {
        return input;
    }

    public void setInput(T input) {
        this.input = input;
    }

    @Override
    public String toString() {
        return "BoardReq{" +
                "username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", boardUID='" + boardUID + '\'' +
                ", input=" + input +
                '}';
    }
}
