package com.enifl33fi.lab1.api.controller;

import com.enifl33fi.lab1.api.dto.request.SubscribeRequestDto;
import com.enifl33fi.lab1.api.dto.response.OfferResponseDto;
import com.enifl33fi.lab1.api.model.user.User;
import com.enifl33fi.lab1.api.service.SubscriptionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/subscriptions")
@RequiredArgsConstructor
public class SubscriptionController {
    private final SubscriptionService subscriptionService;

    @GetMapping("/")
    public ResponseEntity<List<OfferResponseDto>> getAllSubscriptions(@AuthenticationPrincipal User user) {
        return ResponseEntity.ok(subscriptionService.getOffers(user));
    }

    @GetMapping("/{id}")
    public ResponseEntity<OfferResponseDto> getSubscriptionById(@AuthenticationPrincipal User user, @PathVariable Long id) {
        return ResponseEntity.ok(subscriptionService.getOfferById(id, user));
    }

    @PostMapping("/{id}/get")
    public ResponseEntity<?> subscribe(@AuthenticationPrincipal User user, @PathVariable Long id, @RequestBody SubscribeRequestDto request) {
        subscriptionService.subscribeToOffer(id, user, request);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}/delete")
    public ResponseEntity<?> unsubscribe(@AuthenticationPrincipal User user, @PathVariable Long id) {
        subscriptionService.unsubscribeFromOffer(id, user);
        return ResponseEntity.ok().build();
    }
}
