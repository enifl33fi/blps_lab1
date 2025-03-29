package com.enifl33fi.lab1.api.controller;

import com.enifl33fi.lab1.api.dto.response.AuthResponseDto;
import com.enifl33fi.lab1.api.model.user.User;
import com.enifl33fi.lab1.api.service.EmailService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@Tag(
        name = "Email controller",
        description = "Allows to send email with otp"
)
@RestController
@RequestMapping("/api/mail")
@RequiredArgsConstructor
public class EmailController {
    private final EmailService emailService;

    @Operation(
            summary = "Send otp",
            description = "Allows to send otp for email confirmation"
    )
    @PostMapping("/send")
    @ResponseBody
    public ResponseEntity<AuthResponseDto> sendOtp(@AuthenticationPrincipal User user) {
        emailService.sendEmail(user);

        return ResponseEntity.ok().build();
    }
}
