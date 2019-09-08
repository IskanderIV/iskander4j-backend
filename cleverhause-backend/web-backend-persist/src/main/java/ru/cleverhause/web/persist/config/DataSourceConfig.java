package ru.cleverhause.web.persist.config;

import org.apache.commons.dbcp.BasicDataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;

@Configuration
public class DataSourceConfig {
    @Value("${jdbc.driverClassName}")
    private String dataSourceDriverClassName;
    @Value("${jdbc.url}")
    private String dataSourceUrl;
    @Value("${jdbc.username}")
    private String dataSourceUserame;
    @Value("${jdbc.password}")
    private String dataSourcePassword;

    @Bean
    @Profile("dev")
    public DataSource testDataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName("org.h2.Driver");
        dataSource.setUrl("jdbc:h2:mem:db;DB_CLOSE_DELAY=-1");
        dataSource.setUsername("user");
        dataSource.setPassword("user");
        return dataSource;
    }

    @Bean
    @Profile("prod")
    public DataSource prodDataSource() {
        BasicDataSource dataSource = new BasicDataSource();
        dataSource.setDriverClassName(dataSourceDriverClassName);
        dataSource.setUrl(dataSourceUrl);
        dataSource.setUsername(dataSourceUserame);
        dataSource.setPassword(dataSourcePassword);
        return dataSource;
    }
}
