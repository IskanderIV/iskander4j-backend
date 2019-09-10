package ru.cleverhause.web.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import ru.cleverhause.web.config.mvc.WebMvcConfig;
import ru.cleverhause.web.config.security.CommonSecurityConfig;
import ru.cleverhause.web.config.service.ServiceConfig;
import ru.cleverhause.web.persist.config.FrontPersistenceConfig;

/**
 * Created by Alexandr on 15.11.2017.
 */
@Configuration
@ComponentScan(basePackages = {
        "ru.cleverhause.web.api.converter",
        "ru.cleverhause.web.api.validation"})
@PropertySource(value = {"classpath:application.properties"})
@Import(value = {FrontPersistenceConfig.class, CommonSecurityConfig.class, ServiceConfig.class})
public class WebBackAppConfig {

    @Bean
    public static PropertySourcesPlaceholderConfigurer placeHolderConfigurer() {
        return new PropertySourcesPlaceholderConfigurer();
    }

    @Bean
    public MappingJackson2HttpMessageConverter jacksonHttpMessageConverter() {
        MappingJackson2HttpMessageConverter jsonConverter = new MappingJackson2HttpMessageConverter();
        jsonConverter.setPrettyPrint(true);
        return jsonConverter;
    }
}
