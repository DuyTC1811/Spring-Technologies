package com.example.dynamicdatasource.configurations;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

import static com.example.dynamicdatasource.configurations.DataSourceType.MASTER;
import static com.example.dynamicdatasource.configurations.DataSourceType.SALVE;

@Configuration
public class DataSourceConfig {
    @Value("${spring.datasource.master.url}")
    private String masterUrl;

    @Value("${spring.datasource.master.username}")
    private String masterUserName;

    @Value("${spring.datasource.master.password}")
    private String masterPassword;

    @Value("${spring.datasource.slave.url}")
    private String slaveUrl;

    @Value("${spring.datasource.slave.username}")
    private String slaveUsername;

    @Value("${spring.datasource.slave.password}")
    private String slavePassword;

    @Bean
    public DataSource dataSources() {
        Map<Object, Object> targetDataSources = new HashMap<>();
        RoutingDataSource routingDataSource = new RoutingDataSource();
        targetDataSources.put(MASTER, dataSourceMaster());
        targetDataSources.put(SALVE, dataSourceSalve());
        routingDataSource.setTargetDataSources(targetDataSources);
        // Set as all transaction point to master
        routingDataSource.setDefaultTargetDataSource(dataSourceMaster());
        return routingDataSource;
    }


    public DataSource dataSourceMaster() {
        final HikariConfig hikariConfig = new HikariConfig();
        hikariConfig.setJdbcUrl(masterUrl);
        hikariConfig.setUsername(masterUserName);
        hikariConfig.setPassword(masterPassword);
        hikariConfig.setPoolName("MASTER");
        return new HikariDataSource(hikariConfig);
    }

    public DataSource dataSourceSalve() {
        final HikariConfig hikariConfig = new HikariConfig();
        hikariConfig.setJdbcUrl(slaveUrl);
        hikariConfig.setUsername(slaveUsername);
        hikariConfig.setPassword(slavePassword);
        hikariConfig.setPoolName("SALVE");
        return new HikariDataSource(hikariConfig);
    }
}
