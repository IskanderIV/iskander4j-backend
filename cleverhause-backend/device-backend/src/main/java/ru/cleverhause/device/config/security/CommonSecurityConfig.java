package ru.cleverhause.device.config.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Profile;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import ru.cleverhause.device.services.security.DaoUserDetailsService;

import java.util.Collections;

/**
 * Security configuration class
 * Created by
 *
 * @author Aleksandr_Ivanov1
 * @version v.1.0.0
 * @date 3/5/2018.
 */
@Configuration
@Import(value = {DeviceWebSecurityConfig.class})
public class CommonSecurityConfig {

    @Bean
    public PasswordEncoder encoder() {
        return new BCryptPasswordEncoder();
    }

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Bean
    public String getEncodePassword() {
        String str = passwordEncoder.encode("password");
        return str;
    }

    @Profile("dev")
    @Bean
    public UserDetailsService userDetailsService() {
        InMemoryUserDetailsManager manager = new InMemoryUserDetailsManager();
        manager.createUser(User.builder()
                .username("username")
                .password(passwordEncoder.encode("password"))
                .roles("USER")
                .build());
        manager.createUser(User.builder()
                .username("admin")
                .password("admin")
                .roles("USER", "ADMIN")
                .build());
        return manager;
    }

    @Profile("prod")
    @Bean
    public UserDetailsService daoUserDetailsService() {
        return new DaoUserDetailsService();
    }

    @Autowired
    private UserDetailsService userDetailsService;

    @Bean
    public AuthenticationProvider daoAuthenticatedProvider() {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userDetailsService);
        authenticationProvider.setPasswordEncoder(passwordEncoder);
        return authenticationProvider;
    }

//    @Bean
//    public PreAuthenticatedAuthenticationProvider preAuthenticatedAuthenticationProvider() {
//        PreAuthenticatedAuthenticationProvider preAuthenticatedAuthenticationProvider = new PreAuthenticatedAuthenticationProvider();
//        preAuthenticatedAuthenticationProvider.setPreAuthenticatedUserDetailsService(new UserDetailsByNameServiceWrapper<>(userDetailsService));
//
//        return preAuthenticatedAuthenticationProvider;
//    }

    @Bean(name = "authManager")
    public AuthenticationManager authenticationManager() {
        return new ProviderManager(Collections.singletonList(daoAuthenticatedProvider()));
    }
}