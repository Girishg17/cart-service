package com.ecommerce.cart.service.cartserviceimpl;

import com.ecommerce.cart.model.dto.ProductDto;
import com.ecommerce.cart.model.entity.Cart;
import com.ecommerce.cart.model.entity.CartItem;
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

    private ProductDto fetchProductById(Long ProductId)
    {
        String url = "http://localhost:8082/product/products/" + ProductId;
        return restTemplate.getForObject(url, ProductDto.class);
    }
}
