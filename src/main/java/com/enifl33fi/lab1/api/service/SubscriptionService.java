package com.enifl33fi.lab1.api.service;

import com.enifl33fi.lab1.api.dto.request.SubscribeRequestDto;
import com.enifl33fi.lab1.api.dto.response.OfferResponseDto;
import com.enifl33fi.lab1.api.exception.NotFoundException;
import com.enifl33fi.lab1.api.mapper.OfferMapper;
import com.enifl33fi.lab1.api.model.offers.Offer;
import com.enifl33fi.lab1.api.model.offers.Subscription;
import com.enifl33fi.lab1.api.model.user.User;
import com.enifl33fi.lab1.api.repository.OfferRepository;
import com.enifl33fi.lab1.api.repository.SubscriptionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SubscriptionService {
    private final SubscriptionRepository subscriptionRepository;
    private final OfferRepository offerRepository;
    private final OfferMapper offerMapper;
    private final ValidatingService validatingService;

    public List<OfferResponseDto> getOffers(User user) {
        List<Offer> offers = offerRepository.findAll();
        List<Subscription> subscriptions = subscriptionRepository.findByUser(user);

        return offers.stream()
                .map(offer -> {
                    Optional<Subscription> sub = subscriptions.stream()
                            .filter(s -> s.getOffer().getId().equals(offer.getId()))
                            .findFirst();

                    return offerMapper.mapToOfferResponseDto(offer, sub);
                })
                .collect(Collectors.toList());
    }

    public OfferResponseDto getOfferById(Long id, User user) {
        Offer offer = offerRepository.findById(id).orElseThrow(() -> new NotFoundException("Offer"));
        Optional<Subscription> subscriptionOpt = subscriptionRepository.findByUserAndOffer(user, offer);

        return offerMapper.mapToOfferResponseDto(offer, subscriptionOpt);
    }

    public void subscribeToOffer(Long id, User user, SubscribeRequestDto request) {
        validatingService.validateEntity(request);

        Offer offer = offerRepository.findById(id).orElseThrow(() -> new NotFoundException("Offer"));
        Optional<Subscription> subscriptionOpt = subscriptionRepository.findByUserAndOffer(user, offer);

        if (subscriptionOpt.isPresent()) {
            throw new IllegalStateException("User already subscribed to this offer");
        }

        LocalDateTime now = LocalDateTime.now();
        Subscription subscription = Subscription.builder()
                .user(user)
                .offer(offer)
                .startDate(now)
                .endDate(now.plusMonths(request.getDurationMonths()))
                .build();

        subscriptionRepository.save(subscription);
    }

    public void unsubscribeFromOffer(Long id, User user) {
        Offer offer = offerRepository.findById(id).orElseThrow(() -> new NotFoundException("Offer"));
        Subscription subscription = subscriptionRepository.findByUserAndOffer(user, offer).orElseThrow(() -> new NotFoundException("Subscription"));

        subscriptionRepository.delete(subscription);
    }
}
