package com.backend.ecommerce.service;

import com.backend.ecommerce.api.dto.CartDTO;
import com.backend.ecommerce.api.dto.ProductResponseDTO;
import com.backend.ecommerce.exception.APIException;
import com.backend.ecommerce.exception.ResourceNotFoundException;
import com.backend.ecommerce.model.Cart;
import com.backend.ecommerce.model.CartItem;
import com.backend.ecommerce.model.LocalUser;
import com.backend.ecommerce.model.Product;
import com.backend.ecommerce.model.repository.CartItemRepo;
import com.backend.ecommerce.model.repository.CartRepo;
import com.backend.ecommerce.model.repository.ProductRepo;
import com.backend.ecommerce.model.repository.UserRepo;
import com.backend.ecommerce.service.interfaces.ICartService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CartService implements ICartService {

    private final CartRepo cartRepo;
    private final UserRepo userRepo;
    private final ProductRepo productRepo;
    private final CartItemRepo cartItemRepo;
    private final ModelMapper mapper;

    @Override
    @Transactional
    public void createCart(LocalUser user) {
        Cart cart = new Cart();
        cart.setUser(user);
        cartRepo.save(cart);
    }

    @SneakyThrows
    @Override
    @Transactional
    public CartDTO addProductToCart(Long userId, Long productId, Integer quantity) {
        LocalUser user = userRepo.findById(userId)
                .orElseThrow(()-> new ResourceNotFoundException("User", "userId", userId));
        Cart cart = cartRepo.findByUser(user)
                .orElseThrow(()-> new ResourceNotFoundException("Cart", "userId", userId));
        Product product = productRepo.findById(productId)
                .orElseThrow(()-> new ResourceNotFoundException("Product", "productId", productId));
        Optional<CartItem> cartItem = cartItemRepo.findCartItemByProductIdAndCartId(cart.getCartId(), productId);

        if(cartItem.isPresent()){
            throw new APIException("Product " + product.getProductName() + " already exists in the cart.");
        }

        if(product.getInventory().getQuantity() == 0){
            throw new APIException(product.getProductName() + " is not available");
        }

        if(product.getInventory().getQuantity() < quantity){
            throw new APIException("the available quantity of " + product.getProductName() +
                    " less than the quantity you insert.");
        }

        CartItem newCartItem = new CartItem();
        newCartItem.setProduct(product);
        newCartItem.setCart(cart);
        newCartItem.setQuantity(quantity);
        newCartItem.setPrice(product.getPrice());
        newCartItem.setDiscountPrice(product.getDiscountPrice());

        cartItemRepo.save(newCartItem);

        if (product.getDiscountPrice() > 0) {
            cart.setTotalPrice(cart.getTotalPrice() + (product.getDiscountPrice() * quantity));
        } else {
            cart.setTotalPrice(cart.getTotalPrice() + (product.getPrice()) * quantity);
        }
        cartRepo.save(cart);
        CartDTO cartDTO = mapper.map(cart, CartDTO.class);
        List<ProductResponseDTO> productResponseDTOS = cart.getCartItems().stream()
                .map(cartItem1 -> mapper.map(cartItem1.getProduct(), ProductResponseDTO.class)).toList();
        cartDTO.setProducts(productResponseDTOS);
        return cartDTO;
    }

    @Override
    public CartDTO getCart(Long userId) {
        LocalUser user = userRepo.findById(userId)
                .orElseThrow(()->new ResourceNotFoundException("User", "userId", userId));
        Cart cart = cartRepo.findByUser(user)
                .orElseThrow(()->new ResourceNotFoundException("Cart", "userId", userId));

        CartDTO cartDTO = mapper.map(cart, CartDTO.class);
        List<ProductResponseDTO> productResponseDTOS = cart.getCartItems().stream()
                .map(cartItem -> mapper.map(cartItem.getProduct(), ProductResponseDTO.class)).toList();
        cartDTO.setProducts(productResponseDTOS);
        return cartDTO;
    }

    @Override
    @Transactional
    public void deleteProductFromCart(LocalUser user, Long productId) {
        Cart cart = cartRepo.findByUser(user).get();
        CartItem cartItem = cartItemRepo.findCartItemByProductIdAndCartId(cart.getCartId(), productId)
                .orElseThrow(()->new ResourceNotFoundException("Product", "productId", productId));
        if(cartItem.getDiscountPrice() > 0){
            cart.setTotalPrice(cart.getTotalPrice() - cartItem.getDiscountPrice() * cartItem.getQuantity());
        } else{
            cart.setTotalPrice(cart.getTotalPrice() - cartItem.getPrice() * cartItem.getQuantity());
        }
        cartRepo.save(cart);
        cartItemRepo.delete(cartItem);
    }

    @Override
    @Transactional
    public CartDTO updateProductQuantityInCart(LocalUser user, Long productId, Integer quantity) {
        Cart cart = cartRepo.findByUser(user).get();
        CartItem cartItem = cartItemRepo.findCartItemByProductIdAndCartId(cart.getCartId(), productId)
                .orElseThrow(()->new ResourceNotFoundException("Product", "productId", productId));
        int quantityToRemove = cartItem.getQuantity() - quantity;
        cartItem.setQuantity(quantity);
        cartItemRepo.save(cartItem);


        if (cartItem.getDiscountPrice() > 0) {
            cart.setTotalPrice(cart.getTotalPrice() - (cartItem.getDiscountPrice() * quantityToRemove));
        } else {
            cart.setTotalPrice(cart.getTotalPrice() - (cartItem.getPrice()) * quantityToRemove);
        }
        cartRepo.save(cart);
        CartDTO cartDTO = mapper.map(cart, CartDTO.class);
        List<ProductResponseDTO> productResponseDTOS = cart.getCartItems().stream()
                .map(cartItem1 -> mapper.map(cartItem1.getProduct(), ProductResponseDTO.class)).toList();
        cartDTO.setProducts(productResponseDTOS);
        return cartDTO;
    }
}
