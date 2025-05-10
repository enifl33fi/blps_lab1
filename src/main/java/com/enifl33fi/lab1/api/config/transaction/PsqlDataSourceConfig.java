package com.enifl33fi.lab1.api.config.transaction;

import com.atomikos.jdbc.AtomikosDataSourceBean;
import lombok.RequiredArgsConstructor;
import org.postgresql.xa.PGXADataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

@Configuration
@DependsOn("transactionManager")
@EnableJpaRepositories(
        basePackages = "com.enifl33fi.lab1.api.repository",
        entityManagerFactoryRef = "psqlEntityManager",
        transactionManagerRef = "transactionManager"
)
@RequiredArgsConstructor
public class PsqlDataSourceConfig {
    private final JpaVendorAdapter jpaVendorAdapter;

    @Value("${db.url}")
    String dbUrl;

    @Value("${db.username}")
    String dbUsername;

    @Value("${db.password}")
    String dbPassword;


    @Bean(name = "psqlDataSource")
    public DataSource psqlDataSource() {
        PGXADataSource pgxaDataSource = new PGXADataSource();
        pgxaDataSource.setURL(dbUrl);
        pgxaDataSource.setUser(dbUsername);
        pgxaDataSource.setPassword(dbPassword);

        AtomikosDataSourceBean xaDataSource = new AtomikosDataSourceBean();
        xaDataSource.setXaDataSource(pgxaDataSource);
        xaDataSource.setUniqueResourceName("xaPsql");
        xaDataSource.setBorrowConnectionTimeout(60);
        xaDataSource.setMaxPoolSize(20);
        return xaDataSource;
    }

    @Bean(name = "psqlEntityManager")
    public LocalContainerEntityManagerFactoryBean psqlEntityManager() {
        Map<String, Object> properties = getStringObjectMap();

        LocalContainerEntityManagerFactoryBean entityManager = new LocalContainerEntityManagerFactoryBean();
        entityManager.setJtaDataSource(psqlDataSource());
        entityManager.setJpaVendorAdapter(jpaVendorAdapter);
        entityManager.setPackagesToScan(
                "com.enifl33fi.lab1.api.model.offers",
                "com.enifl33fi.lab1.api.model.security",
                "com.enifl33fi.lab1.api.model.user"
        );
        entityManager.setPersistenceUnitName("psql");
        entityManager.setJpaPropertyMap(properties);
        return entityManager;
    }

    private static Map<String, Object> getStringObjectMap() {
        Map<String, Object> properties = new HashMap<String, Object>();
        properties.put("hibernate.hbm2ddl.auto", "update");
        properties.put("hibernate.dialect", "org.hibernate.dialect.PostgreSQLDialect");
        properties.put("hibernate.show_sql", "true");
        properties.put("hibernate.temp.use_jdbc_metadata_defaults", "false");
        properties.put("hibernate.transaction.jta.platform", AtomikosJtaPlatform.class.getName());
        properties.put("javax.persistence.transactionType", "JTA");
        return properties;
    }
}
