package com.alten.service;

import com.alten.entities.Product;

import java.util.List;

public interface ProductService {
    Product addProduct(Product product);
    List<Product> listProducts();
    Product getProductById(Long id);
    void deleteProductById(Long id);
}