package com.alten.service.Impl;


import com.alten.entities.AppUser;
import com.alten.entities.Product;
import com.alten.entities.WishlistItem;
import com.alten.repository.AppUserRepository;
import com.alten.repository.WishlistItemRepository;
import com.alten.service.WishlistService;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
public class WishlistServiceImpl implements WishlistService {

    private final WishlistItemRepository wishlistItemRepository;
    private final AppUserRepository appUserRepository;

    public WishlistServiceImpl(WishlistItemRepository wishlistItemRepository, AppUserRepository appUserRepository) {
        this.wishlistItemRepository = wishlistItemRepository;
        this.appUserRepository = appUserRepository;
    }

    @Override
    public List<WishlistItem> getWishlistItems(String username) {
        AppUser user = appUserRepository.findByEmail(username);
        return wishlistItemRepository.findByUser(user);
    }

    @Override
    public WishlistItem addToWishlist(String username, Product product) {
        AppUser user = appUserRepository.findByEmail(username);
        WishlistItem wishlistItem = new WishlistItem(null, user, product);
        return wishlistItemRepository.save(wishlistItem);
    }

    @Override
    public void removeFromWishlist(String username, Long wishlistItemId) {
        AppUser user = appUserRepository.findByEmail(username);
        WishlistItem wishlistItem = wishlistItemRepository.findById(wishlistItemId)
                .orElseThrow(() -> new RuntimeException("Wishlist item not found"));
        if (wishlistItem.getUser().equals(user)) {
            wishlistItemRepository.delete(wishlistItem);
        }
    }
}