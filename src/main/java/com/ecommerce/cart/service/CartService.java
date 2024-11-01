package com.ecommerce.cart.service;

import com.ecommerce.cart.response.CartResponse;

public interface CartService {
    CartResponse getCartByUserId(Long userId);
}
