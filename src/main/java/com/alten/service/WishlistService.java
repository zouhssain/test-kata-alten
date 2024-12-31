package com.alten.service;


import com.alten.entities.Product;
import com.alten.entities.WishlistItem;

import java.util.List;

public interface WishlistService {
    List<WishlistItem> getWishlistItems(String username);
    WishlistItem addToWishlist(String username, Product product);
    void removeFromWishlist(String username, Long wishlistItemId);
}
