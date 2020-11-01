package ru.cleverhause.users.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import ru.cleverhause.users.gateway.auth.LoginFeignClient;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Setter
@Component(value = "successOAuth2LoginHandler")
@RequiredArgsConstructor
public class SuccessOAuth2LoginHandler extends SimpleUrlAuthenticationSuccessHandler {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    private final LoginFeignClient authServerClient;

    @Override
    protected void handle(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        String json = MAPPER.writeValueAsString(authServerClient.getToken());
        response.setStatus(HttpStatus.OK.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding(StandardCharsets.UTF_8.toString());
        response.setContentLength(json.length());
        response.getWriter().write(json);
    }
}
