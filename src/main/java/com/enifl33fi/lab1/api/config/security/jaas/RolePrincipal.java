package com.enifl33fi.lab1.api.config.security.jaas;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.security.Principal;

/**
 * Principal для роли.
 * 
 * @author amphyxs
 */
@Getter
@AllArgsConstructor
public class RolePrincipal implements Principal {
    
    private final String role;
    
    @Override
    public String getName() {
        return role;
    }
} 