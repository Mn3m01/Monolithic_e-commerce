package com.example.e_commerce.Service.CategoryService;

import com.example.e_commerce.Entity.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;


import java.util.List;

public interface CategoryService {
    Category addCategory(Category category);
    Category updateCategory(Long id, Category category);
    Page<Category> getAllCategories(Pageable pageable);
    Category getCategoryById(Long id);
    Category getCategoryByName(String name);
    void deleteCategoryById(Long id);
    void deleteCategoryByName(String name);

}
