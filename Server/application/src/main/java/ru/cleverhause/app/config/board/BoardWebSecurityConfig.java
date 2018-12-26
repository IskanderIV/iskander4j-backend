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
import ru.cleverhause.filters.BoardBasicPreAuthenticationFilter;
import ru.cleverhause.filters.BoardHttpBasicAuthenticationFilter;
import ru.cleverhause.filters.SecurityFilterChainPostProcessor;
import ru.cleverhause.filters.handler.BoardHttpBasicAuthenticationSuccessHandler;

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
    private static final String ALL_INSIDE = "/**";

    @Bean
    public SecurityFilterChainPostProcessor securityFilterChainPostProcessor() {
        return new SecurityFilterChainPostProcessor();
    }

    @Autowired
    @Qualifier(value = "authManager")
    private AuthenticationManager authenticationManager;

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

    @Bean(name = "boardBasicAuthSuccessHandler")
    public BoardHttpBasicAuthenticationSuccessHandler boardBasicAuthSuccessHandler() {
        return new BoardHttpBasicAuthenticationSuccessHandler();
    }

    @Bean
    public BoardHttpBasicAuthenticationFilter boardBasicAuthFilter() throws Exception {
        BoardHttpBasicAuthenticationFilter boardBasicPreAuthenticationFilter =
                new BoardHttpBasicAuthenticationFilter(BOARDS + ALL_INSIDE);
        boardBasicPreAuthenticationFilter.setAuthenticationManager(authenticationManager);
        boardBasicPreAuthenticationFilter.setAuthenticationSuccessHandler(boardBasicAuthSuccessHandler());
        boardBasicPreAuthenticationFilter.setContinueChainBeforeSuccessfulAuthentication(false);
        boardBasicPreAuthenticationFilter.isNeedCheckBoardBelongsToUser(false);

        return boardBasicPreAuthenticationFilter;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .antMatcher(BOARDS + ALL_INSIDE)
                .authorizeRequests()
                .antMatchers(HttpMethod.POST, BOARDS + ALL_INSIDE).hasAnyRole("USER", "ADMIN")
                .and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        http.addFilterAfter(boardBasicAuthFilter(),
                SecurityContextHolderAwareRequestFilter.class);
    }
}