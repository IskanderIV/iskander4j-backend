package ru.cleverhause.app.filters;

import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.web.util.ContentCachingRequestWrapper;
import ru.cleverhause.app.dto.DeviceData;
import ru.cleverhause.app.dto.request.BoardRequestBody;
import ru.cleverhause.util.JsonUtil;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by
 *
 * @author Aleksandr_Ivanov1
 * @date 8/1/2018.
 */
public class BoardHttpBasicAuthenticationFilter extends AbstractAuthenticationProcessingFilter {
    private BoardRequestBody<DeviceData> body;

    public BoardHttpBasicAuthenticationFilter(String defaultFilterProcessesUrl) {
        super(defaultFilterProcessesUrl);
    }

    public BoardHttpBasicAuthenticationFilter(RequestMatcher requiresAuthenticationRequestMatcher) {
        super(requiresAuthenticationRequestMatcher);
    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
        ContentCachingRequestWrapper requestWrapper = new ContentCachingRequestWrapper((HttpServletRequest) req);
        super.doFilter(requestWrapper, res, chain);
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        super.successfulAuthentication(request, response, chain, authResult);
        chain.doFilter(request, response);
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException, IOException, ServletException {
        Authentication authResult = null;
        String tokens[] = getTokens(request);
        UsernamePasswordAuthenticationToken authRequest = new UsernamePasswordAuthenticationToken(tokens[0], tokens[1]);
        if (getAuthenticationManager() != null) {
            authResult = this.getAuthenticationManager().authenticate(authRequest);
        } else {
            throw new RuntimeException("No AuthenticationManager found for authenticating requests from board");
        }

        return authResult;
    }

    private String[] getTokens(HttpServletRequest request) {
        String[] tokens = null;
        if (retrieveBodyFromPostRequest(request)) {
            tokens = this.extractCreds();
        }

        return tokens;
    }

    private boolean retrieveBodyFromPostRequest(HttpServletRequest request) {
        if (HttpMethod.POST.matches(request.getMethod())) {
            try {
                this.body = JsonUtil.fromInputStreamToBoardData(request.getInputStream());
                request.getInputStream().close();
                System.out.println(body.toString());
                this.body = JsonUtil.fromInputStreamToBoardData(request.getInputStream());
                System.out.println(body.toString());
                this.body = JsonUtil.fromInputStreamToBoardData(request.getInputStream());
                System.out.println(body.toString());
            } catch (Exception e) {
                System.out.println("MyFilter. Can't convert request input stream to json"); //TODO
            }
        }

        return body != null;
    }

    private String[] extractCreds() {
        String username = this.body.getUsername();
        String password = this.body.getPassword();

        return new String[]{username, password};
    }
}
