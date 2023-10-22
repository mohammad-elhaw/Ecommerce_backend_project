package com.backend.ecommerce.service;

import com.backend.ecommerce.api.dto.CreateOrderDTO;
import com.backend.ecommerce.domain.OrderStatus;
import com.backend.ecommerce.exception.APIException;
import com.backend.ecommerce.exception.ResourceNotFoundException;
import com.backend.ecommerce.model.*;
import com.backend.ecommerce.model.repository.*;
import com.backend.ecommerce.service.interfaces.IOrderService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
public class OrderService implements IOrderService {

    private final AddressRepo addressRepo;
    private final UserRepo userRepo;
    private final CartRepo cartRepo;
    private final OrderItemRepo orderItemRepo;
    private final OrderRepo orderRepo;

    @SneakyThrows
    @Override
    @Transactional
    public Order createOrder(LocalUser user, CreateOrderDTO request) {
        Optional<Address> savedAddress = addressRepo.findByUser(user);
        if(savedAddress.isPresent()){
            createAndUpdateAddress(savedAddress.get(), request, user);
        }
        else{
            Address shippedAddress = new Address();
            shippedAddress = createAndUpdateAddress(shippedAddress, request, user);
            user.setAddress(shippedAddress);
            userRepo.save(user);
        }

        Cart cart = cartRepo.findByUser(user).get();
        List<CartItem> cartItems = cart.getCartItems();
        if(cartItems.size() == 0){
            throw new APIException("Cart is empty.");
        }
        List<OrderItem> orderItems = new ArrayList<>();
        for(CartItem cartItem : cartItems){
            OrderItem orderItem = new OrderItem();
            orderItem.setPrice(cartItem.getPrice());
            orderItem.setQuantity(cartItem.getQuantity());
            orderItem.setDiscountPrice(cartItem.getDiscountPrice());
            orderItem.setProduct(cartItem.getProduct());
            orderItemRepo.save(orderItem);
            orderItems.add(orderItem);
        }

        Order order = new Order();
        order.setUser(user);
        order.setOrderItems(orderItems);
        order.setShippingAddress(user.getAddress());
        order.setTotalPrice(cart.getTotalPrice());
        order.setOrderStatus(OrderStatus.PENDING);
        order.setOrderDate(LocalDateTime.now());
        order.setCreatedAt(LocalDateTime.now());

        order = orderRepo.save(order);

        for(OrderItem orderItem : orderItems){
            orderItem.setOrder(order);
            orderItemRepo.save(orderItem);
        }
        return order;
    }

    private Address createAndUpdateAddress(Address shippedAddress, CreateOrderDTO request, LocalUser user) {
        shippedAddress.setAddress(request.getAddress());
        shippedAddress.setCity(request.getCity());
        shippedAddress.setCountry(request.getCountry());
        shippedAddress.setUser(user);
        return addressRepo.save(shippedAddress);
    }
}
