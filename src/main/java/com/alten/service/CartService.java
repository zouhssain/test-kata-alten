package com.alten.service;


import com.alten.entities.CartItem;
import com.alten.entities.Product;

import java.util.List;

public interface CartService {
    List<CartItem> getCartItems(String username);
    CartItem addToCart(String username, Product product, int quantity);
    void removeFromCart(String username, Long cartItemId);
}
