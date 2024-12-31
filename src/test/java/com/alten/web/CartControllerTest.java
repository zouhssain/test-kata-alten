package com.alten.web;

import com.alten.entities.CartItem;
import com.alten.entities.Product;
import com.alten.service.AccountService;
import com.alten.service.CartService;
import com.alten.service.ProductService;
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

@WebMvcTest(CartController.class)
public class CartControllerTest {

    @MockBean
    private CartService cartService;

    @MockBean
    private ProductService productService;

    @MockBean
    private AccountService accountService;

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
    public void testGetCartItemsWithJwt() throws Exception {
        Principal principal = () -> "user@example.com";
        CartItem cartItem = new CartItem(1L, null, null, 2);
        List<CartItem> cartItems = Arrays.asList(cartItem);

        when(cartService.getCartItems(principal.getName())).thenReturn(cartItems);

        mockMvc.perform(get("/cart")
                        .header("Authorization", jwtToken) // Add JWT Header
                        .principal(principal))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].quantity").value(2));

        verify(cartService, times(1)).getCartItems(principal.getName());
    }

    @Test
    public void testAddToCartWithJwt() throws Exception {
        Principal principal = () -> "user@example.com";
        Product product = new Product();
        product.setId(1L);
        CartItem cartItem = new CartItem(1L, null, product, 5);

        when(productService.getProductById(1L)).thenReturn(product);
        when(cartService.addToCart(principal.getName(), product, 5)).thenReturn(cartItem);

        mockMvc.perform(post("/cart/1")
                        .header("Authorization", jwtToken) // Add JWT Header
                        .principal(principal)
                        .param("quantity", "5"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.quantity").value(5));

        verify(productService, times(1)).getProductById(1L);
        verify(cartService, times(1)).addToCart(principal.getName(), product, 5);
    }

    @Test
    public void testRemoveFromCartWithJwt() throws Exception {
        Principal principal = () -> "user@example.com";

        doNothing().when(cartService).removeFromCart(principal.getName(), 1L);

        mockMvc.perform(delete("/cart/1")
                        .header("Authorization", jwtToken) // Add JWT Header
                        .principal(principal))
                .andExpect(status().isOk());

        verify(cartService, times(1)).removeFromCart(principal.getName(), 1L);
    }
}