package com.alten.repository;

import com.alten.entities.AppUser;
import com.alten.entities.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    List<CartItem> findByUser(AppUser user);
}