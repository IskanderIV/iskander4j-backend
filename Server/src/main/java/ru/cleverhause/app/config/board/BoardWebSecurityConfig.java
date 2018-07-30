package ru.cleverhause.app.config.board;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.authentication.www.BasicAuthenticationEntryPoint;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

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

    @Bean(name = "tempUserDetailsService")
    public UserDetailsService userDetailsService() {
        // ensure the passwords are encoded properly
        User.UserBuilder users = User.withDefaultPasswordEncoder();
        InMemoryUserDetailsManager manager = new InMemoryUserDetailsManager();
        manager.createUser(users.username("user").password("password").roles("USER").build());
        manager.createUser(users.username("admin").password("password").roles("USER", "ADMIN").build());
        return manager;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth)
            throws Exception {
        auth.userDetailsService(userDetailsService());
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Bean
    public BasicAuthenticationEntryPoint basicAuthEntryPoint() {
        BasicAuthenticationEntryPoint entryPoint = new BasicAuthenticationEntryPoint();
        entryPoint.setRealmName("Board_REST_Realm");

        return entryPoint;
    }

    @Bean
    public BasicAuthenticationFilter myAuthFilter() throws Exception {
        return new BasicAuthenticationFilter(authenticationManagerBean(), basicAuthEntryPoint());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable().antMatcher("/boards/**")
                .authorizeRequests()
                .antMatchers(BOARDS + BOARD + ALL_INSIDE).authenticated()
                .and()
                .httpBasic()
                .and()
                .formLogin();

        http.addFilterAfter(myAuthFilter(),
                BasicAuthenticationFilter.class);
    }
}
