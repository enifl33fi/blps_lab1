package com.enifl33fi.lab1.api.service;

import com.enifl33fi.lab1.api.dto.request.AuthRequestDto;
import com.enifl33fi.lab1.api.exception.EmailNotUniqueException;
import com.enifl33fi.lab1.api.exception.EmailOtpException;
import com.enifl33fi.lab1.api.mapper.UserMapper;
import com.enifl33fi.lab1.api.model.security.EmailOtp;
import com.enifl33fi.lab1.api.model.user.Role;
import com.enifl33fi.lab1.api.model.user.User;
import com.enifl33fi.lab1.api.repository.EmailOtpRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionStatus;

@Service
@RequiredArgsConstructor
@Log4j2
public class AuthenticationService {
    private final UserService userService;
    private final EmailService emailService;
    private final UserMapper userMapper;
    private final ValidatingService validatingService;
    private final EmailOtpRepository emailOtpRepository;
    private final TransactionService transactionService;
    @Autowired
    private AuthenticationManager authenticationManager;

    public void register(AuthRequestDto userDto) {
        TransactionStatus transaction = null;
        try {
            transaction = transactionService.createTransaction("registerTransaction");

            validatingService.validateEntity(userDto);
            if (!isUserUnique(userDto.getEmail())) {
                throw new EmailNotUniqueException(userDto.getEmail());
            }

            User user = userMapper.mapUserFromAuthDto(userDto);
            user = userService.saveUser(user);


            emailService.sendEmail(user);

            transactionService.commit(transaction);
        } catch (Exception e) {
            if (transaction != null) transactionService.rollback(transaction);
            throw e;
        }
    }

    public void login(AuthRequestDto userDto) {
        validatingService.validateEntity(userDto);
        try {
            Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(userDto.getEmail(), userDto.getPassword())
            );
            log.info("User {} authenticated via Spring Security", userDto.getEmail());
        } catch (AuthenticationException e) {
            throw new RuntimeException("Login failed: " + e.getMessage());
        }
    }

    public void confirmAccount(String otp) {
        EmailOtp emailOtp = emailOtpRepository.findByConfirmationToken(otp)
                .orElseThrow(() -> new EmailOtpException("Not found"));

        User user = emailOtp.getUser();
        user.setRole(Role.USER);
        userService.saveUser(user);

        log.info("User {} confirmed OTP and promoted to USER", user.getEmail());
    }

    public Boolean isUserUnique(String email) {
        return userService.isEmailUnique(email);
    }
}
