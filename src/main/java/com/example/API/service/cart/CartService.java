package com.example.API.service.cart;

import com.example.API.exception.ResourceNotFoundException;
import com.example.API.model.Cart;
import com.example.API.model.CartItem;
import com.example.API.repository.CartItemRepository;
import com.example.API.repository.CartRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.concurrent.atomic.AtomicLong;

@Service
@RequiredArgsConstructor
public class CartService implements ICartService{
    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final AtomicLong cartIdGenerator=new AtomicLong(0);
    @Override
    public Cart getCart(Long id) {
        Cart cart=cartRepository.findById(id)
                .orElseThrow(()->new ResourceNotFoundException("Cart not found"));
        BigDecimal totalAmount=cart.getTotalAmount();
        cart.setTotalAmount(totalAmount);
        return cartRepository.save(cart);
    }

    @Override
    public void clearCart(Long id) {
        Cart cart=getCart(id);
        cartItemRepository.deleteAllCartById(id);
        cart.getItems().clear();
        cartRepository.deleteById(id);
    }

    @Override
    public BigDecimal getTotalPrice(Long id) {
        Cart cart=getCart(id);
        return cart.getTotalAmount();
    }

    @Override
    public Long initializeNewCart(){
        Cart newCart=new Cart();
        Long newCartId=cartIdGenerator.incrementAndGet();
        newCart.setId(newCartId);
        return cartRepository.save(newCart).getId();
    }
}
