package ru.cleverhause.auth.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class AuthenticationBaseDto implements Serializable {
    private String userId;

}
