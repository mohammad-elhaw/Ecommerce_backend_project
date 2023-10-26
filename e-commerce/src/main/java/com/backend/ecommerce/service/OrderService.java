package com.backend.ecommerce.service;

import com.backend.ecommerce.api.dto.CreateOrderDTO;
import com.backend.ecommerce.api.dto.OrderItemDTO;
import com.backend.ecommerce.api.dto.OrderResponseDTO;
import com.backend.ecommerce.api.dto.ShippedAddressDTO;
import com.backend.ecommerce.domain.OrderStatus;
import com.backend.ecommerce.exception.APIException;
import com.backend.ecommerce.exception.ResourceNotFoundException;
import com.backend.ecommerce.model.*;
import com.backend.ecommerce.model.repository.CartRepo;
import com.backend.ecommerce.model.repository.OrderItemRepo;
import com.backend.ecommerce.model.repository.OrderRepo;
import com.backend.ecommerce.model.repository.ProductRepo;
import com.backend.ecommerce.service.interfaces.ICartService;
import com.backend.ecommerce.service.interfaces.IOrderService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService implements IOrderService {

    private final CartRepo cartRepo;
    private final OrderItemRepo orderItemRepo;
    private final OrderRepo orderRepo;
    private final ICartService cartService;
    private final ProductRepo productRepo;
    private final ModelMapper mapper;

    @SneakyThrows
    @Override
    @Transactional
    public OrderResponseDTO createOrder(LocalUser user, CreateOrderDTO request) {

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

        ShippedAddress shippedAddress = new ShippedAddress();
        shippedAddress.setCity(request.getCity());
        shippedAddress.setCountry(request.getCountry());
        shippedAddress.setStreetName(request.getStreetName());
        shippedAddress.setAddress(request.getAddress());
        shippedAddress.setUser(user);

        Order order = new Order();
        order.setUser(user);
        order.setOrderItems(orderItems);
        order.setTotalPrice(cart.getTotalPrice());
        order.setShippedAddress(shippedAddress);
        order.setOrderStatus(OrderStatus.PENDING);
        order.setCreatedAt(LocalDateTime.now());

        order = orderRepo.save(order);

        for(OrderItem orderItem : orderItems){
            orderItem.setOrder(order);
            orderItemRepo.save(orderItem);
        }

        List<CartItem> cartItemsCopy = new ArrayList<>(cartItems);
        cartItemsCopy.forEach(cartItem -> {
            int quantity = cartItem.getQuantity();
            Product product = cartItem.getProduct();
            cartService.deleteProductFromCart(user, product.getProductId());
            int inventoryQuantity = product.getInventory().getQuantity();
            product.getInventory().setQuantity(inventoryQuantity - quantity);
            productRepo.save(product);
        });

        return createOrderResponseDTO(order);
    }

    private OrderResponseDTO createOrderResponseDTO(Order order) {
        OrderResponseDTO orderResponseDTO = mapper.map(order, OrderResponseDTO.class);
        List<OrderItemDTO> orderItemDTOS = order.getOrderItems().stream()
                        .map(orderItem -> mapper.map(orderItem, OrderItemDTO.class)).toList();
        orderResponseDTO.setOrderItems(orderItemDTOS);
        orderResponseDTO.setAddress(mapper.map(order.getShippedAddress(), ShippedAddressDTO.class));
        return orderResponseDTO;
    }

    @Override
    public List<OrderResponseDTO> userOrderHistory(LocalUser user) {
        List<Order> orders = orderRepo.getUserOrders(user.getUserId());
        return createListOrdersResponseDTO(orders);
    }

    private List<OrderResponseDTO> createListOrdersResponseDTO(List<Order> orders) {
        return orders.stream()
                .map(order -> {
                    OrderResponseDTO orderResponseDTO = mapper.map(order, OrderResponseDTO.class);
                    List<OrderItemDTO> orderItemDTOS = order.getOrderItems().stream()
                            .map(orderItem -> mapper.map(orderItem, OrderItemDTO.class)).toList();
                    orderResponseDTO.setAddress(mapper.map(order.getShippedAddress(), ShippedAddressDTO.class));
                    orderResponseDTO.setOrderItems(orderItemDTOS);
                    return orderResponseDTO;
                }).toList();
    }

    @Override
    public OrderResponseDTO findOrderById(LocalUser user, Long orderId) {
        Order order = orderRepo.findOrderByUserIdAndOrderId(user.getUserId(), orderId)
                .orElseThrow(()->new ResourceNotFoundException("Order", "orderId", orderId));
        return createOrderResponseDTO(order);
    }

    @Override
    @Transactional
    public OrderResponseDTO cancelOrder(LocalUser user, Long orderId) {
        Order order =orderRepo.findById(orderId)
                .orElseThrow(()->new ResourceNotFoundException("Order", "orderId", orderId));
        List<OrderItem> orderItems = order.getOrderItems();
        orderItems.forEach(orderItem -> {
            int quantity = orderItem.getQuantity();
            Product product = orderItem.getProduct();
            int inventoryQuantity = product.getInventory().getQuantity();
            product.getInventory().setQuantity(inventoryQuantity + quantity);
            productRepo.save(product);
        });
        return changeOrderStatus(orderId, OrderStatus.CANCELLED);
    }

    @Override
    @Transactional
    public void deleteOrderById(LocalUser user, Long orderId) {
        Order order = orderRepo.findOrderByUserIdAndOrderId(user.getUserId(), orderId)
                .orElseThrow(()->new ResourceNotFoundException("Order", "orderId", orderId));
        orderRepo.delete(order);
    }

    @Override
    public List<OrderResponseDTO> getAllOrders() {
        List<Order> orders = orderRepo.findAll();
        return createListOrdersResponseDTO(orders);
    }

    @Override
    public OrderResponseDTO confirmOrder(Long orderId) {
        return changeOrderStatus(orderId, OrderStatus.CONFIRMED);
    }

    @Override
    public OrderResponseDTO placeOrder(Long orderId) {
        return changeOrderStatus(orderId, OrderStatus.PLACED);
    }

    @Override
    public OrderResponseDTO shipOrder(Long orderId) {
        return changeOrderStatus(orderId, OrderStatus.SHIPPED);
    }

    @Override
    public OrderResponseDTO deliverOrder(Long orderId) {
        return changeOrderStatus(orderId, OrderStatus.DELIVERED);
    }

    @Transactional
    private OrderResponseDTO changeOrderStatus(Long orderId, OrderStatus orderStatus) {
        Order order = orderRepo.findById(orderId)
                .orElseThrow(()-> new ResourceNotFoundException("Order", "orderId", orderId));
        order.setOrderStatus(orderStatus);
        if(orderStatus.equals(OrderStatus.DELIVERED)){
            order.setDeliveryDate(LocalDateTime.now());
        }
        orderRepo.save(order);
        return createOrderResponseDTO(order);
    }

}
