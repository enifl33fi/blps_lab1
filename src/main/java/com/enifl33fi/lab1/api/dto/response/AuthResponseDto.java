package com.enifl33fi.lab1.api.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Entity with access and refresh tokens")
public class AuthResponseDto {
    @Schema(description = "Access token of user")
    private String accessToken;
    @Schema(description = "Refresh token of user")
    private String refreshToken;
}
