package com.enifl33fi.lab1.api.config.security.jaas;

import lombok.extern.log4j.Log4j2;
import org.springframework.context.ApplicationContext;
import javax.security.auth.Subject;
import javax.security.auth.callback.*;
import javax.security.auth.login.*;
import javax.security.auth.spi.LoginModule;
import java.util.Map;

@Log4j2
public class UserLoginModule implements LoginModule {

    private Subject subject;
    private CallbackHandler callbackHandler;
    private String email;
    private String password;
    private boolean succeeded = false;
    private com.enifl33fi.lab1.api.model.user.User authenticatedUser;

    @Override
    public void initialize(Subject subject, CallbackHandler callbackHandler,
                           Map<String, ?> sharedState, Map<String, ?> options) {
        this.subject = subject;
        this.callbackHandler = callbackHandler;
    }

    @Override
    public boolean login() throws LoginException {
        NameCallback nameCallback = new NameCallback("email");
        PasswordCallback passwordCallback = new PasswordCallback("password", false);

        try {
            callbackHandler.handle(new Callback[]{nameCallback, passwordCallback});
            email = nameCallback.getName();
            password = new String(passwordCallback.getPassword());

            ApplicationContext ctx = JaasSpringContext.getApplicationContext();
            var userService = ctx.getBean(com.enifl33fi.lab1.api.service.UserService.class);
            var encoder = ctx.getBean(org.springframework.security.crypto.password.PasswordEncoder.class);

            var user = userService.loadUserByUsername(email);

            if (!com.enifl33fi.lab1.api.model.user.Role.USER.equals(user.getRole())) {
                throw new FailedLoginException("Email not confirmed. Current role: " + user.getRole());
            }

            if (!encoder.matches(password, user.getPassword())) {
                throw new FailedLoginException("Invalid credentials");
            }

            this.authenticatedUser = user;
            this.succeeded = true;
            return true;

        } catch (Exception e) {
            log.error("Login failed", e);
            throw new LoginException("Login failed: " + e.getMessage());
        }
    }

    @Override
    public boolean commit() throws LoginException {
        if (!succeeded) return false;

        subject.getPrincipals().add(new JaasPrincipal(email));
        subject.getPrincipals().add(new RolePrincipal(authenticatedUser.getRole().name()));
        return true;
    }

    @Override
    public boolean abort() {
        clearState();
        return true;
    }

    @Override
    public boolean logout() throws LoginException {
        subject.getPrincipals().clear();
        clearState();
        return true;
    }

    private void clearState() {
        email = null;
        password = null;
        authenticatedUser = null;
        succeeded = false;
    }
}
