package com.enifl33fi.lab1.api.controller;

import com.enifl33fi.lab1.api.dto.response.AuthResponseDto;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/test")
public class TestController {
    @GetMapping("/")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<AuthResponseDto> sendOtp() {
        return ResponseEntity.ok().build();
    }
}

