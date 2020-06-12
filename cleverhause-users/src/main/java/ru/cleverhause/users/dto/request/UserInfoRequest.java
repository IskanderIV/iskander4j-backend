package ru.cleverhause.users.dto.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.io.Serializable;

@Data
@JsonInclude(value = JsonInclude.Include.NON_EMPTY)
public class UserInfoRequest implements Serializable {
    private Long userId;
    private String userName;
}
