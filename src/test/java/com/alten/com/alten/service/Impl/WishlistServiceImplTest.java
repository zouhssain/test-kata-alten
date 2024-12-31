package com.alten.com.alten.service.Impl;

import com.alten.entities.AppUser;
import com.alten.entities.Product;
import com.alten.entities.WishlistItem;
import com.alten.repository.AppUserRepository;
import com.alten.repository.WishlistItemRepository;
import com.alten.service.Impl.WishlistServiceImpl;
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

public class WishlistServiceImplTest {

    @Mock
    private WishlistItemRepository wishlistItemRepository;

    @Mock
    private AppUserRepository appUserRepository;

    @InjectMocks
    private WishlistServiceImpl wishlistService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetWishlistItems() {
        String username = "test@example.com";
        AppUser user = new AppUser();
        List<WishlistItem> wishlistItems = new ArrayList<>();
        wishlistItems.add(new WishlistItem(1L, user, new Product()));

        when(appUserRepository.findByEmail(username)).thenReturn(user);
        when(wishlistItemRepository.findByUser(user)).thenReturn(wishlistItems);

        List<WishlistItem> result = wishlistService.getWishlistItems(username);

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(appUserRepository, times(1)).findByEmail(username);
        verify(wishlistItemRepository, times(1)).findByUser(user);
    }

    @Test
    public void testAddToWishlist() {
        String username = "test@example.com";
        AppUser user = new AppUser();
        Product product = new Product();
        WishlistItem wishlistItem = new WishlistItem(null, user, product);

        when(appUserRepository.findByEmail(username)).thenReturn(user);
        when(wishlistItemRepository.save(any(WishlistItem.class))).thenReturn(wishlistItem);

        WishlistItem result = wishlistService.addToWishlist(username, product);

        assertNotNull(result);
        assertEquals(user, result.getUser());
        assertEquals(product, result.getProduct());
        verify(appUserRepository, times(1)).findByEmail(username);
        verify(wishlistItemRepository, times(1)).save(any(WishlistItem.class));
    }

    @Test
    public void testRemoveFromWishlist() {
        String username = "test@example.com";
        Long wishlistItemId = 1L;
        AppUser user = new AppUser();
        WishlistItem wishlistItem = new WishlistItem(wishlistItemId, user, new Product());

        when(appUserRepository.findByEmail(username)).thenReturn(user);
        when(wishlistItemRepository.findById(wishlistItemId)).thenReturn(Optional.of(wishlistItem));

        assertDoesNotThrow(() -> wishlistService.removeFromWishlist(username, wishlistItemId));

        verify(appUserRepository, times(1)).findByEmail(username);
        verify(wishlistItemRepository, times(1)).findById(wishlistItemId);
        verify(wishlistItemRepository, times(1)).delete(wishlistItem);
    }

    @Test
    public void testRemoveFromWishlist_ItemNotFound() {
        String username = "test@example.com";
        Long wishlistItemId = 1L;

        when(appUserRepository.findByEmail(username)).thenReturn(new AppUser());
        when(wishlistItemRepository.findById(wishlistItemId)).thenReturn(Optional.empty());

        Exception exception = assertThrows(RuntimeException.class,
                () -> wishlistService.removeFromWishlist(username, wishlistItemId));

        assertEquals("Wishlist item not found", exception.getMessage());
        verify(wishlistItemRepository, times(1)).findById(wishlistItemId);
    }
}
