package ru.cleverhause.auth.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class FormLoginAuthenticationRequest implements AuthenticationRequest {
    private String username;
    private String password;
}
