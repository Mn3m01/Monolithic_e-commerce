package com.example.e_commerce.controller;

import com.example.e_commerce.Service.ProductService.ProductService;
import com.example.e_commerce.DTO.ProductDTO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
    public ResponseEntity<Page<ProductDTO>> getAllProducts(Pageable pageable) {
        return ResponseEntity.ok(productService.getAllProducts(pageable));

    }

    //Get product by ID
    @GetMapping("/{id}")
    public ProductDTO getProductById(@PathVariable Long id) {
        return (productService.getProductById(id));
    }

    //Search by name
    @GetMapping("/search/{name}")
    public ResponseEntity<List<ProductDTO>> getProductByName(@PathVariable String name) {
        return ResponseEntity.ok(productService.getProductsByName(name));
    }

    //Search by categoryName
    @GetMapping("/searchByCategory/{categoryName}")
    public ResponseEntity<List<ProductDTO>> getProductsByCategory(@PathVariable String categoryName){
        return ResponseEntity.ok(productService.getProductsByCategory(categoryName));
    }

    //Add new product
    @PostMapping
    public ResponseEntity<ProductDTO> addProduct(@Valid@RequestBody ProductDTO productDTO) {
        return ResponseEntity.ok(productService.addProduct(productDTO));
    }

    //Update product
    @PutMapping("/{id}")
    public ResponseEntity<ProductDTO> updateProduct(@PathVariable Long id, @RequestBody ProductDTO productDTO) {
        return ResponseEntity.ok(productService.updateProduct(id, productDTO));
    }

    //Delete by ID
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteProductById(@PathVariable Long id) {
        productService.deleteProductById(id);
        return ResponseEntity.ok("Product deleted successfully");
    }

    //Delete by name
    /*@DeleteMapping("/delete/{name}")
    public ResponseEntity<String> deleteProductByName(@PathVariable String name) {
        productService.deleteProductByName(name);
        return ResponseEntity.ok("Product deleted successfully by name");
    }*/
}
