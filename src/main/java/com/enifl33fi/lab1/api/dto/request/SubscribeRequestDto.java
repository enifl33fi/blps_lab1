package com.enifl33fi.lab1.api.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Entity with info about requested subscription")
public class SubscribeRequestDto {
    @Schema(description = "Duration of requested subscription")
    @Min(value = 1, message = "Duration must be at least 1 month")
    private int durationMonths;
}
