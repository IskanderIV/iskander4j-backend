package ru.cleverhause.app.config;

import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

/**
 * Created by
 *
 * @author Aleksandr_Ivanov1
 * @date 7/9/2018.
 */
@Order(value = 3)
//@Configuration
//@ComponentScan(basePackages = {"ru.cleverhause.app.security", "ru.cleverhause.service"})
//@PropertySource(value = {"classpath:security.properties"})
public class BoardWebSecurityConfig extends WebSecurityConfigurerAdapter {

    private static final String BASE_BOARDS_ADDRESS = "/cleverhause/boards";

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().mvcMatchers(HttpMethod.POST, "/");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests().anyRequest().permitAll();
//                .antMatchers(BASE_BOARDS_ADDRESS + "/board/registration").permitAll()
//                .antMatchers(BASE_BOARDS_ADDRESS).authenticated();
//                .and()
//                .httpBasic();
    }
}
