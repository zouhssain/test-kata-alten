package com.alten.service.Impl;


import com.alten.entities.AppUser;
import com.alten.entities.CartItem;
import com.alten.entities.Product;
import com.alten.repository.AppUserRepository;
import com.alten.repository.CartItemRepository;
import com.alten.service.CartService;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
public class CartServiceImpl implements CartService {

    private final CartItemRepository cartItemRepository;
    private final AppUserRepository appUserRepository;

    public CartServiceImpl(CartItemRepository cartItemRepository, AppUserRepository appUserRepository) {
        this.cartItemRepository = cartItemRepository;
        this.appUserRepository = appUserRepository;
    }

    @Override
    public List<CartItem> getCartItems(String username) {
        AppUser user = appUserRepository.findByEmail(username);
        return cartItemRepository.findByUser(user);
    }

    @Override
    public CartItem addToCart(String username, Product product, int quantity) {
        AppUser user = appUserRepository.findByEmail(username);
        CartItem cartItem = new CartItem(null, user, product, quantity);
        return cartItemRepository.save(cartItem);
    }

    @Override
    public void removeFromCart(String username, Long cartItemId) {
        AppUser user = appUserRepository.findByEmail(username);
        CartItem cartItem = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new RuntimeException("Cart item not found"));
        if (cartItem.getUser().equals(user)) {
            cartItemRepository.delete(cartItem);
        }
    }
}