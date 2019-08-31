package ru.cleverhause.device.config.persistence;

import org.apache.commons.dbcp.BasicDataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.util.Properties;

/**
 * Created by
 *
 * @author Aleksandr_Ivanov1
 * @date 3/5/2018.
 */
@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(basePackages = "ru.cleverhause.api.persist")
@PropertySource(value = {"classpath:database.properties"})
public class PersistenceConfig {

    private static final String PROPERTY_NAME_HIBERNATE_DIALECT = "hibernate.dialect";
    private static final String PROPERTY_NAME_HIBERNATE_SHOW_SQL = "hibernate.show_sql";
    private static final String[] HIBERNATE_ENTITY_PACKAGES = {"ru.cleverhause.api.persist.entities"};

    @Value("${jdbc.driverClassName}")
    private String dataSourceDriverClassName;
    @Value("${jdbc.url}")
    private String dataSourceUrl;
    @Value("${jdbc.username}")
    private String dataSourceUserame;
    @Value("${jdbc.password}")
    private String dataSourcePassword;


//    @Bean
//    DataSource dataSource() {
//        DataSource dataSource = null;
//        JndiTemplate jndi = new JndiTemplate();
//        try {
//            dataSource = jndi.lookup("java:comp/env/jdbc/yourname", DataSource.class);
//        } catch (NamingException e) {
//            logger.error("NamingException for java:comp/env/jdbc/yourname", e);
//        }
//        return dataSource;
//    }

    @Bean
    public DataSource dataSource() {
        BasicDataSource dataSource = new BasicDataSource();
        dataSource.setDriverClassName(dataSourceDriverClassName);
        dataSource.setUrl(dataSourceUrl);
        dataSource.setUsername(dataSourceUserame);
        dataSource.setPassword(dataSourcePassword);
        return dataSource;
    }

    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory() {
        HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();

        LocalContainerEntityManagerFactoryBean entityManagerFactory = new LocalContainerEntityManagerFactoryBean();
        entityManagerFactory.setDataSource(dataSource());
        entityManagerFactory.setPackagesToScan(HIBERNATE_ENTITY_PACKAGES);
        entityManagerFactory.setJpaVendorAdapter(vendorAdapter);

        Properties jpaProperties = new Properties();
        jpaProperties.put(PROPERTY_NAME_HIBERNATE_DIALECT, "org.hibernate.dialect.PostgreSQLDialect");
//        jpaProperties.put(PROPERTY_NAME_HIBERNATE_SHOW_SQL, "true");

        entityManagerFactory.setJpaProperties(jpaProperties);

        return entityManagerFactory;
    }

//    @Bean
//    public LocalSessionFactoryBean factoryBean() {
//        LocalSessionFactoryBean localSessionFactoryBean = new LocalSessionFactoryBean();
//        localSessionFactoryBean.setDataSource(dataSource());
//        localSessionFactoryBean.setPackagesToScan(HIBERNATE_ENTITY_PACKAGES);
//        localSessionFactoryBean.setAnnotatedClasses(HIBERNATE_ENTITY_ANNOTATED_CLASSES);
//        return localSessionFactoryBean;
//    }

    @Bean
    public JpaTransactionManager transactionManager() {
        JpaTransactionManager txManager = new JpaTransactionManager();
        txManager.setEntityManagerFactory(entityManagerFactory().getObject());

        return txManager;
    }


    @Bean
    public static PropertySourcesPlaceholderConfigurer placeHolderConfigurer() {
        return new PropertySourcesPlaceholderConfigurer();
    }
}
