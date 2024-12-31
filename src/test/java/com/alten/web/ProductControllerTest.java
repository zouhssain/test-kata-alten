package com.alten.web;

import com.alten.entities.Product;
import com.alten.service.AccountService;
import com.alten.service.ProductService;
import com.alten.utils.ControllerConst;
import com.alten.utils.JWTUtil;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
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

@WebMvcTest(ProductController.class)
public class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AccountService accountService; // Mock explicite pour résoudre la dépendance

    @MockBean
    private ProductService productService; // Mock explicite pour le service produit

    private ObjectMapper objectMapper;

    private String adminToken;
    private String userToken;

    @BeforeEach
    public void setUp() {
        objectMapper = new ObjectMapper();
        Algorithm algorithm = Algorithm.HMAC256(JWTUtil.SECRET);

        adminToken = "Bearer " + JWT.create()
                .withSubject(ControllerConst.ADMIN)
                .withIssuedAt(new Date())
                .withExpiresAt(new Date(System.currentTimeMillis() + 3600 * 1000))
                .sign(algorithm);

        userToken = "Bearer " + JWT.create()
                .withSubject("user@example.com")
                .withIssuedAt(new Date())
                .withExpiresAt(new Date(System.currentTimeMillis() + 3600 * 1000))
                .sign(algorithm);
    }

    @Test
    public void testCreateProductAsAdmin() throws Exception {
        Product product = new Product();
        product.setId(1L);
        product.setName("Product 1");

        when(productService.addProduct(any(Product.class))).thenReturn(product);

        mockMvc.perform(post("/products")
                        .header("Authorization", adminToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(product)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Product 1"));

        verify(productService, times(1)).addProduct(any(Product.class));
    }

    @Test
    public void testGetProducts() throws Exception {
        Product product1 = new Product();
        product1.setId(1L);
        Product product2 = new Product();
        product2.setId(2L);

        List<Product> products = Arrays.asList(product1, product2);
        when(productService.listProducts()).thenReturn(products);

        mockMvc.perform(get("/products")
                .header("Authorization", adminToken))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[1].id").value(2));

        verify(productService, times(1)).listProducts();
    }

    @Test
    public void testCreateProductAsNonAdmin() throws Exception {
        Product product = new Product();
        product.setId(1L);
        product.setName("Product 1");

        mockMvc.perform(post("/products")
                        .header("Authorization", userToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(product)))
                .andExpect(status().isForbidden())
                .andExpect(content().string(ControllerConst.ACCESS_DENIED));
    }

    @Test
    public void testDeleteProductAsAdmin() throws Exception {
        doNothing().when(productService).deleteProductById(1L);

        mockMvc.perform(delete("/products/1")
                        .header("Authorization", adminToken))
                .andExpect(status().isOk())
                .andExpect(content().string(ControllerConst.PRODUCT_DELETED_SUCCESSFULLY));

        verify(productService, times(1)).deleteProductById(1L);
    }

    @Test
    public void testDeleteProductAsNonAdmin() throws Exception {
        mockMvc.perform(delete("/products/1")
                        .header("Authorization", userToken))
                .andExpect(status().isForbidden())
                .andExpect(content().string(ControllerConst.ACCESS_DENIED));
    }
}
