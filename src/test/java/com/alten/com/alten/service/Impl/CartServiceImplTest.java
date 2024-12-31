package com.alten.com.alten.service.Impl;

import com.alten.entities.AppUser;
import com.alten.entities.CartItem;
import com.alten.entities.Product;
import com.alten.repository.AppUserRepository;
import com.alten.repository.CartItemRepository;
import com.alten.service.Impl.CartServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class CartServiceImplTest {

    @Mock
    private CartItemRepository cartItemRepository;

    @Mock
    private AppUserRepository appUserRepository;

    @InjectMocks
    private CartServiceImpl cartService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetCartItems() {
        String username = "test@example.com";
        AppUser user = new AppUser();
        List<CartItem> cartItems = new ArrayList<>();
        cartItems.add(new CartItem(1L, user, new Product(), 2));

        when(appUserRepository.findByEmail(username)).thenReturn(user);
        when(cartItemRepository.findByUser(user)).thenReturn(cartItems);

        List<CartItem> result = cartService.getCartItems(username);

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(appUserRepository, times(1)).findByEmail(username);
        verify(cartItemRepository, times(1)).findByUser(user);
    }

    @Test
    public void testAddToCart() {
        String username = "test@example.com";
        AppUser user = new AppUser();
        Product product = new Product();
        CartItem cartItem = new CartItem(null, user, product, 5);

        when(appUserRepository.findByEmail(username)).thenReturn(user);
        when(cartItemRepository.save(any(CartItem.class))).thenReturn(cartItem);

        CartItem result = cartService.addToCart(username, product, 5);

        assertNotNull(result);
        assertEquals(user, result.getUser());
        assertEquals(product, result.getProduct());
        assertEquals(5, result.getQuantity());
        verify(appUserRepository, times(1)).findByEmail(username);
        verify(cartItemRepository, times(1)).save(any(CartItem.class));
    }

    @Test
    public void testRemoveFromCart() {
        String username = "test@example.com";
        Long cartItemId = 1L;
        AppUser user = new AppUser();
        CartItem cartItem = new CartItem(cartItemId, user, new Product(), 2);

        when(appUserRepository.findByEmail(username)).thenReturn(user);
        when(cartItemRepository.findById(cartItemId)).thenReturn(Optional.of(cartItem));

        assertDoesNotThrow(() -> cartService.removeFromCart(username, cartItemId));

        verify(appUserRepository, times(1)).findByEmail(username);
        verify(cartItemRepository, times(1)).findById(cartItemId);
        verify(cartItemRepository, times(1)).delete(cartItem);
    }

    @Test
    public void testRemoveFromCart_ItemNotFound() {
        String username = "test@example.com";
        Long cartItemId = 1L;

        when(appUserRepository.findByEmail(username)).thenReturn(new AppUser());
        when(cartItemRepository.findById(cartItemId)).thenReturn(Optional.empty());

        Exception exception = assertThrows(RuntimeException.class,
                () -> cartService.removeFromCart(username, cartItemId));

        assertEquals("Cart item not found", exception.getMessage());
        verify(cartItemRepository, times(1)).findById(cartItemId);
    }
}