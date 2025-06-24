package com.enifl33fi.lab1.api.delegate;

import com.enifl33fi.lab1.api.service.AuthenticationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Component;

@Component("confirmAccountDelegate")
@RequiredArgsConstructor
@Log4j2
public class ConfirmAccountDelegate implements JavaDelegate {

    private final AuthenticationService authenticationService;

    @Override
    public void execute(DelegateExecution execution) throws Exception {
        log.info("Starting account confirmation process for execution: {}", execution.getId());
        
        // Get OTP from process variables (from form)
        String otp = (String) execution.getVariable("otp");
        
        // Confirm account
        authenticationService.confirmAccount(otp);
        
        log.info("Account confirmed successfully with OTP: {}", otp);
    }
} 