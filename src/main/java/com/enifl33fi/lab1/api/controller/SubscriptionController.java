package com.enifl33fi.lab1.api.controller;

import com.enifl33fi.lab1.api.dto.request.SubscribeRequestDto;
import com.enifl33fi.lab1.api.dto.response.OfferResponseDto;
import com.enifl33fi.lab1.api.model.user.User;
import com.enifl33fi.lab1.api.service.SubscriptionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(
        name = "Subscription controller",
        description = "Allows to get, subscribe and unsubscribe"
)
@RestController
@RequestMapping("/api/subscriptions")
@RequiredArgsConstructor
public class SubscriptionController {
    private final SubscriptionService subscriptionService;

    @Operation(
            summary = "Get all offers",
            description = "Allows to get information about all offers including info about subscription"
    )
    @GetMapping("/")
    public ResponseEntity<List<OfferResponseDto>> getAllSubscriptions(@AuthenticationPrincipal User user) {
        return ResponseEntity.ok(subscriptionService.getOffers(user));
    }

    @Operation(
            summary = "Get info about single offer",
            description = "Allows to get information about single offer including info about subscription"
    )
    @GetMapping("/{id}")
    public ResponseEntity<OfferResponseDto> getSubscriptionById(@AuthenticationPrincipal User user, @PathVariable Long id) {
        return ResponseEntity.ok(subscriptionService.getOfferById(id, user));
    }

    @Operation(
            summary = "Subscribe to offer",
            description = "Allows to subscribe to offer"
    )
    @PostMapping("/{id}")
    public ResponseEntity<?> subscribe(@AuthenticationPrincipal User user, @PathVariable Long id, @RequestBody SubscribeRequestDto request) {
        subscriptionService.subscribeToOffer(id, user, request);
        return ResponseEntity.ok().build();
    }

    @Operation(
            summary = "Unsubscribe from offer",
            description = "Allows to unsubscribe from offer"
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<?> unsubscribe(@AuthenticationPrincipal User user, @PathVariable Long id) {
        subscriptionService.unsubscribeFromOffer(id, user);
        return ResponseEntity.ok().build();
    }
}
