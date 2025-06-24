package com.enifl33fi.lab1.api.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class DelegateAuthenticationAspect {

    private final AuthenticationManager authenticationManager;

    public DelegateAuthenticationAspect(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    @Around("execution(* com.enifl33fi.lab1.api.delegate.*.execute(..)) && args(execution) && !@within(com.enifl33fi.lab1.api.annotation.SkipAuthentication)")
    public Object authenticateDelegate(ProceedingJoinPoint joinPoint, DelegateExecution execution) throws Throwable {
        String email = (String) execution.getVariable("email");
        String password = (String) execution.getVariable("password");

        Authentication previousAuth = SecurityContextHolder.getContext().getAuthentication();

        try {
            if (email != null && password != null) {
                System.out.println(email);
                Authentication auth = new UsernamePasswordAuthenticationToken(email, password);
                Authentication authenticated = authenticationManager.authenticate(auth);
                SecurityContextHolder.getContext().setAuthentication(authenticated);
            }
            return joinPoint.proceed();
        } finally {
            SecurityContextHolder.getContext().setAuthentication(previousAuth);
        }
    }
}
