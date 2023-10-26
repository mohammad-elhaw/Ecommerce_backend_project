package com.backend.ecommerce.api.controller;

import com.backend.ecommerce.api.config.AppConstants;
import com.backend.ecommerce.api.dto.OrderResponseDTO;
import com.backend.ecommerce.api.dto.PageOrderResponseDTO;
import com.backend.ecommerce.service.interfaces.IOrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/orders")
@RequiredArgsConstructor
public class AdminOrderController {

    private final IOrderService orderService;

    @GetMapping("/")
    public ResponseEntity<?> getAllOrdersHandler(
            @RequestParam(name = "pageNumber", defaultValue = AppConstants.PAGE_NUMBER, required = false) Integer pageNumber,
            @RequestParam(name = "pageSize", defaultValue = AppConstants.PAGE_SIZE, required = false) Integer pageSize,
            @RequestParam(name = "sortBy", defaultValue = AppConstants.SORT_ORDERS_BY, required = false) String sortBy,
            @RequestParam(name = "sortOrder", defaultValue = AppConstants.SORT_DIR, required = false) String sortOrder){

        PageOrderResponseDTO orders = orderService.getAllOrders(pageNumber, pageSize, sortBy, sortOrder);
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
