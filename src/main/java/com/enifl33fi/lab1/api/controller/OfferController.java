package com.enifl33fi.lab1.api.controller;

import com.enifl33fi.lab1.api.model.offers.Offer;
import com.enifl33fi.lab1.api.service.OfferService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/offers")
@RequiredArgsConstructor
public class OfferController {
    private final OfferService offerService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Offer> addOffer(@RequestBody Offer offer) {
        return ResponseEntity.ok(offerService.addOffer(offer));
    }
} 