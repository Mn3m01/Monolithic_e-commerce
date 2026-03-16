package com.example.e_commerce.controller;

import com.example.e_commerce.entity.Category;
import com.example.e_commerce.service.CategoryService.CategoryService;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/categories")
public class CategoryController {

    private final CategoryService categoryService;

    @GetMapping
    public ResponseEntity<Page<Category>> getAllCategories(Pageable pageable) {
        Page<Category> categories = categoryService.getAllCategories(pageable);
        return ResponseEntity.ok(categories);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Category> getCategoryById(@PathVariable Long id){
        return ResponseEntity.ok(categoryService.getCategoryById(id));
    }


    @GetMapping("/search/{name}")
    public ResponseEntity<Category> getCategoryByName(@PathVariable String name){
        return ResponseEntity.ok(categoryService.getCategoryByName(name));
    }


    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteCategoryById(@PathVariable Long id){
        categoryService.deleteCategoryById(id);
        return ResponseEntity.ok("Categroy deleted succesfully");
    }


    @DeleteMapping("/{name}")
    public ResponseEntity<String> deleteCategoryById(@PathVariable String name){
        categoryService.deleteCategoryByName(name);
        return ResponseEntity.ok(("Categroy deleted succesfully"));
    }


    @PostMapping
    public Category addCategory(@RequestBody Category category){
        return categoryService.addCategory(category);
    }


    @PutMapping("/{id}")
    public Category updateCategory(@PathVariable Long id ,@RequestBody Category category){
        return categoryService.updateCategory(id,category);
    }





}
