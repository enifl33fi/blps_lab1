package com.enifl33fi.lab1.api.config;

import com.zaxxer.hikari.HikariDataSource;
import org.camunda.bpm.engine.spring.SpringProcessEngineConfiguration;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.io.IOException;

@Configuration
public class CamundaConfig {
    @Value("${spring.datasource.url}")
    String dbUrl;

    @Value("${spring.datasource.username}")
    String dbUsername;

    @Value("${spring.datasource.password}")
    String dbPassword;


    @Bean(name = "camundaDataSource")
    @Primary
    public DataSource camundaDataSource() {
        HikariDataSource ds = new HikariDataSource();
        ds.setJdbcUrl(dbUrl);
        ds.setUsername(dbUsername);
        ds.setPassword(dbPassword);
        ds.setDriverClassName("org.postgresql.Driver");
        ds.setMaximumPoolSize(20);
        ds.setMinimumIdle(5);

        return ds;
    }

    @Bean(name = "camundaTransactionManager")
    public PlatformTransactionManager camundaTransactionManager() {
        return new DataSourceTransactionManager(camundaDataSource());
    }

    @Bean
    public SpringProcessEngineConfiguration processEngineConfiguration() throws IOException {
        SpringProcessEngineConfiguration config = new SpringProcessEngineConfiguration();
        config.setDataSource(camundaDataSource());
        config.setTransactionManager(camundaTransactionManager());
        config.setDatabaseSchemaUpdate("true");
        config.setJobExecutorActivate(true);
        config.setHistory("full");
        config.setMetricsEnabled(true);
        config.setAuthorizationEnabled(true);

        // Explicitly load BPMN files
        PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        Resource[] resources = resolver.getResources("classpath:bpmn/*.bpmn");
        config.setDeploymentResources(resources);

        return config;
    }
} 