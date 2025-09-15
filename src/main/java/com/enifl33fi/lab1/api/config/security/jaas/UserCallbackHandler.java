package com.enifl33fi.lab1.api.config.security.jaas;

import javax.security.auth.callback.*;

public class UserCallbackHandler implements CallbackHandler {
    private final String email;
    private final String password;

    public UserCallbackHandler(String email, String password) {
        this.email = email;
        this.password = password;
    }

    @Override
    public void handle(Callback[] callbacks) throws UnsupportedCallbackException {
        for (Callback callback : callbacks) {
            if (callback instanceof NameCallback nameCallback) {
                nameCallback.setName(email);
            } else if (callback instanceof PasswordCallback passwordCallback) {
                passwordCallback.setPassword(password.toCharArray());
            } else {
                throw new UnsupportedCallbackException(callback, "Unsupported callback");
            }
        }
    }
}

