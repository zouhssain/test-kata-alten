package com.alten.service;

import com.alten.entities.AppUser;

import java.util.List;

public interface AccountService {
    AppUser addNewUser(AppUser appUser);
    AppUser loadUserByEmail(String email);
    List<AppUser> listUsers();
}
