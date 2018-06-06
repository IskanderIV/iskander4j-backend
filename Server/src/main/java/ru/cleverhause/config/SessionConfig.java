package ru.cleverhause.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackages = {"ru.cleverhause.session"})
public class SessionConfig {
//    @Override
//    protected void configure(HttpSecurity http) throws Exception {
//        http.sessionManagement()
//                .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
//                .sessionAuthenticationStrategy(sas())
//                .invalidSessionStrategy(new SimpleRedirectInvalidSessionStrategy("/pages/home.jsp"))
//                .sessionAuthenticationErrorUrl("/pages/home.jsp")
//                .maximumSessions(1);
//    }
//
//    @Bean
//    public SessionAuthenticationStrategy sas() {
//        return new SessionFixationProtectionStrategy();
//    }
}
