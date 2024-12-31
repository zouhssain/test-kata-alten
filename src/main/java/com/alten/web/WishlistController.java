package com.alten.web;

import com.alten.entities.Product;
import com.alten.entities.WishlistItem;
import com.alten.service.ProductService;
import com.alten.service.WishlistService;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/wishlist")
public class WishlistController {

    private final WishlistService wishlistService;
    private final ProductService productService;

    public WishlistController(WishlistService wishlistService, ProductService productService) {
        this.wishlistService = wishlistService;
        this.productService = productService;
    }

    @GetMapping
    public List<WishlistItem> getWishlistItems(Principal principal) {
        return wishlistService.getWishlistItems(principal.getName());
    }

    @PostMapping("/{productId}")
    public WishlistItem addToWishlist(@PathVariable Long productId, Principal principal) {
        Product product = productService.getProductById(productId);
        return wishlistService.addToWishlist(principal.getName(), product);
    }

    @DeleteMapping("/{wishlistItemId}")
    public void removeFromWishlist(@PathVariable Long wishlistItemId, Principal principal) {
        wishlistService.removeFromWishlist(principal.getName(), wishlistItemId);
    }
}