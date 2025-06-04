package com.enifl33fi.lab1.api.repository;

import com.enifl33fi.lab1.api.model.offers.Offer;
import com.enifl33fi.lab1.api.model.offers.Subscription;
import com.enifl33fi.lab1.api.model.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface SubscriptionRepository extends JpaRepository<Subscription, Long> {
    @Query("SELECT s FROM Subscription s WHERE s.endDate < :currentDate AND s.status = 'ACTIVE'")
    List<Subscription> findExpiredActiveSubscriptions(@Param("currentDate") LocalDateTime currentDate);

    List<Subscription> findByUser(User user);

    Optional<Subscription> findByUserAndOffer(User user, Offer offer);
}
