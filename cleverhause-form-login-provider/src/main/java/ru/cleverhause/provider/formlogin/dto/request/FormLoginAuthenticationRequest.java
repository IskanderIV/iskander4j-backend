package ru.cleverhause.provider.formlogin.dto.request;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class FormLoginAuthenticationRequest implements AuthenticationRequest {
    private String username;
    private String password;
}
