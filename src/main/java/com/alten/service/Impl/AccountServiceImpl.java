package com.alten.service.Impl;


import com.alten.entities.AppUser;
import com.alten.repository.AppUserRepository;
import com.alten.service.AccountService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class AccountServiceImpl implements AccountService {

    private AppUserRepository appUserRepository;
    private PasswordEncoder passwordEncoder;

    public AccountServiceImpl(AppUserRepository appUserRepository, PasswordEncoder passwordEncoder) {
        this.appUserRepository = appUserRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public AppUser addNewUser(AppUser appUser) {
        String password = appUser.getPassword();
        appUser.setPassword(passwordEncoder.encode(password));
        return appUserRepository.save(appUser);
    }

    /*@Override
    public AppUser loadUserByUsername(String email) {
        return appUserRepository.findByUsername(email);
    }*/

    @Override
    public AppUser loadUserByEmail(String email) {
        return appUserRepository.findByEmail(email);
    }

    @Override
    public List<AppUser> listUsers() {
        return appUserRepository.findAll();
    }
}
