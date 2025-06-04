package com.enifl33fi.lab1.api.config.ytsaurus;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class YtsaurusJcaConfig {
    @Value("${ytsaurus.endpoint}")
    private String endpoint;
    @Value("${ytsaurus.token}")
    private String token;

    @Bean
    public YtsaurusConnectionFactory ytsaurusConnectionFactory() {
        YtsaurusManagedConnectionFactory managedConnectionFactory = new YtsaurusManagedConnectionFactory();
        managedConnectionFactory.setEndpoint(this.endpoint);
        managedConnectionFactory.setToken(this.token);

        return (YtsaurusConnectionFactory) managedConnectionFactory.createConnectionFactory();
    }
}
