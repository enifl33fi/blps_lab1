package com.enifl33fi.lab1.api.service;

import com.enifl33fi.lab1.api.dto.request.AuthRequestDto;
import com.enifl33fi.lab1.api.dto.response.AuthResponseDto;
import com.enifl33fi.lab1.api.exception.EmailNotUniqueException;
import com.enifl33fi.lab1.api.exception.EmailOtpException;
import com.enifl33fi.lab1.api.exception.RefreshTokenException;
import com.enifl33fi.lab1.api.mapper.UserMapper;
import com.enifl33fi.lab1.api.model.security.EmailOtp;
import com.enifl33fi.lab1.api.model.security.RefreshToken;
import com.enifl33fi.lab1.api.model.user.Role;
import com.enifl33fi.lab1.api.model.user.User;
import com.enifl33fi.lab1.api.repository.EmailOtpRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Log4j2
public class AuthenticationService {
    private final UserService userService;
    private final EmailService emailService;
    private final UserMapper userMapper;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final RefreshTokenService refreshTokenService;
    private final ValidatingService validatingService;
    private final EmailOtpRepository emailOtpRepository;

    @Transactional
    public AuthResponseDto register(AuthRequestDto userDto) {
        validatingService.validateEntity(userDto);
        if (!isUserUnique(userDto.getEmail())) {
            throw new EmailNotUniqueException(userDto.getEmail());
        }

        User user = userMapper.mapUserFromAuthDto(userDto);
        user = userService.saveUser(user);
        AuthResponseDto responseDto = getResponseByUser(user);

        emailService.sendEmail(user);

        return responseDto;
    }

    @Transactional
    public AuthResponseDto login(AuthRequestDto userDto) {
        validatingService.validateEntity(userDto);

        Authentication authentication =
                authenticationManager.authenticate(
                        new UsernamePasswordAuthenticationToken(userDto.getEmail(), userDto.getPassword()));
        User user = (User) authentication.getPrincipal();
        return getResponseByUser(user);
    }

    public AuthResponseDto confirmAccount(String otp) {
        EmailOtp emailOtp = emailOtpRepository.findByConfirmationToken(otp).orElseThrow(() -> new EmailOtpException("Not found"));

        User user = emailOtp.getUser();
        user.setRole(Role.USER);
        userService.saveUser(user);

        return getResponseByUser(user);
    }

    public AuthResponseDto getTokens(String refreshToken) {
        if (jwtService.validateRefreshToken(refreshToken)) {
            RefreshToken token = refreshTokenService.getByToken(refreshToken);
            User user = token.getUser();
            return getResponseByUser(user);
        }
        refreshTokenService.deleteByToken(refreshToken);
        throw new RefreshTokenException("Invalid token");
    }

    public Boolean isUserUnique(String email) {
        return userService.isEmailUnique(email);
    }

    private AuthResponseDto getResponseByUser(User user) {
        String accessToken = jwtService.generateAccessToken(user);
        String refreshToken = jwtService.generateRefreshToken(user);
        refreshTokenService.saveToken(refreshToken, user);
        return AuthResponseDto.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }
}
