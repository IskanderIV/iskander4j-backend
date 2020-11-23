package ru.cleverhause.provider.formlogin.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.RequestMatcher;

import javax.servlet.ServletRequestWrapper;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;

@Slf4j
public class FormLoginAuthenticationFilter extends AbstractAuthenticationProcessingFilter {

    private final ObjectMapper mapper;

    public FormLoginAuthenticationFilter(RequestMatcher requiresAuthenticationRequestMatcher, ObjectMapper mapper) {
        super(requiresAuthenticationRequestMatcher);
        this.mapper = mapper;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException, IOException {
        InputStream is = new ServletRequestWrapper(request).getInputStream();
        Authentication authRequest = mapper.readValue(is, UsernamePasswordAuthenticationToken.class);
        return this.getAuthenticationManager().authenticate(authRequest);
    }
}
