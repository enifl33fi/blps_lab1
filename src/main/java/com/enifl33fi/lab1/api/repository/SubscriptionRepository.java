package com.enifl33fi.lab1.api.repository;

import com.enifl33fi.lab1.api.model.offers.Offer;
import com.enifl33fi.lab1.api.model.offers.Subscription;
import com.enifl33fi.lab1.api.model.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SubscriptionRepository extends JpaRepository<Subscription, Long> {
    List<Subscription> findByUser(User user);

    Optional<Subscription> findByUserAndOffer(User user, Offer offer);
}
