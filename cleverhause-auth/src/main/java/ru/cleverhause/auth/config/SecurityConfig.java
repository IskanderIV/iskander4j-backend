package ru.cleverhause.auth.config;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.jackson2.CoreJackson2Module;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.web.access.AccessDeniedHandlerImpl;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.savedrequest.RequestCacheAwareFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.servlet.Filter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    @Qualifier("successHandler")
    private final AuthenticationSuccessHandler successHandler;
    @Qualifier("failureHandler")
    private final AuthenticationFailureHandler failureHandler;

    private final AuthenticationProviderInfoResolver authenticationProviderInfoResolver;

    @Override
    public void configure(WebSecurity web) {
        web.ignoring()
                .antMatchers("/actuator/**");
        web.debug(true);
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http
                .requestMatcher(new AntPathRequestMatcher("/oauth/authorize/**"))
                .csrf().disable()
                .formLogin().disable()
                .httpBasic().disable()
                .addFilterBefore(getAuthenticationFilter(), RequestCacheAwareFilter.class)
                .exceptionHandling().accessDeniedHandler(new AccessDeniedHandlerImpl())
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED);
    }

    @Bean("authenticationFilter")
    public Filter getAuthenticationFilter() throws Exception {
        AbstractAuthenticationProcessingFilter formLoginFilter =
                new AuthenticationFilter("/oauth/authorize", authenticationProviderInfoResolver);
        formLoginFilter.setAuthenticationManager(authenticationManagerBean());
        formLoginFilter.setAuthenticationSuccessHandler(successHandler);
        formLoginFilter.setAuthenticationFailureHandler(failureHandler);
        formLoginFilter.setContinueChainBeforeSuccessfulAuthentication(true);
        formLoginFilter.afterPropertiesSet();
        return formLoginFilter;
    }

    @Bean
    @Primary
    public ObjectMapper objectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModules(new CoreJackson2Module());
        mapper.addMixIn(OAuth2AccessToken.class, OAuth2AccessTokenMixin.class);
        return mapper;
    }

    @JsonTypeInfo(use = JsonTypeInfo.Id.NONE)
    @JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY, getterVisibility = JsonAutoDetect.Visibility.NONE,
            isGetterVisibility = JsonAutoDetect.Visibility.NONE)
    @JsonIgnoreProperties(ignoreUnknown = true)
    abstract static class OAuth2AccessTokenMixin {

        @JsonCreator
        OAuth2AccessTokenMixin(
                @JsonProperty("tokenType") String tokenType,
                @JsonProperty("tokenValue") String tokenValue
//                @JsonProperty("issuedAt") Instant issuedAt,
//                @JsonProperty("expiresAt") Date expiration,
//                @JsonProperty("scopes") Set<String> scopes
        ) {
        }
    }
}
