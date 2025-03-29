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
@Schema(description = "Entity with info about error")
public class ErrorResponseDto {
    private int status;
    private String statusText;
    private String message;
}
