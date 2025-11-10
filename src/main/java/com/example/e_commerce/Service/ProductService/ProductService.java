package com.example.e_commerce.Service.ProductService;

import com.example.e_commerce.Entity.Product;
import com.example.e_commerce.DTO.ProductDTO;

import java.util.List;

public interface ProductService {

    Product addProduct(ProductDTO productDTO);

    Product updateProduct(Long id , Product product);

    void deleteProductById(Long id);

    Product getProductById(Long id);

    List<Product> getAllProducts();

    List<Product> getProductByName(String name);

    List<Product> getProductByCategory(String categoryName);

    void deleteByName(String name);
}
