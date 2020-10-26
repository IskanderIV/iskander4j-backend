package ru.cleverhause.auth.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.context.request.async.WebAsyncManagerIntegrationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.AnyRequestMatcher;
import org.springframework.security.web.util.matcher.OrRequestMatcher;

@Configuration
@EnableWebSecurity
@Order(-1)
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    @Qualifier("successOAuth2LoginHandler")
    private final AuthenticationSuccessHandler successOAuth2LoginHandler;
    @Qualifier("failureOAuth2LoginHandler")
    private final AuthenticationFailureHandler failureOAuth2LoginHandler;
    private final OAuth2SuccessLoginSessionAuthnStrategy oAuth2SuccessLoginSessionAuthnStrategy;

    public SecurityConfig(AuthenticationSuccessHandler successOAuth2LoginHandler,
                          AuthenticationFailureHandler failureOAuth2LoginHandler,
                          OAuth2SuccessLoginSessionAuthnStrategy oAuth2SuccessLoginSessionAuthnStrategy) {
        super(true);
        this.successOAuth2LoginHandler = successOAuth2LoginHandler;
        this.failureOAuth2LoginHandler = failureOAuth2LoginHandler;
        this.oAuth2SuccessLoginSessionAuthnStrategy = oAuth2SuccessLoginSessionAuthnStrategy;
    }

    @Override
    public void configure(WebSecurity web) {
        web.ignoring()
                .antMatchers("/oauth/test")
                .antMatchers("/actuator/**");
        web.debug(true);
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http
                .requestMatcher(new OrRequestMatcher(new AntPathRequestMatcher("/oauth/token"), AnyRequestMatcher.INSTANCE))
                .csrf().disable()
                .addFilter(new WebAsyncManagerIntegrationFilter())
                .exceptionHandling().and()
                .headers().and()
                .securityContext().and()
                .requestCache().and()
                .anonymous().and()
                .servletApi().and()
                .authorizeRequests()
                .antMatchers("/oauth/token/**").permitAll()
                .anyRequest().authenticated()
                .and()
                .oauth2Login()
                .successHandler(successOAuth2LoginHandler)
                .failureHandler(failureOAuth2LoginHandler)
                .and()
                .logout().and()
                .sessionManagement()
                .sessionAuthenticationStrategy(oAuth2SuccessLoginSessionAuthnStrategy)
                .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED);
//                .tokenEndpoint()
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.inMemoryAuthentication()
                .withUser("username").password("{noop}password").authorities("ROLE_USER")
                .and()
                .withUser("admin").password("{noop}admin").authorities("ROLE_ADMIN");
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Bean
    @Primary
    @Override
    public UserDetailsService userDetailsServiceBean() throws Exception {
        return super.userDetailsServiceBean();
    }
}
