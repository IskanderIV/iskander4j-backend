package ru.cleverhause.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.authentication.dao.ReflectionSaltSource;
import org.springframework.security.authentication.encoding.Md5PasswordEncoder;
import org.springframework.security.config.BeanIds;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.session.SessionAuthenticationStrategy;
import org.springframework.security.web.authentication.session.SessionFixationProtectionStrategy;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

/**
 * Security configuration class
 * Created by
 *
 * @author Aleksandr_Ivanov1
 * @version v.1.0.0
 * @date 3/5/2018.
 */

@EnableWebSecurity(debug = true)
@Configuration
@ComponentScan(basePackages = {"ru.cleverhause.security", "ru.cleverhause.service"})
@PropertySource(value = {"classpath:security.properties"})
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private static final String POST = "POST";
    private static final String COOKIES_JSESSIONID = "JSESSIONID";

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

    @Autowired
    private UserDetailsService userDetailsService;

    @Bean
    public Md5PasswordEncoder encoder() {
        Md5PasswordEncoder md5PasswordEncoder = new Md5PasswordEncoder();
        return md5PasswordEncoder;
    }

    @Bean
    public ReflectionSaltSource saltSource() {
        ReflectionSaltSource saltSource = new ReflectionSaltSource();
        saltSource.setUserPropertyToUse(usernameParam);
        return saltSource;
    }

    @Bean
    public AuthenticationProvider daoAuthenticatedProvider() {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userDetailsService);
        authenticationProvider.setPasswordEncoder(encoder());
        authenticationProvider.setSaltSource(saltSource());
        return authenticationProvider;
    }

    @Override
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(daoAuthenticatedProvider());
    }

    @Bean(name = BeanIds.AUTHENTICATION_MANAGER)
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
//                .antMatchers("/", "/home").hasAnyRole(adminRole, userRole, anonimousRole)
                .antMatchers("/admin").hasRole(adminRole)
//                .antMatchers("/admin").authenticated()
                .antMatchers("/arduino").authenticated()
                .anyRequest().permitAll()
//                .antMatchers("/mobile").authenticated()
//                .and()
//                .exceptionHandling()
//                .authenticationEntryPoint(authenticationEntryPoint)
                .and()
                .formLogin()
                .loginPage(loginPage)
                .defaultSuccessUrl("/home")
                .failureUrl("/login?error")
                .usernameParameter(usernameParam)
                .passwordParameter(passwordParam)
//                .successHandler(authSuccessHandler)
//                .failureHandler(authFailureHandler)
                .and()
                .logout()
//                .logoutSuccessHandler(logoutSuccessHandler)
                .logoutUrl(logoutPage)
                .invalidateHttpSession(true)
                .deleteCookies(COOKIES_JSESSIONID)
                .logoutRequestMatcher(new AntPathRequestMatcher(logoutPage, POST));

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
    public static PropertySourcesPlaceholderConfigurer propertyConfig() {
        return new PropertySourcesPlaceholderConfigurer();
    }
}
