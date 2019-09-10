package ru.cleverhause.device.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import ru.cleverhause.device.config.mvc.DeviceWebMvcConfig;
import ru.cleverhause.device.config.security.CommonSecurityConfig;
import ru.cleverhause.device.config.service.ServiceConfig;
import ru.cleverhause.device.dao.config.DevicePersistenceConfig;

@Configuration
@PropertySource(value = {"classpath:application.properties"})
@ComponentScan(value = {
        "ru.cleverhause.device.api.filters",
        "ru.cleverhause.device.api.converter"})
@Import(value = {DevicePersistenceConfig.class, CommonSecurityConfig.class, ServiceConfig.class})
public class DeviceAppConfig {

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