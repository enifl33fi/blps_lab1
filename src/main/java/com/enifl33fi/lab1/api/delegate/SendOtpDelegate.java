package com.enifl33fi.lab1.api.delegate;

import com.enifl33fi.lab1.api.model.security.EmailOtp;
import com.enifl33fi.lab1.api.model.user.User;
import com.enifl33fi.lab1.api.service.EmailService;
import com.enifl33fi.lab1.api.service.UserService;
import com.enifl33fi.lab1.api.service.MqttService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Component;

@Component("sendOtpDelegate")
@RequiredArgsConstructor
@Log4j2
public class SendOtpDelegate implements JavaDelegate {

    private final EmailService emailService;
    private final UserService userService;
    private final MqttService mqttService;

    @Override
    public void execute(DelegateExecution execution) throws Exception {
        log.info("SendOtpDelegate: Старт отправки OTP для execution: {}", execution.getId());

        String email = (String) execution.getVariable("email");
        if (email == null || email.isEmpty()) {
            log.error("Email variable is null or empty");
            throw new RuntimeException("Email variable is required");
        }

        User user = userService.loadUserByUsername(email);
        EmailOtp emailOtp = emailService.createEmailOtp(user);
        mqttService.sendEmailOtpRequest(user.getEmail(), emailOtp.getConfirmationToken());

        log.info("SendOtpDelegate: Email с OTP отправлен на {} через очередь", email);
    }
} 