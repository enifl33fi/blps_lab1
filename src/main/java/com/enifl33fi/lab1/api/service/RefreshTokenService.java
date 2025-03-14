package com.enifl33fi.lab1.api.service;

import com.enifl33fi.lab1.api.exception.RefreshTokenException;
import com.enifl33fi.lab1.api.model.security.RefreshToken;
import com.enifl33fi.lab1.api.model.user.User;
import com.enifl33fi.lab1.api.repository.RefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {
    private final RefreshTokenRepository refreshTokenRepository;

    public void saveToken(String token, User user) {
        RefreshToken refreshToken = getByUser(user).orElse(RefreshToken.builder().user(user).build());
        refreshToken.setToken(token);
        refreshTokenRepository.save(refreshToken);
    }

    private Optional<RefreshToken> getByUser(User user) {
        return refreshTokenRepository.findByUser(user);
    }

    public RefreshToken getByToken(String token) {
        return refreshTokenRepository
                .findByToken(token)
                .orElseThrow(() -> new RefreshTokenException("Not found"));
    }

    public void deleteByToken(String token) {
        refreshTokenRepository.deleteByToken(token);
    }
}
