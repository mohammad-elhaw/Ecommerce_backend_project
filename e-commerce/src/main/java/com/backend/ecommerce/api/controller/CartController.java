package com.backend.ecommerce.api.controller;

import com.backend.ecommerce.api.dto.CartDTO;
import com.backend.ecommerce.model.LocalUser;
import com.backend.ecommerce.service.interfaces.ICartService;
import com.backend.ecommerce.service.interfaces.IUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class CartController {

    private final ICartService cartService;
    private final IUserService userService;

    @PostMapping("/cart/product/{productId}/quantity/{quantity}")
    public ResponseEntity<?> addProductToCart(@PathVariable Long productId,
                                              @PathVariable Integer quantity){
        LocalUser user = userService.getAuthenticatedUser();
        CartDTO cartDTO = cartService.addProductToCart(user.getUserId(), productId, quantity);
        return new ResponseEntity<>(cartDTO, HttpStatus.CREATED);
    }

    @GetMapping("/cart")
    public ResponseEntity<?> getCartByUser(){
        LocalUser user = userService.getAuthenticatedUser();
        CartDTO cartDTO = cartService.getCart(user.getUserId());
        return new ResponseEntity<>(cartDTO, HttpStatus.FOUND);
    }

    @PutMapping("/cart/product/{productId}/quantity/{quantity}")
    public ResponseEntity<?> updateCartProduct(@PathVariable Long productId, @PathVariable Integer quantity){
        LocalUser user = userService.getAuthenticatedUser();
        CartDTO cartDTO = cartService.updateProductQuantityInCart(user, productId, quantity);
        return new ResponseEntity<>(cartDTO, HttpStatus.OK);
    }

    @DeleteMapping("/cart/product/{productId}")
    public ResponseEntity<?> deleteProductFromCart(@PathVariable Long productId){
        LocalUser user = userService.getAuthenticatedUser();
        cartService.deleteProductFromCart(user, productId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
