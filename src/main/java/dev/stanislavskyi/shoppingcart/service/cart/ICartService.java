package dev.stanislavskyi.shoppingcart.service.cart;

import dev.stanislavskyi.shoppingcart.model.Cart;

import java.math.BigDecimal;


public interface ICartService {
    Cart getCart(Long id);
    void clearCart(Long id);
    BigDecimal getTotalPrice(Long id);

    Cart initializeNewCart(User user);

    Cart getCartByUserId(Long userId);
}