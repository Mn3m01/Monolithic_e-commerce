package com.example.e_commerce.repository;
import com.example.e_commerce.entity.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CategoryRepo extends JpaRepository<Category,Long> {
    Optional<Category> findByName(String name);
    void deleteByName(String name);

    @Override
    Page<Category> findAll(Pageable pageable);
}
