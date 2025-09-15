package com.enifl33fi.lab1.api.config.security.jaas;

import java.security.Principal;

public class JaasPrincipal implements Principal {
    private final String name;

    public JaasPrincipal(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }
}