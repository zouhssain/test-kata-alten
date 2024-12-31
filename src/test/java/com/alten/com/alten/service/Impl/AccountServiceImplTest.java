package com.alten.com.alten.service.Impl;

import com.alten.entities.AppUser;
import com.alten.repository.AppUserRepository;
import com.alten.service.Impl.AccountServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class AccountServiceImplTest {

    @Mock
    private AppUserRepository appUserRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private AccountServiceImpl accountService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testAddNewUser() {
        AppUser user = new AppUser();
        user.setPassword("plainPassword");

        when(passwordEncoder.encode("plainPassword")).thenReturn("encodedPassword");
        when(appUserRepository.save(user)).thenReturn(user);

        AppUser result = accountService.addNewUser(user);

        assertNotNull(result);
        assertEquals("encodedPassword", result.getPassword());
        verify(passwordEncoder, times(1)).encode("plainPassword");
        verify(appUserRepository, times(1)).save(user);
    }

    @Test
    public void testLoadUserByEmail() {
        String email = "test@example.com";
        AppUser user = new AppUser();
        user.setEmail(email);

        when(appUserRepository.findByEmail(email)).thenReturn(user);

        AppUser result = accountService.loadUserByEmail(email);

        assertNotNull(result);
        assertEquals(email, result.getEmail());
        verify(appUserRepository, times(1)).findByEmail(email);
    }

    @Test
    public void testListUsers() {
        List<AppUser> users = new ArrayList<>();
        users.add(new AppUser());
        users.add(new AppUser());

        when(appUserRepository.findAll()).thenReturn(users);

        List<AppUser> result = accountService.listUsers();

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(appUserRepository, times(1)).findAll();
    }
}
