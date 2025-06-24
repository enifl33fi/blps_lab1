package com.enifl33fi.lab1.api.delegate;

import com.enifl33fi.lab1.api.dto.request.SubscribeRequestDto;
import com.enifl33fi.lab1.api.service.SubscriptionService;
import com.enifl33fi.lab1.api.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Component;

@Component("subscribeDelegate")
@RequiredArgsConstructor
@Log4j2
public class SubscribeDelegate implements JavaDelegate {

    private final SubscriptionService subscriptionService;
    private final UserService userService;

    @Override
    public void execute(DelegateExecution execution) throws Exception {
        log.info("Starting subscription process for execution: {}", execution.getId());

        // Get form data from process variables
        Integer offerId = (Integer) execution.getVariable("id");
        Integer durationMonths = (Integer) execution.getVariable("durationInMonths");
        String userEmail = (String) execution.getVariable("email");

        // Get user from database
        var user = userService.loadUserByUsername(userEmail);

        // Create subscription request
        SubscribeRequestDto request = SubscribeRequestDto.builder()
                .durationMonths(durationMonths)
                .build();

        // Subscribe to offer
        subscriptionService.subscribeToOffer(offerId.longValue(), user, request);

        log.info("Subscription completed for user: {} to offer: {}", userEmail, offerId);
    }
} 