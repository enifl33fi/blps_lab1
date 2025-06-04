package com.enifl33fi.lab1.api.config.ytsaurus;

import jakarta.resource.spi.ConnectionManager;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class YtsaurusConnectionFactory {
    private final YtsaurusManagedConnectionFactory ytsaurusManagedConnectionFactory;
    private final ConnectionManager connectionManager;

    public YtsaurusConnection getConnection() {
        return new YtsaurusConnection(ytsaurusManagedConnectionFactory.getEndpoint(), ytsaurusManagedConnectionFactory.getToken());
    }

    public void closeConnection(YtsaurusConnection connection) {
        connection.close();
    }
}
