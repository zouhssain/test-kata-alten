package com.alten.web;


import com.alten.entities.CartItem;
import com.alten.entities.Product;
import com.alten.service.CartService;
import com.alten.service.ProductService;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/cart")
public class CartController {

    private final CartService cartService;
    private final ProductService productService;

    public CartController(CartService cartService, ProductService productService) {
        this.cartService = cartService;
        this.productService = productService;
    }

    @GetMapping
    public List<CartItem> getCartItems(Principal principal) {
        return cartService.getCartItems(principal.getName());
    }

    @PostMapping("/{productId}")
    public CartItem addToCart(@PathVariable Long productId, @RequestParam int quantity, Principal principal) {
        Product product = productService.getProductById(productId);
        return cartService.addToCart(principal.getName(), product, quantity);
    }

    @DeleteMapping("/{cartItemId}")
    public void removeFromCart(@PathVariable Long cartItemId, Principal principal) {
        cartService.removeFromCart(principal.getName(), cartItemId);
    }
}