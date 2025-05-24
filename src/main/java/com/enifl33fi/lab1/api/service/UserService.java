package com.enifl33fi.lab1.api.service;

import com.enifl33fi.lab1.api.model.user.User;
import com.enifl33fi.lab1.api.model.user.Role;
import com.enifl33fi.lab1.api.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${admin.email}")
    private String adminEmail;
    @Value("${admin.password}")
    private String adminPassword;

    @PostConstruct
    public void initAdmin() {
        Optional<User> user = userRepository.findByEmail(adminEmail);

        if (user.isEmpty()) {
            User admin = new User();
            admin.setEmail(adminEmail);
            admin.setPassword(passwordEncoder.encode(adminPassword));
            admin.setRole(Role.ADMIN);
            admin.setLocked(false);
            userRepository.save(admin);
        }
    }

    @Override
    public User loadUserByUsername(String email) {
        return userRepository
                .findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User with provided username not found."));
    }

    public User saveUser(User user) {
        return userRepository.saveAndFlush(user);
    }

    public boolean isEmailUnique(String email) {
        Optional<User> user = userRepository.findByEmail(email);
        return user.isEmpty();
    }
}
