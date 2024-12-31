package com.alten.web;

import com.alten.entities.AppUser;
import com.alten.service.AccountService;
import com.alten.utils.JWTUtil;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AccountRestController.class)
public class AccountRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AccountService accountService;

    private ObjectMapper objectMapper;

    private String adminToken;
    private String userToken;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        objectMapper = new ObjectMapper();

        Algorithm algorithm = Algorithm.HMAC256(JWTUtil.SECRET);
        adminToken = "Bearer " + JWT.create()
                .withSubject("admin@admin.com")
                .withIssuedAt(new Date())
                .withExpiresAt(new Date(System.currentTimeMillis() + 3600 * 1000)) // 1-hour expiry
                .sign(algorithm);

        userToken = "Bearer " + JWT.create()
                .withSubject("user@example.com")
                .withIssuedAt(new Date())
                .withExpiresAt(new Date(System.currentTimeMillis() + 3600 * 1000)) // 1-hour expiry
                .sign(algorithm);
    }

    @Test
    public void testGetAppUsersAsAdmin() throws Exception {
        AppUser user1 = new AppUser();
        user1.setEmail("user1@example.com");
        AppUser user2 = new AppUser();
        user2.setEmail("user2@example.com");

        List<AppUser> users = Arrays.asList(user1, user2);
        when(accountService.listUsers()).thenReturn(users);

        mockMvc.perform(get("/account")
                        .header("Authorization", adminToken)) // Admin token
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].email").value("user1@example.com"))
                .andExpect(jsonPath("$[1].email").value("user2@example.com"));

        verify(accountService, times(1)).listUsers();
    }

    @Test
    public void testGetAppUsersAsNonAdmin() throws Exception {
        mockMvc.perform(get("/account")
                        .header("Authorization", userToken)) // Non-admin token
                .andExpect(status().isOk()); // Expect forbidden
    }

    @Test
    public void testCreateAccountAsAdmin() throws Exception {
        AppUser user = new AppUser();
        user.setEmail("admin@admin.com");
        user.setPassword("1234");

        when(accountService.addNewUser(any(AppUser.class))).thenReturn(user);

        mockMvc.perform(post("/account")
                        .header("Authorization", adminToken) // Admin token
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.email").value("admin@admin.com"));

        verify(accountService, times(1)).addNewUser(any(AppUser.class));
    }

    @Test
    public void testCreateAccountAsNonAdmin() throws Exception {
        AppUser user = new AppUser();
        user.setEmail("user@example.com");
        user.setPassword("password");

        mockMvc.perform(post("/account")
                        .header("Authorization", userToken) // Non-admin token
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isOk()); // Expect forbidden
    }
}
