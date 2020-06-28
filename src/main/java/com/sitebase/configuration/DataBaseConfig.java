package com.sitebase.configuration;

import com.zaxxer.hikari.HikariDataSource;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.transaction.TransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;

@Configuration
@EnableTransactionManagement
public class DataBaseConfig {

    @Autowired
    private Environment env;

    @Bean
    public DataSource dataSource() {
        HikariDataSource ds = new HikariDataSource();

        ds.setDataSourceClassName("org.postgresql.ds.PGSimpleDataSource");
        ds.setUsername(env.getProperty("database.user"));
        ds.setPassword(env.getProperty("database.password"));
        ds.set

        return ds;
    }

    @Bean
    public TransactionManager transactionManager() {
        TransactionManager transactionManager = new TransactionManager() {
            @Override
            public int hashCode() {
                return super.hashCode();
            }
        }
    }
}
