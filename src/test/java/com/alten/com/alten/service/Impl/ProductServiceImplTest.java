package com.alten.com.alten.service.Impl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import com.alten.entities.Product;
import com.alten.entities.Product.InventoryStatus;
import com.alten.repository.ProductRepository;
import com.alten.service.Impl.ProductServiceImpl;

class ProductServiceImplTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductServiceImpl productService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testListProducts() {
        // Arrange
        List<Product> mockProducts = Arrays.asList(
                new Product(1L, "P001", "Product A", "Description A", "imageA.jpg", "Category A", 10.0, 100, "RefA", 1, InventoryStatus.INSTOCK, 5, new Date(), new Date()),
                new Product(2L, "P002", "Product B", "Description B", "imageB.jpg", "Category B", 20.0, 200, "RefB", 2, InventoryStatus.LOWSTOCK, 4, new Date(), new Date())
        );
        when(productRepository.findAll()).thenReturn(mockProducts);

        // Act
        List<Product> result = productService.listProducts();

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("Product A", result.get(0).getName());
        verify(productRepository, times(1)).findAll();
    }

    @Test
    void testGetProductById_Found() {
        // Arrange
        Product mockProduct = new Product(1L, "P001", "Product A", "Description A", "imageA.jpg", "Category A", 10.0, 100, "RefA", 1, InventoryStatus.INSTOCK, 5, new Date(), new Date());
        when(productRepository.findById(1L)).thenReturn(Optional.of(mockProduct));

        // Act
        Product result = productService.getProductById(1L);

        // Assert
        assertNotNull(result);
        assertEquals("Product A", result.getName());
        verify(productRepository, times(1)).findById(1L);
    }

    @Test
    void testGetProductById_NotFound() {
        // Arrange
        when(productRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(RuntimeException.class, () -> productService.getProductById(1L));
        verify(productRepository, times(1)).findById(1L);
    }

    @Test
    void testAddProduct() {
        // Arrange
        Product mockProduct = new Product(null, "P003", "Product C", "Description C", "imageC.jpg", "Category C", 15.0, 150, "RefC", 3, InventoryStatus.OUTOFSTOCK, 3, new Date(), new Date());
        Product savedProduct = new Product(3L, "P003", "Product C", "Description C", "imageC.jpg", "Category C", 15.0, 150, "RefC", 3, InventoryStatus.OUTOFSTOCK, 3, new Date(), new Date());
        when(productRepository.save(mockProduct)).thenReturn(savedProduct);

        // Act
        Product result = productService.addProduct(mockProduct);

        // Assert
        assertNotNull(result);
        assertEquals(3L, result.getId());
        assertEquals("Product C", result.getName());
        verify(productRepository, times(1)).save(mockProduct);
    }

    @Test
    void testDeleteProductById() {
        // Act
        productService.deleteProductById(1L);

        // Assert
        verify(productRepository, times(1)).deleteById(1L);
    }
}