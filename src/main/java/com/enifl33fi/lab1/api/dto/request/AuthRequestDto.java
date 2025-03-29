package com.enifl33fi.lab1.api.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Entity with user credentials")
public class AuthRequestDto {
    @Schema(description = "User's unique email", example = "enifl33fi@mail.ru")
    @Email
    @NotBlank
    private String email;

    @Schema(description = "User's password", example = "some hashed string")
    @NotBlank
    private String password;
}
