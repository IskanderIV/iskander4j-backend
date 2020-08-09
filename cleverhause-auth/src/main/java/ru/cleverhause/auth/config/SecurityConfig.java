package ru.cleverhause.auth.config;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Primary;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableOAuth2Client;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

@Configuration
@EnableWebSecurity
//@EnableOAuth2Client
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    @Qualifier("successFormLoginHandler")
    private final AuthenticationSuccessHandler successFormLoginHandler;
    @Qualifier("failureFormLoginHandler")
    private final AuthenticationFailureHandler failureFormLoginHandler;
    @Qualifier("successOAuth2LoginHandler")
    private final AuthenticationSuccessHandler successOAuth2LoginHandler;
    @Qualifier("failureOAuth2LoginHandler")
    private final AuthenticationFailureHandler failureOAuth2LoginHandler;

//    public final OAuth2ClientConfig.OAuth2AuthorizationCodeAccessTokenResponseClient client;

    @Override
    public void configure(WebSecurity web) {
        web.ignoring().antMatchers("/oauth/test");
        web.debug(true);
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .authorizeRequests()
                .antMatchers("/oauth/token/**").permitAll()
                .anyRequest().authenticated()
                .and()
//                .formLogin()
//                .permitAll()
//                .successHandler(successFormLoginHandler)
//                .failureHandler(failureFormLoginHandler)
//                .and()
//                .logout().permitAll()
//                .and()
//                .httpBasic()
//                .and()
                .oauth2Login()
//                .defaultSuccessUrl("http://cleverhause.ru/oauth/token")
                .successHandler(successOAuth2LoginHandler)
                .failureHandler(failureOAuth2LoginHandler);
//                .tokenEndpoint()
//                .accessTokenResponseClient(client);
//                .oauth2Client();
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
