package com.ecommerce.cart.service.cartserviceimpl;

import com.ecommerce.cart.model.dto.CartItemDto;
import com.ecommerce.cart.model.dto.ProductDto;
import com.ecommerce.cart.model.entity.Cart;
import com.ecommerce.cart.model.entity.CartItem;
import com.ecommerce.cart.repository.CartItemRepository;
import com.ecommerce.cart.repository.CartRepository;
import com.ecommerce.cart.response.CartItemResponse;
import com.ecommerce.cart.response.CartResponse;
import com.ecommerce.cart.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CartserviceImpl implements CartService {
    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private CartItemRepository cartItemRepository;

    @Autowired
    private RestTemplate restTemplate;

    @Override
    public CartResponse getCartByUserId(Long userId) {
        Optional<Cart> cartOptional = cartRepository.findByUserId(userId);
        CartResponse cartResponse = new CartResponse();

        if (cartOptional.isPresent()) {
            Cart cart = cartOptional.get();
            List<CartItemResponse> itemResponses = new ArrayList<>();

            for (CartItem cartItem : cart.getItems()) {
                 ProductDto product = fetchProductById(cartItem.getProductId());

                CartItemResponse itemResponse = new CartItemResponse();
                itemResponse.setProductId(product.getId());
                itemResponse.setProductName(product.getName());
                itemResponse.setProductUsp(product.getUsp());
                itemResponse.setProductDescription(product.getDescription());
                itemResponse.setProductFile(product.getFile());
                itemResponse.setProductPrice(product.getPrice());
                itemResponse.setProductStock(product.getStock());
                itemResponse.setQuantity(cartItem.getQuantity());

                itemResponses.add(itemResponse);
            }

            cartResponse.setItems(itemResponses);
        }

        return cartResponse; // Return the cart response
    }

    public String addToCart(Long userId, CartItemDto cartItemDTO) {
        Cart cart = cartRepository.findByUserId(userId).orElseGet(() -> createNewCart(userId));
        if (cart == null) {
            return "Cart could not be created";
        }

//        Product product = productRepository.findById(cartItemDTO.getProductId())
//                .orElseThrow(() -> new RuntimeException("Product not found"));

        boolean productExistsInCart = cart.getItems().stream()
                .anyMatch(item -> item.getProductId().equals(cartItemDTO.getProductId()));

        if (productExistsInCart) {
            return "Product already in cart";
        }


        CartItem cartItem = new CartItem();
        cartItem.setProductId(cartItemDTO.getProductId());
        cartItem.setQuantity(cartItemDTO.getQuantity());
        cartItem.setCart(cart);

        cart.getItems().add(cartItem);
        cartRepository.save(cart);

        return "Product added to cart successfully";
    }

    public void removeFromCart(Long userId, Long productId) {
        Cart cart = cartRepository.findByUserId(userId).orElseThrow(() -> new RuntimeException("Cart not found"));
        cart.getItems().removeIf(item -> item.getProductId().equals(productId));
        cartRepository.save(cart);
    }

    public void updateCartItem(Long userId, Long productId, Integer quantity) {
        Cart cart = cartRepository.findByUserId(userId).orElseThrow(() -> new RuntimeException("Cart not found"));
        CartItem cartItem = cart.getItems().stream()
                .filter(item -> item.getProductId().equals(productId))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Product not found in cart"));

        cartItem.setQuantity(quantity);
        cartItemRepository.save(cartItem);
    }


    //other
    public List<CartItem> cartItemofUser(Long userId) {
        List<CartItem> cart = cartRepository.findByUserId(userId).get().getItems();
        return cart;
    }

    public Long getCartId(Long userId) {
        return cartRepository.findByUserId(userId).get().getId();
    }


    public void deleteCart(Long cartId) {
        cartRepository.deleteById(cartId);
    }

    private Cart createNewCart(Long userId) {
        Cart cart = new Cart();
        cart.setUserId(userId);
        return cartRepository.save(cart);
    }


    private ProductDto fetchProductById(Long ProductId)
    {
        String url = "http://localhost:8082/product/products/" + ProductId;
        return restTemplate.getForObject(url, ProductDto.class);
    }
}
