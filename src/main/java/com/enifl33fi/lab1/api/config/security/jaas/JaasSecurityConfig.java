package com.enifl33fi.lab1.api.config.security.jaas;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.jaas.AuthorityGranter;
import org.springframework.security.authentication.jaas.DefaultJaasAuthenticationProvider;
import org.springframework.security.authentication.jaas.memory.InMemoryConfiguration;

import javax.security.auth.login.AppConfigurationEntry;
import java.security.Principal;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Конфигурация для интеграции JAAS со Spring Security.
 * 
 * - Настраивает LoginModule
 * - Определяет AuthorityGranter для преобразования JAAS principals в роли Spring Security
 *
 * @author amphyxs
 */
@Configuration
public class JaasSecurityConfig {

    /**
     * Создает конфигурацию JAAS, которая определяет нашу XML-аутентификацию.
     * 
     * @return Конфигурация JAAS
     */
    @Bean
    public javax.security.auth.login.Configuration jaasConfiguration() {
        Map<String, String> options = new HashMap<>();
        options.put("debug", "true");
        
        AppConfigurationEntry appConfigurationEntry = new AppConfigurationEntry(
                UserLoginModule.class.getName(),
                AppConfigurationEntry.LoginModuleControlFlag.REQUIRED,
                options
        );
        
        Map<String, AppConfigurationEntry[]> configMap = new HashMap<>();
        configMap.put("SPRINGSECURITY", new AppConfigurationEntry[] {appConfigurationEntry});
        
        return new InMemoryConfiguration(configMap);
    }

    /**
     * Создает JAAS провайдер аутентификации для работы со Spring Security.
     * 
     * @return DefaultJaasAuthenticationProvider
     */
    @Bean
    @Lazy
    public DefaultJaasAuthenticationProvider jaasAuthenticationProvider() {
        DefaultJaasAuthenticationProvider provider = new DefaultJaasAuthenticationProvider();
        provider.setConfiguration(jaasConfiguration());
        provider.setAuthorityGranters(new AuthorityGranter[] {new JaasAuthorityGranter()});
        provider.setLoginContextName("SPRINGSECURITY");
        return provider;
    }

    /**
     * Преобразователь прав доступа, который конвертирует JAAS principals в роли Spring Security.
     */
    private static class JaasAuthorityGranter implements AuthorityGranter {
        @Override
        public Set<String> grant(Principal principal) {
            Set<String> authorities = new HashSet<>();
            
            if (principal instanceof RolePrincipal) {
                authorities.add("ROLE_" + principal.getName());
            }
            
            return authorities;
        }
    }
} 