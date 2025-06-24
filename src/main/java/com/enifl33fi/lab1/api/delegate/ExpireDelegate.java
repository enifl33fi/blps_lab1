package com.enifl33fi.lab1.api.delegate;

import com.enifl33fi.lab1.api.model.offers.Subscription;
import com.enifl33fi.lab1.api.repository.SubscriptionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component("expireDelegate")
@RequiredArgsConstructor
@Log4j2
public class ExpireDelegate implements JavaDelegate {

    private final SubscriptionRepository subscriptionRepository;

    @Override
    public void execute(DelegateExecution execution) throws Exception {
        log.info("Starting subscription expiration check for execution: {}", execution.getId());
        
        // Get all active subscriptions that have expired
        List<Subscription> expiredSubscriptions = subscriptionRepository.findByEndDateBefore(LocalDateTime.now());
        
        // Mark expired subscriptions as inactive (you might want to add an 'active' field to Subscription)
        for (Subscription subscription : expiredSubscriptions) {
            log.info("Marking subscription as expired: user={}, offer={}, endDate={}", 
                    subscription.getUser().getEmail(), 
                    subscription.getOffer().getName(), 
                    subscription.getEndDate());
            
            // Here you would typically update the subscription status
            // For now, we'll just log it
        }
        
        log.info("Expiration check completed. Found {} expired subscriptions", expiredSubscriptions.size());
    }
} 