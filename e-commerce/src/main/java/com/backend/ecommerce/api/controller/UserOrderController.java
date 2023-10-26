package com.backend.ecommerce.api.controller;

import com.backend.ecommerce.api.dto.CreateOrderDTO;
import com.backend.ecommerce.api.dto.OrderResponseDTO;
import com.backend.ecommerce.api.dto.SuccessMessage;
import com.backend.ecommerce.model.LocalUser;
import com.backend.ecommerce.service.interfaces.IOrderService;
import com.backend.ecommerce.service.interfaces.IUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/user/order")
@RequiredArgsConstructor
public class UserOrderController {

    private final IUserService userService;
    private final IOrderService orderService;

    @PostMapping("/")
    public ResponseEntity<?> createOrderHandler(@RequestBody CreateOrderDTO request){

        LocalUser user = userService.getAuthenticatedUser();
        OrderResponseDTO order = orderService.createOrder(user, request);
        return new ResponseEntity<>(order, HttpStatus.CREATED);
    }

    @PutMapping("/{orderId}/cancel")
    public ResponseEntity<?> cancelOrderHandler(@PathVariable Long orderId){
        LocalUser user = userService.getAuthenticatedUser();
        OrderResponseDTO order = orderService.cancelOrder(user, orderId);
        return new ResponseEntity<>(order, HttpStatus.ACCEPTED);
    }

    @GetMapping("/history")
    public ResponseEntity<?> userOrderHistoryHandler(){
        LocalUser user = userService.getAuthenticatedUser();
        List<OrderResponseDTO> orders = orderService.userOrderHistory(user);
        return new ResponseEntity<>(orders, HttpStatus.ACCEPTED);
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<?> findOrderHandler(@PathVariable Long orderId){
        LocalUser user = userService.getAuthenticatedUser();
        OrderResponseDTO order = orderService.findOrderById(user, orderId);
        return new ResponseEntity<>(order, HttpStatus.ACCEPTED);
    }

    @DeleteMapping("/{orderId}/delete")
    public ResponseEntity<?> deleteOrderHandler(@PathVariable Long orderId){
        LocalUser user = userService.getAuthenticatedUser();
        orderService.deleteOrderById(user, orderId);
        return ResponseEntity.status(HttpStatus.ACCEPTED)
                .body(new SuccessMessage(
                        HttpStatus.ACCEPTED.value(),
                        new Date(),
                        "Order Deleted Successfully."
                ));
    }

}
