package com.example.e_commerce.Repository;

import com.example.e_commerce.Entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepo extends JpaRepository<Product,Long> {

    List<Product> findByName(String name);
    List<Product> findByCategory_Name(String categoryName);
    List<Product> findByNameContainingIgnoreCase(String keyword);
    List<Product> findByCategoryNameOrderByPriceAsc(String categoryName);


    void deleteByName(String name);
}
