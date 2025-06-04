package com.enifl33fi.lab1.api.service;

import com.enifl33fi.lab1.api.config.ytsaurus.YtsaurusConnection;
import com.enifl33fi.lab1.api.config.ytsaurus.YtsaurusConnectionFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.Instant;

@Service
@RequiredArgsConstructor
public class StatisticService {
    private final YtsaurusConnectionFactory ytsaurusConnectionFactory;

    @Async
    public void saveSubscriptionStatistic(String subscriptionName, String userEmail) {
        YtsaurusConnection connection = this.ytsaurusConnectionFactory.getConnection();
        Timestamp timestamp = Timestamp.from(Instant.now());
        long time = timestamp.getTime() * 1000 + timestamp.getNanos() / 1000;

        connection.addStatisticRow(time, subscriptionName, userEmail);
        connection.close();
    }
}
