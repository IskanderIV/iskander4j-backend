package ru.cleverhause.app.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.session.SessionAuthenticationStrategy;
import org.springframework.security.web.authentication.session.SessionFixationProtectionStrategy;

/**
 * Created by
 *
 * @author Aleksandr_Ivanov1
 * @date 7/9/2018.
 */
@Order(value = 2)
//@Configuration
//@ComponentScan(basePackages = {"ru.cleverhause.app.security", "ru.cleverhause.service"})
//@PropertySource(value = {"classpath:security.properties"})
public class FrontWebSecurityConfig extends WebSecurityConfigurerAdapter {

    private static final String POST = "POST";
    private static final String COOKIES_JSESSIONID = "JSESSIONID";
    private static final String BASE_ADDRESS = "/cleverhause/site";

    @Value("${security.loginPage}")
    private String loginPage;
    @Value("${security.loginErrorPage}")
    private String loginErrorPage;
    @Value("${security.loginErrorPage}")
    private String logoutPage;

    @Value("${security.param.username}")
    private String usernameParam;
    @Value("${security.param.password}")
    private String passwordParam;

    @Value("${security.role.admin}")
    private String adminRole;
    @Value("${security.role.user}")
    private String userRole;
    @Value("${security.role.anonimous}")
    private String anonimousRole;


    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().mvcMatchers(HttpMethod.POST, "/");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests().anyRequest().permitAll();
//                .antMatchers(BASE_ADDRESS + "/home").permitAll();
//                .antMatchers(BASE_ADDRESS + "/admin").hasRole(adminRole)
//                //                .antMatchers("/mobile").authenticated()
//                //                .and()
//                //                .exceptionHandling()
//                //                .authenticationEntryPoint(authenticationEntryPoint)
//                .and()
//                .formLogin()
//                .loginPage(loginPage)
//                .defaultSuccessUrl(BASE_ADDRESS + "/home")
//                .failureUrl(BASE_ADDRESS + "/login?error")
//                .usernameParameter(usernameParam)
//                .passwordParameter(passwordParam)
//                //                .successHandler(authSuccessHandler)
//                //                .failureHandler(authFailureHandler)
//                .and()
//                .logout()
//                //                .logoutSuccessHandler(logoutSuccessHandler)
//                .logoutUrl(logoutPage)
//                .invalidateHttpSession(true)
//                .deleteCookies(COOKIES_JSESSIONID)
//                .logoutRequestMatcher(new AntPathRequestMatcher(logoutPage, POST));

        //        http.sessionManagement()
        //                .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
        //                .sessionAuthenticationStrategy(sas())
        //                .invalidSessionStrategy(new SimpleRedirectInvalidSessionStrategy("/pages/home.jsp"))
        //                .sessionAuthenticationErrorUrl("/pages/home.jsp")
        //                .maximumSessions(1);
    }

    @Bean
    public SessionAuthenticationStrategy sas() {
        return new SessionFixationProtectionStrategy();
    }

    @Bean
    public static PropertyPlaceholderConfigurer placeHolderConfigurer() {
        return new PropertyPlaceholderConfigurer();
    }
}
