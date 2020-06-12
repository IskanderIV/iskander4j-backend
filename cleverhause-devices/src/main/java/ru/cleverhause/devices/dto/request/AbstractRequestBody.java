package ru.cleverhause.devices.dto.request;

import java.io.Serializable;

/**
 * Created by
 *
 * @author Aleksandr_Ivanov1
 * @date 12/1/2017.
 */
public abstract class AbstractRequestBody implements Serializable {
    private String username;
    private String password;
    private Long boardUID;

    protected AbstractRequestBody(String userName, String pass, Long boardUID) {
        this.username = userName;
        this.password = pass;
        this.boardUID = boardUID;
    }

    protected AbstractRequestBody() {
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

    @Override
    public String toString() {
        return "AbstractRequestBody{" +
                "username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", boardUID=" + boardUID +
                '}';
    }
}
