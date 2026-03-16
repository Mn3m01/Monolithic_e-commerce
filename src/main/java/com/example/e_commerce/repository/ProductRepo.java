package com.example.e_commerce.repository;


import com.example.e_commerce.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface ProductRepo extends JpaRepository<Product,Long>, JpaSpecificationExecutor<Product> {

    List<Product> findByCategory_Name(String categoryName);

    List<Product> findByNameContainingIgnoreCase(String keyword);

    List<Product> findByCategory_NameOrderByPriceAsc(String categoryName);

    Page<Product> findAll(Pageable pageable);
    void deleteAllByName(String name);
    Page<Product> findByNameContainingIgnoreCaseAndCategory_Name(
            String name,
            String categoryName,
            Pageable pageable
    );
}
