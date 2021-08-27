package ru.cleverhause.users.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import ru.cleverhause.users.gateway.auth.LoginFeignClient;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Component(value = "successFormLoginHandler")
@RequiredArgsConstructor
public class SuccessFormLoginHandler extends SimpleUrlAuthenticationSuccessHandler {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    private final LoginFeignClient authServerClient;

    @Override
    protected void handle(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        String json = MAPPER.writeValueAsString(authServerClient.getToken());
        response.setStatus(HttpStatus.OK.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding(StandardCharsets.UTF_8.toString());
        response.setContentLength(json.length());
        response.getWriter().write(json);
    }
}
