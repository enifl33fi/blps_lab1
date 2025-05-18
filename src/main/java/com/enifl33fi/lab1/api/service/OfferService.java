package com.enifl33fi.lab1.api.service;

import com.enifl33fi.lab1.api.model.offers.Offer;
import com.enifl33fi.lab1.api.repository.OfferRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OfferService {
    private final OfferRepository offerRepository;

    public Offer addOffer(Offer offer) {
        return offerRepository.save(offer);
    }
} 