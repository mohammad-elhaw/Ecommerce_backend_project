package com.backend.ecommerce.api.controller;

import com.backend.ecommerce.api.dto.OrderResponseDTO;
import com.backend.ecommerce.service.interfaces.IOrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/orders")
@RequiredArgsConstructor
public class AdminOrderController {

    private final IOrderService orderService;

    @GetMapping("/")
    public ResponseEntity<?> getAllOrdersHandler(){
        List<OrderResponseDTO> orders = orderService.getAllOrders();
        return new ResponseEntity<>(orders, HttpStatus.ACCEPTED);
    }

    @PutMapping("/{orderId}/confirm")
    public ResponseEntity<?> confirmOrderHandler(@PathVariable Long orderId){
        OrderResponseDTO order = orderService.confirmOrder(orderId);
        return new ResponseEntity<>(order, HttpStatus.ACCEPTED);
    }

    @PutMapping("/{orderId}/place")
    public ResponseEntity<?> placeOrderHandler(@PathVariable Long orderId){
        OrderResponseDTO order = orderService.placeOrder(orderId);
        return new ResponseEntity<>(order, HttpStatus.ACCEPTED);
    }

    @PutMapping("/{orderId}/ship")
    public ResponseEntity<?> shipOrderHandler(@PathVariable Long orderId){
        OrderResponseDTO order = orderService.shipOrder(orderId);
        return new ResponseEntity<>(order, HttpStatus.ACCEPTED);
    }

    @PutMapping("/{orderId}/deliver")
    public ResponseEntity<?> deliverOrderHandler(@PathVariable Long orderId){
        OrderResponseDTO order = orderService.deliverOrder(orderId);
        return new ResponseEntity<>(order, HttpStatus.ACCEPTED);
    }


}
