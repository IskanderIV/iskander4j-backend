package ru.cleverhause.auth.config;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component(value = "successFormLoginHandler")
public class SuccessFormLoginHandler implements AuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        new DefaultRedirectStrategy().sendRedirect(request, response, "http://localhost:8080/oauth/authorize?response_type=code&state=state&client_id=client&scope=read&redirect_uri=http://localhost:8080/oauth/authorize");
    }
}