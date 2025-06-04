package com.enifl33fi.lab1.api.service;

import com.enifl33fi.lab1.api.model.security.EmailOtp;
import com.enifl33fi.lab1.api.model.user.User;
import com.enifl33fi.lab1.api.repository.EmailOtpRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class EmailService {
    private final EmailOtpRepository emailOtpRepository;

    public EmailOtp createEmailOtp(User user) {
        EmailOtp emailOtp = new EmailOtp(user);
        Optional<EmailOtp> emailOtpOptional = emailOtpRepository.findByUser(user);

        emailOtpOptional.ifPresent(otp -> emailOtp.setId(otp.getId()));

        return emailOtpRepository.save(emailOtp);
    }
}
