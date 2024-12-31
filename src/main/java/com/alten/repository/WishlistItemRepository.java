package com.alten.repository;

import com.alten.entities.AppUser;
import com.alten.entities.WishlistItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface WishlistItemRepository extends JpaRepository<WishlistItem, Long> {
    List<WishlistItem> findByUser(AppUser user);
}