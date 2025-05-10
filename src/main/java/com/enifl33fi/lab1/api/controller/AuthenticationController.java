package com.enifl33fi.lab1.api.controller;

import com.enifl33fi.lab1.api.dto.request.AuthRequestDto;
import com.enifl33fi.lab1.api.service.AuthenticationService;
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
    public ResponseEntity<Void> register(@RequestBody AuthRequestDto userDto) {
        authenticationService.register(userDto);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/login")
    public ResponseEntity<Void> login(@RequestBody AuthRequestDto userDto) {
        authenticationService.login(userDto);
        return ResponseEntity.ok().build();
    }

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
