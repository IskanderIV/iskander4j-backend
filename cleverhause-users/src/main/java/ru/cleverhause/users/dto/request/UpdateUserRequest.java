package ru.cleverhause.users.dto.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
@Builder
@JsonInclude(value = JsonInclude.Include.NON_EMPTY)
public class UpdateUserRequest implements Serializable {
    @NotNull
    private Long userId;
    private String username;
//    private String username; //googleName
    private String email;
    private String password;
}
