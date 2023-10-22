package com.backend.ecommerce.api.controller;

import com.backend.ecommerce.api.dto.CreateOrderDTO;
import com.backend.ecommerce.model.LocalUser;
import com.backend.ecommerce.model.Order;
import com.backend.ecommerce.service.interfaces.IOrderService;
import com.backend.ecommerce.service.interfaces.IUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserOrderController {

    private final IUserService userService;
    private final IOrderService orderService;

    @PostMapping("/order")
    public ResponseEntity<?> createOrderHandler(@RequestBody CreateOrderDTO request){

        LocalUser user = userService.getAuthenticatedUser();
        Order order = orderService.createOrder(user, request);
        return new ResponseEntity<>(order, HttpStatus.CREATED);
    }

}
