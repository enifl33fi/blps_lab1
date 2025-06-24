package com.enifl33fi.lab1.api.delegate;

import com.enifl33fi.lab1.api.dto.request.AuthRequestDto;
import com.enifl33fi.lab1.api.exception.EmailNotUniqueException;
import com.enifl33fi.lab1.api.mapper.UserMapper;
import com.enifl33fi.lab1.api.model.user.User;
import com.enifl33fi.lab1.api.service.UserService;
import com.enifl33fi.lab1.api.service.ValidatingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Component;

@Component("registerDelegate")
@RequiredArgsConstructor
@Log4j2
public class UserRegistrationDelegate implements JavaDelegate {

    private final UserService userService;
    private final UserMapper userMapper;
    private final ValidatingService validatingService;

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
        
        // Validate user data
        validatingService.validateEntity(userDto);
        
        // Check if user is unique
        if (!userService.isEmailUnique(userDto.getEmail())) {
            throw new EmailNotUniqueException(userDto.getEmail());
        }
        
        // Create and save user
        User user = userMapper.mapUserFromAuthDto(userDto);
        user = userService.saveUser(user);
        
        // Store user in process variables for next steps
        execution.setVariable("userId", user.getId());
        execution.setVariable("userEmail", user.getEmail());
        
        log.info("User registration completed for user: {}", user.getEmail());
    }
} 