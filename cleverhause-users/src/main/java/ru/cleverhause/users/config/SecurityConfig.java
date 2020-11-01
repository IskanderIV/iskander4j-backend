package ru.cleverhause.users.config;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.oauth2.client.web.OAuth2LoginAuthenticationFilter;
import org.springframework.security.web.access.AccessDeniedHandlerImpl;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.OrRequestMatcher;

import javax.servlet.Filter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    @Qualifier("successOAuth2LoginHandler")
    private final AuthenticationSuccessHandler successOAuth2LoginHandler;
    @Qualifier("failureOAuth2LoginHandler")
    private final AuthenticationFailureHandler failureOAuth2LoginHandler;
    @Qualifier("successFormLoginHandler")
    private final AuthenticationSuccessHandler successFormLoginHandler;
    @Qualifier("failureFormLoginHandler")
    private final AuthenticationFailureHandler failureFormLoginHandler;

    @Override
    public void configure(WebSecurity web) {
        web.ignoring()
                .antMatchers("/actuator/**")
                .antMatchers("/api/users/**");
        web.debug(true);
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http
                .requestMatcher(new OrRequestMatcher(
                        new AntPathRequestMatcher("/login/**", HttpMethod.POST.name()),
                        new AntPathRequestMatcher("/login/oauth2/code/**", HttpMethod.GET.name()),
                        new AntPathRequestMatcher("/oauth2/authorization/**", HttpMethod.GET.name())))
                .csrf().disable()
                .formLogin().disable()
                .httpBasic().disable()
                .oauth2Login()
                    .successHandler(successOAuth2LoginHandler)
                    .failureHandler(failureOAuth2LoginHandler)
                .and()
                .addFilterAfter(getFormLoginAuthenticationFilter(), OAuth2LoginAuthenticationFilter.class)
                .exceptionHandling().accessDeniedHandler(new AccessDeniedHandlerImpl())
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.NEVER);
    }

    private Filter getFormLoginAuthenticationFilter() throws Exception {
        AbstractAuthenticationProcessingFilter formLoginFilter = new UsernamePasswordAuthenticationFilter();
        formLoginFilter.setAuthenticationManager(authenticationManagerBean());
        formLoginFilter.setAuthenticationSuccessHandler(successFormLoginHandler);
        formLoginFilter.setAuthenticationFailureHandler(failureFormLoginHandler);
        formLoginFilter.setFilterProcessesUrl("/login");
        formLoginFilter.afterPropertiesSet();
        return formLoginFilter;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.inMemoryAuthentication()
                .passwordEncoder(NoOpPasswordEncoder.getInstance())
                .withUser("username").password("{noop}password").authorities("ROLE_USER")
                .and()
                .withUser("admin").password("{noop}admin").authorities("ROLE_ADMIN");
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManager() throws Exception {
        return super.authenticationManager();
    }

    @Bean
    @Primary
    @Override
    public UserDetailsService userDetailsServiceBean() throws Exception {
        return super.userDetailsServiceBean();
    }
}
