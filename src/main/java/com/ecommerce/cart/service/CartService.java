package com.ecommerce.cart.service;

import com.ecommerce.cart.model.dto.CartItemDto;
import com.ecommerce.cart.model.entity.CartItem;
import com.ecommerce.cart.response.CartResponse;

import java.util.List;

public interface CartService {
    CartResponse getCartByUserId(Long userId);
    String addToCart(Long userId, CartItemDto cartItemDTO);
    void removeFromCart(Long userId, Long productId);
    void updateCartItem(Long userId, Long productId, Integer quantity);
    List<CartItem> cartItemofUser(Long userId);
    Long getCartId(Long userId);
    void deleteCart(Long cartId);
}
