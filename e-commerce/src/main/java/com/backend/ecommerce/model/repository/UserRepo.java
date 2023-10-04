package com.backend.ecommerce.model.repository;

import com.backend.ecommerce.model.LocalUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepo extends JpaRepository<LocalUser, Long> {
    Optional<LocalUser> findByEmailIgnoreCase(String email);
}
