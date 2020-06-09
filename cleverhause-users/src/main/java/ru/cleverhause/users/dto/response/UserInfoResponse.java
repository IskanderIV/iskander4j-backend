package ru.cleverhause.users.dto.response;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

@Data
@Builder
public class UserInfoResponse implements Serializable {
    private Long userId;
    private String username;
    private String email;
}
