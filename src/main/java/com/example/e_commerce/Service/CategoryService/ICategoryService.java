package com.example.e_commerce.Service.CategoryService;

import com.example.e_commerce.Entity.Category;


import java.util.List;

public interface ICategoryService {
    Category addCategory(Category category);
    Category updateCategory(Long id, Category category);
    List<Category> getAllCategories();
    Category getCategoryById(Long id);
    Category getCategoryByName(String name);
    void deleteCategoryById(Long id);
    void deleteCategoryByName(String name);

}
