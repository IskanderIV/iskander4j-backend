package ru.cleverhause.users.dto.request;

import lombok.Builder;
import lombok.Value;

import java.io.Serializable;

@Value
@Builder
public class UserInfoRequest implements Serializable {
    private final Long userId;
    private final String userName;
}
