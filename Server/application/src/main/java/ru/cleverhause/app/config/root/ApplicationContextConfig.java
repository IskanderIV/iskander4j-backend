package ru.cleverhause.app.config.root;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import ru.cleverhause.app.config.persistence.PersistenceConfig;
import ru.cleverhause.app.config.security.CommonSecurityConfig;
import ru.cleverhause.app.config.service.ServiceConfig;

/**
 * Created by Alexandr on 15.11.2017.
 */
@Configuration
@ComponentScan(basePackages = {"ru.cleverhause.app.config.root"})
@PropertySource(value = {"classpath:application.properties"})
@Import(value = {PersistenceConfig.class, CommonSecurityConfig.class, ServiceConfig.class})
public class ApplicationContextConfig {

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