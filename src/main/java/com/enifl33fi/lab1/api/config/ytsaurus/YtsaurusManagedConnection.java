package com.enifl33fi.lab1.api.config.ytsaurus;

import jakarta.resource.ResourceException;
import jakarta.resource.spi.*;

import javax.security.auth.Subject;
import javax.transaction.xa.XAResource;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

public class YtsaurusManagedConnection implements ManagedConnection {
    private YtsaurusConnection connection;
    private List<ConnectionEventListener> connectionEventListeners = new ArrayList<>();

    public YtsaurusManagedConnection(String endpoint, String token) {
        this.connection = new YtsaurusConnection(endpoint, token);
    }

    @Override
    public Object getConnection(Subject subject, ConnectionRequestInfo cxRequestInfo) {
        return this.connection;
    }

    @Override
    public void addConnectionEventListener(ConnectionEventListener listener) {
        this.connectionEventListeners.add(listener);
    }

    @Override
    public void removeConnectionEventListener(ConnectionEventListener listener) {
        this.connectionEventListeners.remove(listener);
    }

    @Override
    public XAResource getXAResource() {
        return null;
    }

    @Override
    public LocalTransaction getLocalTransaction() throws ResourceException {
        return null;
    }

    @Override
    public ManagedConnectionMetaData getMetaData() {
        return null;
    }

    @Override
    public PrintWriter getLogWriter() {
        return null;
    }

    @Override
    public void setLogWriter(PrintWriter out) {
    }

    @Override
    public void destroy() {
    }

    @Override
    public void cleanup() {
    }

    @Override
    public void associateConnection(Object connection) {
    }
}
