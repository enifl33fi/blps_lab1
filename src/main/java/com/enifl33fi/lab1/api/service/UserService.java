package com.enifl33fi.lab1.api.service;

import com.enifl33fi.lab1.api.model.user.User;
import com.enifl33fi.lab1.api.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {
    private final UserRepository userRepository;

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
