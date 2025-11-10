package com.example.e_commerce.Repository;
import com.example.e_commerce.Entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CategoryRepo extends JpaRepository<Category,Long> {
    Optional<Category> findByName(String name);
    void deleteByName(String name);
}
