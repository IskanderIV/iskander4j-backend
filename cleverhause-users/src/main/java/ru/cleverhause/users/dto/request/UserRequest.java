package ru.cleverhause.users.dto.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
@JsonInclude(value = JsonInclude.Include.NON_EMPTY)
public class UserRequest implements Serializable {
    @NotNull
    private Long userId;
    @NotBlank
    private String username;
//    private String username; //googleName
    private String email;
    private String password;
}
