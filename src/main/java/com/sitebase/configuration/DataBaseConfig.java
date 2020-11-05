package com.sitebase.configuration;

import com.zaxxer.hikari.HikariDataSource;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.orm.hibernate5.HibernateTransactionManager;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.util.Properties;

@Configuration
@EnableTransactionManagement
public class DataBaseConfig {

    private Environment env;

    public DataBaseConfig(Environment env) {
        this.env = env;
    }

    @Bean
    public LocalSessionFactoryBean sessionFactory() {
        LocalSessionFactoryBean localSessionFactory = new LocalSessionFactoryBean();
        localSessionFactory.setDataSource(dataSource());
        localSessionFactory.setPackagesToScan("com.baeldung.hibernate.bootstrap.model");

        return localSessionFactory;
    }

    @Bean
    public DataSource dataSource() {
        HikariDataSource ds = new HikariDataSource();

        ds.setDataSourceClassName("org.postgresql.ds.PGSimpleDataSource");
        ds.setUsername(env.getProperty("database.user"));
        ds.setPassword(env.getProperty("database.password"));
        ds.setJdbcUrl(env.getProperty("database.url"));

        return ds;
    }

    @Bean
    public PlatformTransactionManager transactionManager() {
        HibernateTransactionManager transactionManager = new HibernateTransactionManager();

        transactionManager.setDataSource(dataSource());

        return transactionManager;
    }

    private final Properties hibernateProperties() {
        Properties hibernateProperties = new Properties();
        hibernateProperties.setProperty(
                "hibernate.hbm2ddl.auto", "create-drop");
        hibernateProperties.setProperty(
                "hibernate.dialect", "org.hibernate.dialect.H2Dialect");

        return hibernateProperties;
    }
}
