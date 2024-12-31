package com.alten.repository;

import com.alten.entities.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AppUserRepository extends JpaRepository<AppUser, Long> {
    AppUser findByUsername(String userName);
    AppUser findByEmail(String email);
}