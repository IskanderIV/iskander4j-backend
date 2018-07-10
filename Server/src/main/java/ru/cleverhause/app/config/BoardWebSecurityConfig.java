package ru.cleverhause.app.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.www.BasicAuthenticationEntryPoint;

/**
 * Created by
 *
 * @author Aleksandr_Ivanov1
 * @date 7/9/2018.
 */
@Order(value = 300)
@Configuration
@EnableWebSecurity
//@PropertySource(value = {"classpath:security.properties"})
public class BoardWebSecurityConfig extends WebSecurityConfigurerAdapter {

    private static final String BASE_BOARDS_ADDRESS = "/boards";

    @Bean
    public BasicAuthenticationEntryPoint basicAuthEntryPoint() {
        BasicAuthenticationEntryPoint entryPoint = new BasicAuthenticationEntryPoint();
        entryPoint.setRealmName("REST Realm");

        return entryPoint;
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
//        web.;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers(HttpMethod.POST, BASE_BOARDS_ADDRESS).authenticated()
                .antMatchers(HttpMethod.GET, BASE_BOARDS_ADDRESS).permitAll()
                .and().httpBasic().authenticationEntryPoint(basicAuthEntryPoint());
    }
}
