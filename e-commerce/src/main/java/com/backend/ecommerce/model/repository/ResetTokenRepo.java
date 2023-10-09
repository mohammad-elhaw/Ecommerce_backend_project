package com.backend.ecommerce.model.repository;

import com.backend.ecommerce.model.LocalUser;
import com.backend.ecommerce.model.ResetToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ResetTokenRepo extends JpaRepository<ResetToken, Long> {
    Optional<ResetToken> findByUser(LocalUser user);
    Optional<ResetToken> findByResetToken(String token);
}
