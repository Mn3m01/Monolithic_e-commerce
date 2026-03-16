package com.example.e_commerce.service.ProductService;


import com.example.e_commerce.dto.product.ProductDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ProductService {

    // Create
    ProductDTO addProduct(ProductDTO dto);

    // Update
    ProductDTO updateProduct(Long id, ProductDTO dto);

    // Delete
    void deleteProductById(Long id);

    // Read
    ProductDTO getProductById(Long id);

    Page<ProductDTO> getAllProducts(Pageable pageable);

    List<ProductDTO> getProductsByName(String name);

    List<ProductDTO> getProductsByCategory(String categoryName);

}
