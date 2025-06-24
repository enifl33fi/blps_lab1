package com.enifl33fi.lab1.api.delegate;

import com.enifl33fi.lab1.api.annotation.SkipAuthentication;
import com.enifl33fi.lab1.api.dto.request.AuthRequestDto;
import com.enifl33fi.lab1.api.service.AuthenticationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Component;

@Component("loginDelegate")
@RequiredArgsConstructor
@Log4j2
@SkipAuthentication
public class LoginDelegate implements JavaDelegate {

    private final AuthenticationService authenticationService;

    @Override
    public void execute(DelegateExecution execution) throws Exception {
        log.info("Starting login process for execution: {}", execution.getId());

        // Get form data from process variables
        String email = (String) execution.getVariable("email");
        String password = (String) execution.getVariable("password");

        // Create AuthRequestDto from form data
        AuthRequestDto userDto = AuthRequestDto.builder()
                .email(email)
                .password(password)
                .build();

        // Perform login
        authenticationService.login(userDto);

        log.info("Login completed for user: {}", email);
    }
} 