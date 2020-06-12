package ru.cleverhause.users.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.autoconfigure.orm.jpa.JpaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.util.Properties;

@Configuration
@EnableTransactionManagement
public class DataBaseConfig {

    @Bean
    public PasswordEncoder encoder() {
        return new BCryptPasswordEncoder();
    }
//
//    @Bean
//    @Profile("local")
//    public DataSource simpleDataSource(DataSourceProperties properties) {
//        DriverManagerDataSource ds = new DriverManagerDataSource(properties.getUrl());
//        ds.setUsername(properties.getUsername());
//        ds.setPassword(properties.getPassword());
//        ds.setDriverClassName(properties.getDriverClassName());
//        return ds;
//    }
//
//    @Bean
//    @Profile("prod")
//    public DataSource dataSource(DataSourceProperties properties) {
//        HikariConfig config = new HikariConfig();
//        config.setJdbcUrl(properties.getUrl());
//        config.setUsername(properties.getUsername());
//        config.setPassword(properties.getPassword());
//        config.setDriverClassName(properties.getDriverClassName());
//        return new HikariDataSource(config);
//    }
//
//    @Bean
//    public LocalContainerEntityManagerFactoryBean entityManagerFactory(DataSource dataSource, JpaProperties jpaConfig) {
//        JpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
//
//        LocalContainerEntityManagerFactoryBean entityManagerFactory = new LocalContainerEntityManagerFactoryBean();
//        entityManagerFactory.setDataSource(dataSource);
//        entityManagerFactory.setPackagesToScan("ru.cleverhause.users.entity");
//        entityManagerFactory.setJpaVendorAdapter(vendorAdapter);
//
//        Properties jpaProperties = new Properties();
//        jpaProperties.putAll(jpaConfig.getProperties());
//
//        entityManagerFactory.setJpaProperties(jpaProperties);
//
//        return entityManagerFactory;
//    }

//    @Bean
//    public JpaTransactionManager transactionManager(DataSource dataSource, JpaProperties jpaConfig) {
//        JpaTransactionManager txManager = new JpaTransactionManager();
//        txManager.setEntityManagerFactory(entityManagerFactory(dataSource, jpaConfig).getObject());
//        return txManager;
//    }
}
