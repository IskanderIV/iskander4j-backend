package ru.cleverhause.auth.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

@Data
@NoArgsConstructor
public class FormLoginAuthenticationResponse implements AuthenticationResponse {
    private UsernamePasswordAuthenticationToken authentication;
}
