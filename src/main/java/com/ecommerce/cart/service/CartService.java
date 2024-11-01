package com.ecommerce.cart.service;

import com.ecommerce.cart.model.dto.CartItemDto;
import com.ecommerce.cart.response.CartResponse;

public interface CartService {
    CartResponse getCartByUserId(Long userId);
    String addToCart(Long userId, CartItemDto cartItemDTO);
    void removeFromCart(Long userId, Long productId);
    void updateCartItem(Long userId, Long productId, Integer quantity);
}
