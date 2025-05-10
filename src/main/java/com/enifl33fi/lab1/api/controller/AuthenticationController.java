package com.enifl33fi.lab1.api.controller;

import com.enifl33fi.lab1.api.dto.request.AuthRequestDto;
import com.enifl33fi.lab1.api.service.AuthenticationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.Map;

@Tag(
        name = "Authentication controller",
        description = "Allows to register, log in, confirm email and update access and refresh keys"
)
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthenticationController {
    private final AuthenticationService authenticationService;

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
}
