package ru.cleverhause.provider.formlogin.dto.response;

import lombok.Data;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

@Data
public class FormLoginAuthenticationResponse implements AuthenticationResponse {
    private UsernamePasswordAuthenticationToken authentication;
}
