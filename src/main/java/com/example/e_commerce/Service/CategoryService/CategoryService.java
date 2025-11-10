package com.example.e_commerce.Service.CategoryService;

import com.example.e_commerce.Entity.Category;
import com.example.e_commerce.Repository.CategoryRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class CategoryService implements ICategoryService{

    private final CategoryRepo categoryRepo;

    @Override
    public Category addCategory(Category category) {
        return categoryRepo.save(category);
    }

    @Override
    public Category updateCategory(Long id, Category category) {
        Optional<Category> existing = categoryRepo.findById(id);
        if(existing.isPresent()){
            Category updatedCategory = existing.get();
            updatedCategory.setName(category.getName());


            return categoryRepo.save(updatedCategory);
        }
        throw new RuntimeException("Category not found with id : " + id);
    }

    @Override
    public List<Category> getAllCategories() {
        return categoryRepo.findAll();
    }

    @Override
    public Category getCategoryById(Long id) {

        return categoryRepo.findById(id).orElseThrow(()->new RuntimeException("Category not Found By id:" + id));
    }

    @Override
    public Category getCategoryByName(String name) {
        return categoryRepo.findByName(name)
                .orElseThrow(()-> new RuntimeException("Category not found with name : " + name));
    }

    @Override
    public void deleteCategoryById(Long id) {
        Category category = categoryRepo.findById(id)
                .orElseThrow(()-> new RuntimeException("Category not found with id : "+id));
        categoryRepo.delete(category);
    }

    @Transactional
    @Override
    public void deleteCategoryByName(String name) {
        Category category = categoryRepo.findByName(name)
                .orElseThrow(()-> new RuntimeException("Category not found with name : "+ name));
        categoryRepo.deleteByName(name);
    }
}
