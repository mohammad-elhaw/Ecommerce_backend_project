package com.backend.ecommerce.service.interfaces;

import com.backend.ecommerce.api.dto.CartDTO;
import com.backend.ecommerce.model.LocalUser;

public interface ICartService {
    void createCart(LocalUser user);
    CartDTO addProductToCart(Long userId, Long productId, Integer quantity);
    CartDTO getCart(Long userId);
    void deleteProductFromCart(LocalUser user, Long productId);
    CartDTO updateProductQuantityInCart(LocalUser user, Long productId, Integer quantity);
}
