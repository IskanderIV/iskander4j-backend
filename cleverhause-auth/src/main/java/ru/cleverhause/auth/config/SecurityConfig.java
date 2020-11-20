package ru.cleverhause.auth.config;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.access.AccessDeniedHandlerImpl;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.authentication.AnonymousAuthenticationFilter;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.client.RestTemplate;

import javax.servlet.Filter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableConfigurationProperties(ExistentProvidersMap.class)
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    @Qualifier("successFormLoginHandler")
    private final AuthenticationSuccessHandler successFormLoginHandler;
    @Qualifier("failureFormLoginHandler")
    private final AuthenticationFailureHandler failureFormLoginHandler;

    private final SimpleAuthenticationProviderInfoResolver authProviderUrlResolver;

    @Override
    public void configure(WebSecurity web) {
        web.ignoring()
                .antMatchers("/actuator/**");
        web.debug(true);
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http
                .requestMatcher(new AntPathRequestMatcher("/oauth/token", HttpMethod.POST.name()))
                .csrf().disable()
                .formLogin().disable()
                .httpBasic().disable()
                .addFilterBefore(getAuthenticationFilter(), AnonymousAuthenticationFilter.class)
                .exceptionHandling().accessDeniedHandler(new AccessDeniedHandlerImpl())
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.NEVER);
    }

    private Filter getAuthenticationFilter() throws Exception {
        AbstractAuthenticationProcessingFilter formLoginFilter = new AuthenticationFilter("/oauth/token", );
        formLoginFilter.setAuthenticationManager(authenticationManagerBean());
        formLoginFilter.setAuthenticationSuccessHandler(successFormLoginHandler);
        formLoginFilter.setAuthenticationFailureHandler(failureFormLoginHandler);
        formLoginFilter.setFilterProcessesUrl("/login");
        formLoginFilter.afterPropertiesSet();
        return formLoginFilter;
    }

    @Bean
    public RestTemplate restTemplate() {
        ClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
        RestTemplate template = new RestTemplate();
    }
}
