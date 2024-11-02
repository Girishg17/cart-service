package com.ecommerce.cart.controller;

import com.ecommerce.cart.model.dto.CartItemDto;
import com.ecommerce.cart.model.entity.CartItem;
import com.ecommerce.cart.request.QuantityReq;
import com.ecommerce.cart.response.CartResponse;
import com.ecommerce.cart.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cart")
public class CartController {

    @Autowired
    private CartService cartService;

    @GetMapping("/{userId}")
    public CartResponse getCart(@PathVariable Long userId) {
        CartResponse c = cartService.getCartByUserId(userId);
        return c;
    }

    @PostMapping("/{userId}/add")
    public ResponseEntity<String> addToCart(@PathVariable Long userId, @RequestBody CartItemDto cartItemDTO) {
        String message = cartService.addToCart(userId, cartItemDTO);
        return ResponseEntity.ok(message);
    }

    @DeleteMapping("/{userId}/remove/{productId}")
    public void removeFromCart(@PathVariable Long userId, @PathVariable Long productId) {
        cartService.removeFromCart(userId, productId);
    }

    @PutMapping("/{userId}/update/{productId}")
    public void updateCartItem(@PathVariable Long userId, @PathVariable Long productId, @RequestBody QuantityReq quantityReq) {
        Integer q = quantityReq.getQuantity();
        if (q > 0) {
            cartService.updateCartItem(userId, productId, q);
        } else {
            cartService.removeFromCart(userId, productId);
        }
    }


    @GetMapping("/items/{userId}")
    public ResponseEntity<List<CartItem>> getCartItems(@PathVariable Long userId) {
        List<CartItem> cartItems = cartService.cartItemofUser(userId);
        return ResponseEntity.ok(cartItems);
    }

    @GetMapping("/id/{userId}")
    public ResponseEntity<Long> getCartId(@PathVariable Long userId) {
        Long cartId = cartService.getCartId(userId);
        return ResponseEntity.ok(cartId);
    }

    @DeleteMapping("/delete/{cartId}")
    public ResponseEntity<Void> deleteCart(@PathVariable Long cartId) {
        cartService.deleteCart(cartId);
        return ResponseEntity.noContent().build();
    }
}
