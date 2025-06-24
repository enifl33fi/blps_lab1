package com.enifl33fi.lab1.api.delegate;

import com.enifl33fi.lab1.api.dto.request.AuthRequestDto;
import com.enifl33fi.lab1.api.service.AuthenticationService;
import com.enifl33fi.lab1.api.service.ValidatingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Component;

@Component("loginDelegate")
@RequiredArgsConstructor
@Log4j2
public class LoginDelegate implements JavaDelegate {

    private final AuthenticationService authenticationService;
    private final ValidatingService validatingService;

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
        
        // Validate user data
        validatingService.validateEntity(userDto);
        
        // Perform login
        authenticationService.login(userDto);
        
        // Store user info in process variables
        execution.setVariable("userEmail", email);
        
        log.info("Login completed for user: {}", email);
    }
} 