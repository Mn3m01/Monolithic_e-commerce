package com.example.e_commerce.controller;

import com.example.e_commerce.Entity.Product;
import com.example.e_commerce.Service.ProductService.ProductService;
import com.example.e_commerce.DTO.ProductDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/products")
public class ProductController {

    private final ProductService productService;

    //Get all products
    @GetMapping
    public ResponseEntity<List<Product>> getAllProducts() {
        return ResponseEntity.ok(productService.getAllProducts());
    }

    //Get product by ID
    @GetMapping("/{id}")
    public Product getProductById(@PathVariable Long id) {
        return (productService.getProductById(id));
    }

    //Search by name
    @GetMapping("/search/{name}")
    public ResponseEntity<List<Product>> getProductByName(@PathVariable String name) {
        return ResponseEntity.ok(productService.getProductByName(name));
    }

    //Add new product
    @PostMapping
    public ResponseEntity<Product> addProduct(@RequestBody ProductDTO productDTO) {
        return ResponseEntity.ok(productService.addProduct(productDTO));
    }

    //Update product
    @PutMapping("/{id}")
    public ResponseEntity<Product> updateProduct(@PathVariable Long id, @RequestBody Product product) {
        return ResponseEntity.ok(productService.updateProduct(id, product));
    }

    //Delete by ID
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteProductById(@PathVariable Long id) {
        productService.deleteProductById(id);
        return ResponseEntity.ok("Product deleted successfully");
    }

    //Delete by name
    @DeleteMapping("/delete/{name}")
    public ResponseEntity<String> deleteProductByName(@PathVariable String name) {
        productService.deleteByName(name);
        return ResponseEntity.ok("Product deleted successfully by name");
    }
}
