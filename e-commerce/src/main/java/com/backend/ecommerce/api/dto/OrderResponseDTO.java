package com.backend.ecommerce.api.dto;

import com.backend.ecommerce.domain.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class OrderResponseDTO {

    private Long orderId;
    private List<OrderItemDTO> orderItems = new ArrayList<>();
    private LocalDateTime createdAt;
    private double totalPrice;
    private ShippedAddressDTO address;
    private OrderStatus orderStatus;
    private LocalDateTime deliveryDate;
}
