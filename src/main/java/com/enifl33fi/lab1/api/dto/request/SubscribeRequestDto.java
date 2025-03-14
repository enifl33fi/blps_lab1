package com.enifl33fi.lab1.api.dto.request;

import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SubscribeRequestDto {
    @Min(value = 1, message = "Duration must be at least 1 month")
    private int durationMonths;
}
