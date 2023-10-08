package com.backend.ecommerce.model.repository;

import com.backend.ecommerce.model.Inventory;
import com.backend.ecommerce.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface InventoryRepo extends JpaRepository<Inventory, Long> {
}
