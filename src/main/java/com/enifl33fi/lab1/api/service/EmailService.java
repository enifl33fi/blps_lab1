package com.enifl33fi.lab1.api.service;

import com.enifl33fi.lab1.api.model.security.EmailOtp;
import com.enifl33fi.lab1.api.model.user.User;
import com.enifl33fi.lab1.api.repository.EmailOtpRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService {
    private final JavaMailSender javaMailSender;
    private final EmailOtpRepository emailOtpRepository;

    @Value("${spring.mail.username}")
    private String fromMail;

    @Async
    public void sendEmail(User user) {
        EmailOtp emailOtp = emailOtpRepository.findByUser(user).orElse(new EmailOtp(user));
        emailOtpRepository.save(emailOtp);

        SimpleMailMessage email = new SimpleMailMessage();
        email.setTo(user.getEmail());
        email.setFrom(fromMail);
        email.setSubject("Confirmation code");
        email.setText(emailOtp.getConfirmationToken());

        javaMailSender.send(email);
    }
}
