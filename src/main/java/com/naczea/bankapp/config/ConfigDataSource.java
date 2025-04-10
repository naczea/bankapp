package com.naczea.bankapp.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;

/**
 * Manual datasource configuration for scalability reasons
 */
@Configuration
public class ConfigDataSource {

    private final Environment env;

    public ConfigDataSource(Environment env) {
        this.env = env;
    }

    /**
     * You can add other beans with different datasources
     * @return
     */
    @Primary
    @Bean(name = "dataSourceBankApp")
    public DataSource dataSourceBankApp() {
        return createDataSource("spring.datasource");
    }

    private DriverManagerDataSource createDataSource(String prefix) {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(env.getProperty("spring.datasource.driver-class-name"));
        dataSource.setUrl(env.getProperty(prefix + ".url"));
        dataSource.setUsername(env.getProperty(prefix + ".username"));
        dataSource.setPassword(env.getProperty(prefix + ".password"));
        return dataSource;
    }
}
