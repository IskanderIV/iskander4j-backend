package ru.cleverhause.app.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Created by
 *
 * @author Aleksandr_Ivanov1
 * @date 7/9/2018.
 */
@Configuration
@EnableWebMvc
@ComponentScan(basePackages = {"ru.cleverhause.app.rest.board", "ru.cleverhause.app.rest.board"})
//@Import(value = {BoardWebSecurityConfig.class})
public class BoardConfig implements WebMvcConfigurer {
}
