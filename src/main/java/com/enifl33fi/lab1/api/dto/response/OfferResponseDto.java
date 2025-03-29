package com.enifl33fi.lab1.api.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Entity with info about offer including subscription")
public class OfferResponseDto {
    private Long id;
    private String name;
    private String description;
    private BigDecimal price;
    private boolean isSubscribed;
    private LocalDateTime endDate;
}
