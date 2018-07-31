package ru.cleverhause.app.config.board;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.www.BasicAuthenticationEntryPoint;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import ru.cleverhause.app.filters.BoardBasicPreAuthenticationFilter;

/**
 * Created by
 *
 * @author Aleksandr_Ivanov1
 * @date 7/9/2018.
 */
@Configuration
@EnableWebSecurity
public class BoardWebSecurityConfig extends WebSecurityConfigurerAdapter {

    private static final String CONTEXT = "/cleverhause";
    private static final String BOARDS = "/boards";
    private static final String BOARD = "/board";
    private static final String ALL_INSIDE = "/**";

    @Autowired
    @Qualifier(value = "authManager")
    public AuthenticationManager authenticationManager;

    @Bean
    public BasicAuthenticationEntryPoint basicAuthEntryPoint() {
        BasicAuthenticationEntryPoint entryPoint = new BasicAuthenticationEntryPoint();
        entryPoint.setRealmName("Board_REST_Realm");

        return entryPoint;
    }

    @Bean
    public BoardBasicPreAuthenticationFilter boardPreAuthFilter() throws Exception {
        BoardBasicPreAuthenticationFilter boardBasicPreAuthenticationFilter =
                new BoardBasicPreAuthenticationFilter();
        boardBasicPreAuthenticationFilter.setAuthenticationManager(authenticationManager);

        return boardBasicPreAuthenticationFilter;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .antMatcher("/boards/**")
                .authorizeRequests()
                .antMatchers(BOARDS + BOARD).authenticated()
                .and()
                .httpBasic().authenticationEntryPoint(basicAuthEntryPoint());

        http.addFilterBefore(boardPreAuthFilter(),
                BasicAuthenticationFilter.class);
    }
}
