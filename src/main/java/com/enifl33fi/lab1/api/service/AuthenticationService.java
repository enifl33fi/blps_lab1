package com.enifl33fi.lab1.api.service;

import com.enifl33fi.lab1.api.config.security.jaas.UserCallbackHandler;
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
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionStatus;

import javax.security.auth.Subject;
import javax.security.auth.login.LoginContext;
import javax.security.auth.login.LoginException;

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
            LoginContext loginContext = new LoginContext("YourAppLogin",
                    new UserCallbackHandler(userDto.getEmail(), userDto.getPassword()));
            loginContext.login();

            Subject subject = loginContext.getSubject();
            log.info("User {} authenticated via JAAS", userDto.getEmail());

        } catch (LoginException e) {
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
