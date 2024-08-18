package com.example.API.service.cart;

import com.example.API.model.Cart;

import java.math.BigDecimal;

public interface ICartService {
    Cart getCart(Long id);
    void clearCart(Long id);
    BigDecimal getTotalPrice(Long id);
    public Long initializeNewCart();
}
