package com.enifl33fi.lab1.api.config.ytsaurus;


import jakarta.resource.spi.ConnectionManager;
import jakarta.resource.spi.ConnectionRequestInfo;
import jakarta.resource.spi.ManagedConnection;
import jakarta.resource.spi.ManagedConnectionFactory;
import lombok.Data;

import javax.security.auth.Subject;
import java.io.PrintWriter;
import java.util.Set;

@Data
public class YtsaurusManagedConnectionFactory implements ManagedConnectionFactory {
    private String token;
    private String endpoint;

    @Override
    public Object createConnectionFactory(ConnectionManager connectionManager) {
        return new YtsaurusConnectionFactory(this, connectionManager);
    }

    @Override
    public Object createConnectionFactory() {
        return new YtsaurusConnectionFactory(this, null);
    }

    @Override
    public ManagedConnection createManagedConnection(Subject subject, ConnectionRequestInfo info) {
        return new YtsaurusManagedConnection(endpoint, token);
    }

    @Override
    public ManagedConnection matchManagedConnections(Set set, Subject subject, ConnectionRequestInfo connectionRequestInfo) {
        return null;
    }

    @Override
    public PrintWriter getLogWriter() {
        return null;
    }

    @Override
    public void setLogWriter(PrintWriter out) {
    }
}
