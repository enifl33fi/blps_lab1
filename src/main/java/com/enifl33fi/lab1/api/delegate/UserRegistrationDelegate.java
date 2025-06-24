package com.enifl33fi.lab1.api.delegate;

import com.enifl33fi.lab1.api.annotation.SkipAuthentication;
import com.enifl33fi.lab1.api.dto.request.AuthRequestDto;
import com.enifl33fi.lab1.api.service.AuthenticationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Component;

@Component("registerDelegate")
@RequiredArgsConstructor
@Log4j2
@SkipAuthentication
public class UserRegistrationDelegate implements JavaDelegate {

    private final AuthenticationService authenticationService;

    @Override
    public void execute(DelegateExecution execution) throws Exception {
        log.info("Starting user registration process for execution: {}", execution.getId());

        // Get user data from process variables
        String email = (String) execution.getVariable("email");
        String password = (String) execution.getVariable("password");

        log.info("Registering user with email: {}", email);

        // Create AuthRequestDto from process variables
        AuthRequestDto userDto = new AuthRequestDto();
        userDto.setEmail(email);
        userDto.setPassword(password);

        authenticationService.register(userDto);
    }
} 