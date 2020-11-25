package ru.cleverhause.provider.formlogin.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.jackson2.CoreJackson2Module;
import org.springframework.security.web.access.AccessDeniedHandlerImpl;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.authentication.AnonymousAuthenticationFilter;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.util.matcher.AndRequestMatcher;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.MediaTypeRequestMatcher;
import ru.cleverhause.provider.formlogin.service.FormLoginUserDetailsService;

import javax.servlet.Filter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableConfigurationProperties
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final FormLoginUserDetailsService formLoginUserDetailsService;

    @Override
    public void configure(WebSecurity web) {
        web.ignoring()
                .antMatchers("/actuator/**");
        web.debug(true);
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http
                .requestMatcher(new AndRequestMatcher(
                        new AntPathRequestMatcher("/login", HttpMethod.POST.name()),
                        new MediaTypeRequestMatcher(MediaType.APPLICATION_JSON)))
                .csrf().disable()
                .formLogin().disable()
                .httpBasic().disable()
                .addFilterBefore(getFormLoginAuthenticationFilter(), AnonymousAuthenticationFilter.class)
                .exceptionHandling().accessDeniedHandler(new AccessDeniedHandlerImpl())
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.NEVER);
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(formLoginUserDetailsService);
    }

    @Bean
    @Override
    protected AuthenticationManager authenticationManager() throws Exception {
        return super.authenticationManager();
    }

    private Filter getFormLoginAuthenticationFilter() throws Exception {
        AbstractAuthenticationProcessingFilter formLoginFilter =
                new FormLoginAuthenticationFilter(new AntPathRequestMatcher("/login", HttpMethod.GET.name()), objectMapper());
        formLoginFilter.setAuthenticationManager(authenticationManager());
        formLoginFilter.setAuthenticationSuccessHandler(successHandler());
        formLoginFilter.setAuthenticationFailureHandler(failureHandler());
        formLoginFilter.afterPropertiesSet();
        return formLoginFilter;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return NoOpPasswordEncoder.getInstance();
    }

    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new CoreJackson2Module());
        return mapper;
    }

    @Bean
    public AuthenticationSuccessHandler successHandler() {
        return new SuccessFormLoginHandler(objectMapper());
    }

    @Bean
    public AuthenticationFailureHandler failureHandler() {
        return new FailureFormLoginHandler(objectMapper());
    }
}
