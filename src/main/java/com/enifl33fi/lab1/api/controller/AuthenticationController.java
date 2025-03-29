package com.enifl33fi.lab1.api.controller;

import com.enifl33fi.lab1.api.dto.request.AuthRequestDto;
import com.enifl33fi.lab1.api.dto.request.RefreshJwtRequestDto;
import com.enifl33fi.lab1.api.dto.response.AuthResponseDto;
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
    @ResponseBody
    public ResponseEntity<AuthResponseDto> register(@RequestBody AuthRequestDto userDto) {
        return ResponseEntity.ok(authenticationService.register(userDto));
    }

    @Operation(
            summary = "Login",
            description = "Allows to log in"
    )
    @PostMapping("/login")
    @ResponseBody
    public ResponseEntity<AuthResponseDto> login(@RequestBody AuthRequestDto userDto) {
        return ResponseEntity.ok(authenticationService.login(userDto));
    }

    @Operation(
            summary = "Confirm email",
            description = "Allows to confirm email by providing otp from message"
    )
    @PostMapping("/confirm/{otp}")
    @ResponseBody
    public ResponseEntity<AuthResponseDto> confirm(@PathVariable String otp) {
        return ResponseEntity.ok(authenticationService.confirmAccount(otp));
    }

    @Operation(
            summary = "Receiving accept and refresh token",
            description = "Allows to receive new accept and refresh token"
    )
    @PostMapping("/refresh")
    @ResponseBody
    public ResponseEntity<AuthResponseDto> getTokens(
            @RequestBody RefreshJwtRequestDto request) {
        return ResponseEntity.ok(authenticationService.getTokens(request.getRefreshToken()));
    }

    @Operation(
            summary = "Is email unique",
            description = "Allows to get information about uniqueness of email"
    )
    @GetMapping("/unique")
    @ResponseBody
    public ResponseEntity<Map<String, Boolean>> isUserUnique(
            @RequestParam("email") String email) {
        Boolean isUnique = authenticationService.isUserUnique(email);
        return ResponseEntity.ok(Collections.singletonMap("unique", isUnique));
    }
}
