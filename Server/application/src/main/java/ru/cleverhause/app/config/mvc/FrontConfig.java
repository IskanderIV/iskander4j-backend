package ru.cleverhause.app.config.mvc;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

/**
 * Created by
 *
 * @author Aleksandr_Ivanov1
 * @date 7/9/2018.
 */
@Configuration
@EnableWebMvc
@ComponentScan(basePackages = {
        "ru.cleverhause.rest.site",
        "ru.cleverhause.rest.user",
        "ru.cleverhause.validators"})
public class FrontConfig implements WebMvcConfigurer {

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
    @Bean
    public ReloadableResourceBundleMessageSource messageSource() {
        ReloadableResourceBundleMessageSource resourceBundleMessageSource =
                new ReloadableResourceBundleMessageSource();
        String[] basename = {"classpath:validation"};
        resourceBundleMessageSource.setBasenames(basename);

        return resourceBundleMessageSource;
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/resources/**").addResourceLocations("/resources/");
    }

    @Bean
    public InternalResourceViewResolver viewResolver() {
        InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
        viewResolver.setPrefix("/WEB-INF/pages/");
        viewResolver.setSuffix(".jsp");
        return viewResolver;
    }
}
