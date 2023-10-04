package com.backend.ecommerce.model.repository;

import com.backend.ecommerce.model.LocalUser;
import com.backend.ecommerce.model.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RefreshTokenRepo extends JpaRepository<RefreshToken, Long> {
    Optional<RefreshToken> findByRefreshToken(String token);

    @Modifying
    int deleteByUser(LocalUser user);
}
