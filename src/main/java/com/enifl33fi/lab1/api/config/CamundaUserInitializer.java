package com.enifl33fi.lab1.api.config;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.camunda.bpm.engine.IdentityService;
import org.camunda.bpm.engine.identity.User;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CamundaUserInitializer {
    private final IdentityService identityService;

    @PostConstruct
    public void createDefaultUser() {
        if (identityService.createUserQuery().userId("admin").singleResult() == null) {
            User user = identityService.newUser("admin");
            user.setFirstName("Admin");
            user.setLastName("User");
            user.setEmail("admin@example.com");
            user.setPassword("admin");
            identityService.saveUser(user);
        }
    }
} 