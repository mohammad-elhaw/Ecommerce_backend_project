package com.backend.ecommerce.model.repository;

import com.backend.ecommerce.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepo extends JpaRepository<Order, Long> {
    @Query("SELECT o FROM Order o WHERE o.user.id = :userId AND(o.orderStatus=PLACED OR o.orderStatus=CONFIRMED OR o.orderStatus=SHIPPED OR o.orderStatus=DELIVERED)")
    List<Order> getUserOrders(Long userId);
    @Query("SELECT o FROM Order o WHERE o.user.id = ?1 AND o.id = ?2")
    Optional<Order> findOrderByUserIdAndOrderId(Long userId, Long orderId);
}
