package com.enifl33fi.lab1.api.controller;

import com.enifl33fi.lab1.api.dto.request.AuthRequestDto;
import com.enifl33fi.lab1.api.model.security.EmailOtp;
import com.enifl33fi.lab1.api.model.user.User;
import com.enifl33fi.lab1.api.repository.EmailOtpRepository;
import com.enifl33fi.lab1.api.service.AuthenticationService;
import com.enifl33fi.lab1.api.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.Map;
import java.util.Optional;
import java.util.HashMap;

@Tag(
        name = "Authentication controller",
        description = "Allows to register, log in, confirm email and update access and refresh keys"
)
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthenticationController {
    private final AuthenticationService authenticationService;
    private final EmailOtpRepository emailOtpRepository;
    private final UserService userService;

    @Operation(
            summary = "User registration",
            description = "Registration ONLY for users"
    )
    @PostMapping("/register")
    public ResponseEntity<Void> register(@RequestBody AuthRequestDto userDto) {
        authenticationService.register(userDto);
        return ResponseEntity.ok().build();
    }

    @Operation(
            summary = "Login",
            description = "Allows to log in"
    )
    @PostMapping("/login")
    public ResponseEntity<Void> login(@RequestBody AuthRequestDto userDto) {
        authenticationService.login(userDto);
        return ResponseEntity.ok().build();
    }

    @Operation(
            summary = "Confirm email",
            description = "Allows to confirm email by providing otp from message"
    )
    @PostMapping("/confirm/{otp}")
    public ResponseEntity<Void> confirm(@PathVariable String otp) {
        authenticationService.confirmAccount(otp);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/unique")
    public ResponseEntity<Map<String, Boolean>> isUserUnique(@RequestParam("email") String email) {
        Boolean isUnique = authenticationService.isUserUnique(email);
        return ResponseEntity.ok(Collections.singletonMap("unique", isUnique));
    }

    @GetMapping("/otp/{email}")
    public ResponseEntity<Map<String, Object>> getOtpForEmail(@PathVariable String email) {
        try {
            // Find user by email first
            User user = userService.loadUserByUsername(email);
            // Then find OTP for this user
            Optional<EmailOtp> emailOtp = emailOtpRepository.findByUser(user);
            
            Map<String, Object> response = new HashMap<>();
            if (emailOtp.isPresent()) {
                response.put("otp", emailOtp.get().getConfirmationToken());
                response.put("email", email);
                response.put("message", "OTP found");
            } else {
                response.put("message", "No OTP found for this email");
            }
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }
}
