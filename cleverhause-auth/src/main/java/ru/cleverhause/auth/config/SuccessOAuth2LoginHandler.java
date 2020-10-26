package ru.cleverhause.auth.config;

import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.context.ServletContextAware;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static org.springframework.security.oauth2.core.AuthorizationGrantType.CLIENT_CREDENTIALS;

@Setter
@Component(value = "successOAuth2LoginHandler")
@RequiredArgsConstructor
public class SuccessOAuth2LoginHandler extends SimpleUrlAuthenticationSuccessHandler implements ServletContextAware {

    private ServletContext servletContext;
    @Value("${spring.security.oauth2.client.redirectUrl}")
    private String redirectUrl;

    @Override
    protected void handle(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        SecurityContextHolder.getContext().setAuthentication(authentication);
//        RequestDispatcher dispatcherServlet = servletContext.getRequestDispatcher("/oauth/token");
//        dispatcherServlet.forward(request, response);
        String enrichedRedirectUri = redirectUrl + "?grant_type=" + CLIENT_CREDENTIALS.getValue();
        setDefaultTargetUrl(enrichedRedirectUri);
//        HttpSession session = request.getSession(false);
//        session.setAttribute(SPRING_SECURITY_CONTEXT_KEY, SecurityContextHolder.getContext());
//        response.setStatus(HttpStatus.TEMPORARY_REDIRECT.value());
//        enrichedRedirectUri = response.encodeRedirectURL(enrichedRedirectUri);
//        response.sendRedirect(enrichedRedirectUri);
        super.handle(request, response, authentication);
    }

    @Override
    public void setServletContext(ServletContext servletContext) {
        this.servletContext = servletContext;
    }
}
