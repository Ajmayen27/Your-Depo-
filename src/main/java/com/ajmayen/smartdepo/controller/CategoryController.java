package com.ajmayen.smartdepo.controller;

import com.ajmayen.smartdepo.dto.CategoryRequest;
import com.ajmayen.smartdepo.model.Category;
import com.ajmayen.smartdepo.repository.CategoryRepository;
import com.ajmayen.smartdepo.service.CategoryService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/categories")
public class CategoryController {

    private final CategoryService categoryService;
    private final CategoryRepository categoryRepository;

    public CategoryController(CategoryService categoryService, CategoryRepository categoryRepository) {
        this.categoryService = categoryService;
        this.categoryRepository = categoryRepository;
    }

    @PostMapping
    public Category createCategory(@RequestBody CategoryRequest request) {
        return categoryService.createCategory(request.getName());
    }

    @GetMapping
    public List<Category> getAllCategories() {
        return categoryService.getAllCategories();
    }


    @PutMapping("/{id}")
    public Category updateCategory(@PathVariable Long id, @RequestBody CategoryRequest request) {

        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Category not found"));

        category.setName(request.getName());
        return categoryRepository.save(category);
    }

    @DeleteMapping("/{id}")
    public String deleteCategory(@PathVariable Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Category not found"));
        categoryRepository.delete(category);
        return  "Category deleted successfully";
    }
}
