package com.backend.ecommerce.model.repository;

import com.backend.ecommerce.model.AccessToken;
import com.backend.ecommerce.model.LocalUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface AccessTokenRepo extends JpaRepository<AccessToken, Long> {
    Optional<AccessToken> findByUser(LocalUser user);
    Optional<AccessToken> findByAccessToken(String token);

    @Transactional
    void deleteByUser(LocalUser user);

}
