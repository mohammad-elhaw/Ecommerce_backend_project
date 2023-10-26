package com.backend.ecommerce.service.interfaces;

import com.backend.ecommerce.api.dto.CreateOrderDTO;
import com.backend.ecommerce.api.dto.OrderResponseDTO;
import com.backend.ecommerce.api.dto.PageOrderResponseDTO;
import com.backend.ecommerce.model.LocalUser;

import java.util.List;

public interface IOrderService {
    OrderResponseDTO createOrder(LocalUser user, CreateOrderDTO request);
    List<OrderResponseDTO> userOrderHistory(LocalUser user);
    OrderResponseDTO findOrderById(LocalUser user, Long orderId);
    OrderResponseDTO cancelOrder(LocalUser user, Long orderId);
    void deleteOrderById(LocalUser user, Long orderId);
    PageOrderResponseDTO getAllOrders(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder);
    OrderResponseDTO confirmOrder(Long orderId);
    OrderResponseDTO placeOrder(Long orderId);
    OrderResponseDTO shipOrder(Long orderId);
    OrderResponseDTO deliverOrder(Long orderId);
}
