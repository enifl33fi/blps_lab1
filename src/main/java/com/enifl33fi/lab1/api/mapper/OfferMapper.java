package com.enifl33fi.lab1.api.mapper;

import com.enifl33fi.lab1.api.dto.response.OfferResponseDto;
import com.enifl33fi.lab1.api.model.offers.Offer;
import com.enifl33fi.lab1.api.model.offers.Subscription;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OfferMapper {
    public OfferResponseDto mapToOfferResponseDto(Offer offer, Optional<Subscription> subscriptionOpt) {
        return OfferResponseDto.builder()
                .id(offer.getId())
                .description(offer.getDescription())
                .price(offer.getPrice())
                .name(offer.getName())
                .isSubscribed(subscriptionOpt.isPresent())
                .endDate(subscriptionOpt.map(Subscription::getEndDate).orElse(null))
                .build();
    }
}
