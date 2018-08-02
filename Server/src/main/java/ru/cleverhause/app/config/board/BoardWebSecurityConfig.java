package ru.cleverhause.app.config.board;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.www.BasicAuthenticationEntryPoint;
import org.springframework.security.web.servletapi.SecurityContextHolderAwareRequestFilter;
import ru.cleverhause.app.filters.BoardBasicPreAuthenticationFilter;
import ru.cleverhause.app.filters.BoardHttpBasicAuthenticationFilter;
import ru.cleverhause.app.filters.SecurityFilterChainPostProcessor;
import ru.cleverhause.app.filters.handler.BoardHttpBasicAuthenticationSuccessHandler;
import ru.cleverhause.service.board.BoardDataService;

/**
 * Created by
 *
 * @author Aleksandr_Ivanov1
 * @date 7/9/2018.
 */
@Configuration
@EnableWebSecurity
public class BoardWebSecurityConfig extends WebSecurityConfigurerAdapter {

    private static final String BOARDS = "/boards";
    private static final String BOARD = "/board";
    private static final String ALL_INSIDE = "/**";

    @Bean
    public SecurityFilterChainPostProcessor securityFilterChainPostProcessor() {
        return new SecurityFilterChainPostProcessor();
    }

    @Autowired
    @Qualifier(value = "authManager")
    public AuthenticationManager authenticationManager;

    @Autowired
    public BoardDataService boardDataService;

    @Bean
    public BasicAuthenticationEntryPoint basicAuthEntryPoint() {
        BasicAuthenticationEntryPoint entryPoint = new BasicAuthenticationEntryPoint();
        entryPoint.setRealmName("Board_REST_Realm");

        return entryPoint;
    }

    // TODO back to it when will do a token authentication
    @Bean
    public BoardBasicPreAuthenticationFilter boardPreAuthFilter() throws Exception {
        BoardBasicPreAuthenticationFilter boardBasicPreAuthenticationFilter =
                new BoardBasicPreAuthenticationFilter();
        boardBasicPreAuthenticationFilter.setAuthenticationManager(authenticationManager);

        return boardBasicPreAuthenticationFilter;
    }

    @Bean
    public BoardHttpBasicAuthenticationSuccessHandler boardBasicAuthSuccessHandler() {
        return new BoardHttpBasicAuthenticationSuccessHandler();
    }

    @Bean
    public BoardHttpBasicAuthenticationFilter boardBasicAuthFilter() throws Exception {
        BoardHttpBasicAuthenticationFilter boardBasicPreAuthenticationFilter =
                new BoardHttpBasicAuthenticationFilter("/boards/**");
        boardBasicPreAuthenticationFilter.setAuthenticationManager(authenticationManager);
        boardBasicPreAuthenticationFilter.setAuthenticationSuccessHandler(boardBasicAuthSuccessHandler());
        boardBasicPreAuthenticationFilter.setContinueChainBeforeSuccessfulAuthentication(false);
        boardBasicPreAuthenticationFilter.isNeedCheckBoardBelongsToUser(false);
        boardBasicPreAuthenticationFilter.setBoardDataService(boardDataService);

        return boardBasicPreAuthenticationFilter;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .antMatcher("/boards/**")
                .authorizeRequests()
                .antMatchers(HttpMethod.POST, BOARDS + ALL_INSIDE).authenticated()
                .and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
//                .and()
//                .httpBasic().authenticationEntryPoint(basicAuthEntryPoint());

        http.addFilterAfter(boardBasicAuthFilter(),
                SecurityContextHolderAwareRequestFilter.class);
    }
}
