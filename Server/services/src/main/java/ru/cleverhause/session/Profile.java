package ru.cleverhause.session;

import org.springframework.web.context.annotation.SessionScope;

import java.sql.Timestamp;

/**
 * Created by
 *
 * @author Aleksandr_Ivanov1
 * @date 12/5/2017.
 */
@SessionScope
public class Profile {
    private String username;
    private String sessionId;
    private boolean isExpired = true;
    private boolean isLogged = false;
    private String address;
    private String phoneNumber;
    private Timestamp loginTime;

    public Profile(String username,
                   String sessionId,
                   boolean isExpired,
                   boolean isLogged,
                   String address,
                   String phoneNumber,
                   Timestamp loginTime) {
        this.username = username;
        this.sessionId = sessionId;
        this.isExpired = isExpired;
        this.isLogged = isLogged;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.loginTime = loginTime;
    }

    public Profile() {
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public boolean isExpired() {
        return isExpired;
    }

    public void setExpired(boolean expired) {
        isExpired = expired;
    }

    public boolean isLogged() {
        return isLogged;
    }

    public void setLogged(boolean logged) {
        isLogged = logged;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public Timestamp getLoginTime() {
        return loginTime;
    }

    public void setLoginTime(Timestamp loginTime) {
        this.loginTime = loginTime;
    }
}
