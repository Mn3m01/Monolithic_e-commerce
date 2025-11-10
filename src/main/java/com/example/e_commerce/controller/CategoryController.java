package com.example.e_commerce.controller;

import com.example.e_commerce.Entity.Category;
import com.example.e_commerce.Service.CategoryService.ICategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/categories")
public class CategoryController {

    private final ICategoryService categoryService;

    @GetMapping
    public ResponseEntity<List<Category>> getAllCategories(){
        List<Category> categories = categoryService.getAllCategories();
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
