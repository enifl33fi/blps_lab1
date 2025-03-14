package com.enifl33fi.lab1.api.dto.response;

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
public class OfferResponseDto {
    private Long id;
    private String name;
    private String description;
    private BigDecimal price;
    private boolean isSubscribed;
    private LocalDateTime endDate;
}
