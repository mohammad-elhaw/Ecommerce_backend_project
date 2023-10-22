package com.backend.ecommerce.model.repository;

import com.backend.ecommerce.model.Address;
import com.backend.ecommerce.model.LocalUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AddressRepo extends JpaRepository<Address, Long> {
    Optional<Address> findByUser(LocalUser user);
}
