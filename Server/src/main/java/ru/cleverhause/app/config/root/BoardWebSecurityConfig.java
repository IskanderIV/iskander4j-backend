package ru.cleverhause.app.config.root;

import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.authentication.www.BasicAuthenticationEntryPoint;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

/**
 * Created by
 *
 * @author Aleksandr_Ivanov1
 * @date 7/9/2018.
 */
//@Order(value = 1)
//@Configuration
//@EnableWebSecurity
//@ComponentScan(basePackages = {"ru.cleverhause.service.board"})
//@PropertySource(value = {"classpath:security.properties"})
public class BoardWebSecurityConfig extends WebSecurityConfigurerAdapter {

    private static final String CONTEXT_ADDRESS = "/cleverhause";
    private static final String BASE_BOARDS_ADDRESS = "/boards";
    private static final String TEST_BOARD_ADDRESS = "/board*";
    private static final String ALL_IN_ADDRESS = "/";
//
//    @Autowired
//    private UserDetailsService userDetailsService;
//
//    @Bean
//    public PasswordEncoder encoder() {
//        return new BCryptPasswordEncoder();
//    }
//
//    @Bean
//    public AuthenticationProvider daoAuthenticatedProvider() {
//        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
//        authenticationProvider.setUserDetailsService(userDetailsService);
//        authenticationProvider.setPasswordEncoder(encoder());
//        return authenticationProvider;
//    }
//
//    @Bean(name = "boardAuthManager")
//    public AuthenticationManager authenticationManager() {
//        ProviderManager providerManager = new ProviderManager(Arrays.asList(daoAuthenticatedProvider()));
//        return providerManager;
//    }

    @Bean(name = "tempUserDetailsService")
    public UserDetailsService userDetailsService() {
        // ensure the passwords are encoded properly
        User.UserBuilder users = User.withDefaultPasswordEncoder();
        InMemoryUserDetailsManager manager = new InMemoryUserDetailsManager();
        manager.createUser(users.username("user").password("password").roles("USER").build());
        manager.createUser(users.username("admin").password("password").roles("USER", "ADMIN").build());
        return manager;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth)
            throws Exception {
        auth.userDetailsService(userDetailsService());
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Bean
    public BasicAuthenticationEntryPoint basicAuthEntryPoint() {
        BasicAuthenticationEntryPoint entryPoint = new BasicAuthenticationEntryPoint();
        entryPoint.setRealmName("Board_REST_Realm");

        return entryPoint;
    }

//    @Override
//    public void configure(WebSecurity web) throws Exception {
////        web.;
//    }

    @Bean
    public BasicAuthenticationFilter myAuthFilter() throws Exception {
        return new BasicAuthenticationFilter(authenticationManagerBean(), basicAuthEntryPoint());
    }

    @Override

    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .authorizeRequests()
                .antMatchers(CONTEXT_ADDRESS + BASE_BOARDS_ADDRESS + ALL_IN_ADDRESS).authenticated()
                .and()
                .httpBasic()
                .authenticationEntryPoint(basicAuthEntryPoint())
                .and()
                .formLogin()
        ;

        http.addFilterAfter(myAuthFilter(),
                BasicAuthenticationFilter.class);
    }
}
