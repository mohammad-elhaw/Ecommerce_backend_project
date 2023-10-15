package com.backend.ecommerce.model.repository;

import com.backend.ecommerce.model.Cart;
import com.backend.ecommerce.model.LocalUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CartRepo extends JpaRepository<Cart, Long> {
    Optional<Cart> findByUser(LocalUser user);
}
