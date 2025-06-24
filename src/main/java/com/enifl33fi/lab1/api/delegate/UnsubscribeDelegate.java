package com.enifl33fi.lab1.api.delegate;

import com.enifl33fi.lab1.api.service.SubscriptionService;
import com.enifl33fi.lab1.api.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Component;

@Component("unsubscribeDelegate")
@RequiredArgsConstructor
@Log4j2
public class UnsubscribeDelegate implements JavaDelegate {

    private final SubscriptionService subscriptionService;
    private final UserService userService;

    @Override
    public void execute(DelegateExecution execution) throws Exception {
        log.info("Starting unsubscription process for execution: {}", execution.getId());

        // Get form data from process variables
        Integer offerId = (Integer) execution.getVariable("id");
        String userEmail = (String) execution.getVariable("email");

        // Get user from database
        var user = userService.loadUserByUsername(userEmail);

        // Unsubscribe from offer
        subscriptionService.unsubscribeFromOffer(offerId.longValue(), user);

        log.info("Unsubscription completed for user: {} from offer: {}", userEmail, offerId);
    }
} 