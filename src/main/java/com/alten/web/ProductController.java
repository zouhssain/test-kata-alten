package com.alten.web;

import com.alten.entities.Product;
import com.alten.service.ProductService;
import com.alten.utils.ControllerConst;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/products")
public class ProductController {
    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    public List<Product> getProducts() {
        return productService.listProducts();
    }

    @GetMapping("/{id}")
    public Product getProductById(@PathVariable Long id) {
        return productService.getProductById(id);
    }

    @PostMapping
    public ResponseEntity<?> createProduct(@RequestBody Product product, Principal principal) {
        if (!isAdmin(principal)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(ControllerConst.ACCESS_DENIED);
        }
        return ResponseEntity.ok(productService.addProduct(product));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<?> updateProduct(@PathVariable Long id, @RequestBody Product product, Principal principal) {
        if (!isAdmin(principal)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(ControllerConst.ACCESS_DENIED);
        }
        Product existingProduct = productService.getProductById(id);
        if (existingProduct == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ControllerConst.PRODUCT_NOT_FOUND);
        }
        product.setId(id);
        return ResponseEntity.ok(productService.addProduct(product));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteProduct(@PathVariable Long id, Principal principal) {
        if (!isAdmin(principal)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(ControllerConst.ACCESS_DENIED);
        }
        productService.deleteProductById(id);
        return ResponseEntity.ok(ControllerConst.PRODUCT_DELETED_SUCCESSFULLY);
    }

    private boolean isAdmin(Principal principal) {
        return principal != null && ControllerConst.ADMIN.equalsIgnoreCase(principal.getName());
    }


}