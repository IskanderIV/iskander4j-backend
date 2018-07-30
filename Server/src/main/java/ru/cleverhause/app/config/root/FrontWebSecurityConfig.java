package ru.cleverhause.app.config.root;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import java.util.Collections;

/**
 * Created by
 *
 * @author Aleksandr_Ivanov1
 * @date 7/9/2018.
 */
//@Order(value = 200)
//@Configuration
//@EnableWebSecurity
//@ComponentScan(basePackages = {"ru.cleverhause.service.security"})
//@PropertySource(value = {"classpath:security.properties"})
public class FrontWebSecurityConfig extends WebSecurityConfigurerAdapter {

    private static final String POST = "POST";
    private static final String COOKIES_JSESSIONID = "JSESSIONID";
    private static final String BASE_ADDRESS = "/cleverhause/site/";

    @Autowired
    @Qualifier(value = "daoUserDetailsService")
    private UserDetailsService userDetailsService;

    @Bean
    public PasswordEncoder encoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationProvider daoAuthenticatedProvider() {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userDetailsService);
        authenticationProvider.setPasswordEncoder(encoder());
        return authenticationProvider;
    }

    @Bean(name = "frontAuthManager")
    public AuthenticationManager authenticationManager() {
        ProviderManager providerManager = new ProviderManager(Collections.singletonList(daoAuthenticatedProvider()));
        return providerManager;
    }

    @Bean
    public BasicAuthenticationFilter myAuthFilter2() throws Exception {
        return new BasicAuthenticationFilter(authenticationManagerBean());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .authorizeRequests()
                .antMatchers("/").authenticated()
                .and()
                .formLogin();

        http.addFilterAfter(myAuthFilter2(),
                BasicAuthenticationFilter.class);
//                .antMatchers(BASE_ADDRESS + "/home").permitAll();
//                .antMatchers(BASE_ADDRESS + "/admin").hasRole(adminRole)
//                //                .antMatchers("/mobile").authenticated()
//                //                .and()
//                //                .exceptionHandling()
//                //                .authenticationEntryPoint(authenticationEntryPoint)
//                .and()
//                .formLogin();
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

//    @Bean
//    public SessionAuthenticationStrategy sas() {
//        return new SessionFixationProtectionStrategy();
//    }

//    @Bean
//    public static PropertyPlaceholderConfigurer placeHolderConfigurer() {
//        return new PropertyPlaceholderConfigurer();
//    }
}
