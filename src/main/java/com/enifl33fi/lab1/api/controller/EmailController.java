package com.enifl33fi.lab1.api.controller;

import com.enifl33fi.lab1.api.dto.response.AuthResponseDto;
import com.enifl33fi.lab1.api.model.user.User;
import com.enifl33fi.lab1.api.service.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/mail")
@RequiredArgsConstructor
public class EmailController {
    private final EmailService emailService;

    @PostMapping("/send")
    @ResponseBody
    public ResponseEntity<AuthResponseDto> sendOtp(@AuthenticationPrincipal User user) {
        emailService.sendEmail(user);

        return ResponseEntity.ok().build();
    }
}
