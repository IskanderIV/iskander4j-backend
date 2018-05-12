package ru.cleverhause.config;

import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

/**
 * Created by Alexandr on 15.11.2017.
 */
@EnableWebMvc
@Configuration
@ComponentScan(basePackages = {"ru.cleverhause"})
@PropertySource(value = {"classpath:application.properties"})
@Import(value = {AppConfigData.class, AppConfigSecurity.class})
public class AppConfig extends WebMvcConfigurerAdapter {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/resources/**").addResourceLocations("/resources/");
    }

    @Bean
    public ReloadableResourceBundleMessageSource messageSource() {
        ReloadableResourceBundleMessageSource resourceBundleMessageSource =
                new ReloadableResourceBundleMessageSource();
        String[] basename = {"classpath:validation"};
        resourceBundleMessageSource.setBasenames(basename);

        return resourceBundleMessageSource;
    }

    @Bean
    public static PropertyPlaceholderConfigurer placeHolderConfigurer() {
        return new PropertyPlaceholderConfigurer();
    }

    //This is a straightforward mechanism to map views names to URLs with no need for an explicit controller in between
//    @Override
//    public void addViewControllers(ViewControllerRegistry registry) {
//        super.addViewControllers(registry);
//
//        registry.addViewController("/somepage.html");
//    }

    @Bean
    public InternalResourceViewResolver viewResolver() {
        InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
        viewResolver.setPrefix("/WEB-INF/pages/"); //
        viewResolver.setSuffix(".jsp");
        return viewResolver;
    }

    @Bean
    public MappingJackson2HttpMessageConverter jacksonHttpMessageConverter() {
        MappingJackson2HttpMessageConverter jsonConverter = new MappingJackson2HttpMessageConverter();
        jsonConverter.setPrettyPrint(true);
        return jsonConverter;
    }

}
