package com.enifl33fi.lab1.api.controller;

import com.enifl33fi.lab1.api.dto.request.AuthRequestDto;
import com.enifl33fi.lab1.api.dto.request.RefreshJwtRequestDto;
import com.enifl33fi.lab1.api.dto.response.AuthResponseDto;
import com.enifl33fi.lab1.api.service.AuthenticationService;
import com.enifl33fi.lab1.api.service.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthenticationController {
    private final AuthenticationService authenticationService;

    @PostMapping("/register")
    @ResponseBody
    public ResponseEntity<AuthResponseDto> register(@RequestBody AuthRequestDto userDto) {
       return ResponseEntity.ok(authenticationService.register(userDto));
    }

    @PostMapping("/login")
    @ResponseBody
    public ResponseEntity<AuthResponseDto> login(@RequestBody AuthRequestDto userDto) {
        return ResponseEntity.ok(authenticationService.login(userDto));
    }

    @PostMapping("/confirm/{otp}")
    @ResponseBody
    public ResponseEntity<AuthResponseDto> confirm(@PathVariable String otp) {
        return ResponseEntity.ok(authenticationService.confirmAccount(otp));
    }

    @PostMapping("/refresh")
    @ResponseBody
    public ResponseEntity<AuthResponseDto> getTokens(
            @RequestBody RefreshJwtRequestDto request) {
        return ResponseEntity.ok(authenticationService.getTokens(request.getRefreshToken()));
    }

    @GetMapping("/unique")
    @ResponseBody
    public ResponseEntity<Map<String, Boolean>> isUserUnique(
            @RequestParam("email") String email) {
        Boolean isUnique = authenticationService.isUserUnique(email);
        return ResponseEntity.ok(Collections.singletonMap("unique", isUnique));
    }
}
