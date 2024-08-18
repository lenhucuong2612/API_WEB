package com.example.API.service.cart;

import com.example.API.model.CartItem;

public interface ICartItemService {
    void addCartItem(Long cartId, Long product, int quantity);
    void removeItemFromCart(Long cartId, Long productId);
    void updateItemQuantity(Long cartId, Long productId, int quantity);

    CartItem getCartItem(Long cartId, Long productId);
}
