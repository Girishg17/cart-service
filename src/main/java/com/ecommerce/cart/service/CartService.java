package com.ecommerce.cart.service;

import com.ecommerce.cart.model.dto.CartItemDto;
import com.ecommerce.cart.response.CartResponse;

public interface CartService {
    CartResponse getCartByUserId(Long userId);
    String addToCart(Long userId, CartItemDto cartItemDTO);
}
