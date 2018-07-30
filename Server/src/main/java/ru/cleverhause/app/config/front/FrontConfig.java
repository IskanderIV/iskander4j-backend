package ru.cleverhause.app.config.front;

import org.springframework.context.annotation.Bean;
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
@ComponentScan(basePackages = {
        "ru.cleverhause.service.user",
        "ru.cleverhause.app.rest.site",
        "ru.cleverhause.app.rest.user",
        "ru.cleverhause.app.validator"})
public class FrontConfig implements WebMvcConfigurer {

    @Bean
    public String getString() {
        String str = new String("18");
        return str;
    }
//    @Bean
//    public static PropertyPlaceholderConfigurer placeHolderConfigurer() {
//        return new PropertyPlaceholderConfigurer();
//    }

    //This is a straightforward mechanism to map views names to URLs with no need for an explicit controller in between
//    @Override
//    public void addViewControllers(ViewControllerRegistry registry) {
//        super.addViewControllers(registry);
//
//        registry.addViewController("/somepage.html");
//    }

//    @Bean
//    public InternalResourceViewResolver viewResolver() {
//        InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
//        viewResolver.setPrefix("/WEB-INF/pages/"); //
//        viewResolver.setSuffix(".jsp");
//        return viewResolver;
//    }
}
