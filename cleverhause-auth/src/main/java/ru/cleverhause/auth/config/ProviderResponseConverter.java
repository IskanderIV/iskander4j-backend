package ru.cleverhause.auth.config;

import org.springframework.security.core.Authentication;
import ru.cleverhause.auth.dto.AuthenticationBaseDto;

public interface ProviderResponseConverter {

    Authentication convert(AuthenticationBaseDto dto);
}
