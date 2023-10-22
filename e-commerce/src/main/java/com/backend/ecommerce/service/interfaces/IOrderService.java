package com.backend.ecommerce.service.interfaces;

import com.backend.ecommerce.api.dto.CreateOrderDTO;
import com.backend.ecommerce.model.LocalUser;
import com.backend.ecommerce.model.Order;

public interface IOrderService {
    Order createOrder(LocalUser user, CreateOrderDTO request);

}
