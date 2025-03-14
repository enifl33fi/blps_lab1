package com.enifl33fi.lab1.api.repository;

import com.enifl33fi.lab1.api.model.security.EmailOtp;
import com.enifl33fi.lab1.api.model.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EmailOtpRepository extends JpaRepository<EmailOtp, Long> {
    Optional<EmailOtp> findByConfirmationToken(String token);

    void deleteByConfirmationToken(String token);

    Optional<EmailOtp> findByUser(User user);
}
