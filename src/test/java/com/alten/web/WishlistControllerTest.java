package com.alten.web;

import com.alten.entities.Product;
import com.alten.entities.WishlistItem;
import com.alten.service.AccountService;
import com.alten.service.ProductService;
import com.alten.service.WishlistService;
import com.alten.utils.JWTUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.security.Principal;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(WishlistController.class)
public class WishlistControllerTest {

    @MockBean
    private WishlistService wishlistService;

    @MockBean
    private AccountService accountService;

    @MockBean
    private ProductService productService;

    @Autowired
    private MockMvc mockMvc;

    private String jwtToken;

    @BeforeEach
    public void setUp() {
        Algorithm algorithm = Algorithm.HMAC256(JWTUtil.SECRET);
        jwtToken = "Bearer " + JWT.create()
                .withSubject("user@example.com")
                .withClaim("role", "USER")
                .withIssuedAt(new Date())
                .withExpiresAt(new Date(System.currentTimeMillis() + 3600 * 1000)) // 1-hour expiry
                .sign(algorithm);
    }

    @Test
    public void testGetWishlistItemsWithJwt() throws Exception {
        Principal principal = () -> "user@example.com";
        WishlistItem wishlistItem = new WishlistItem(1L, null, null);
        List<WishlistItem> wishlistItems = Arrays.asList(wishlistItem);

        when(wishlistService.getWishlistItems(principal.getName())).thenReturn(wishlistItems);

        mockMvc.perform(get("/wishlist")
                        .header("Authorization", jwtToken)
                        .principal(principal))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].id").value(1L));

        verify(wishlistService, times(1)).getWishlistItems(principal.getName());
    }

    @Test
    public void testAddToWishlistWithJwt() throws Exception {
        Principal principal = () -> "user@example.com";
        Product product = new Product();
        product.setId(1L);
        WishlistItem wishlistItem = new WishlistItem(1L, null, product);

        when(productService.getProductById(1L)).thenReturn(product);
        when(wishlistService.addToWishlist(principal.getName(), product)).thenReturn(wishlistItem);

        mockMvc.perform(post("/wishlist/1")
                        .header("Authorization", jwtToken)
                        .principal(principal))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1L));

        verify(productService, times(1)).getProductById(1L);
        verify(wishlistService, times(1)).addToWishlist(principal.getName(), product);
    }

    @Test
    public void testRemoveFromWishlistWithJwt() throws Exception {
        Principal principal = () -> "user@example.com";

        doNothing().when(wishlistService).removeFromWishlist(principal.getName(), 1L);

        mockMvc.perform(delete("/wishlist/1")
                        .header("Authorization", jwtToken)
                        .principal(principal))
                .andExpect(status().isOk());

        verify(wishlistService, times(1)).removeFromWishlist(principal.getName(), 1L);
    }
}
