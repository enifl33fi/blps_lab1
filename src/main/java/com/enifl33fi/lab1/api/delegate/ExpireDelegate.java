package com.enifl33fi.lab1.api.delegate;

import com.enifl33fi.lab1.api.service.SubscriptionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Component;

@Component("expireDelegate")
@RequiredArgsConstructor
@Log4j2
public class ExpireDelegate implements JavaDelegate {
    private final SubscriptionService subscriptionService;

    @Override
    public void execute(DelegateExecution execution) throws Exception {
        log.info("Starting subscription expiration check for execution: {}", execution.getId());
        subscriptionService.checkExpiredSubscriptions();
    }
}