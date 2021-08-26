package ru.cleverhause.users.dto.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
@Builder
@JsonInclude(value = JsonInclude.Include.NON_EMPTY)
public class AddUserRequest implements Serializable {
    @NotBlank
    private String username;
    private String email;
    @NotBlank
    private String password;
}
