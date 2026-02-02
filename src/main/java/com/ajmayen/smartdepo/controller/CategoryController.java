package com.ajmayen.smartdepo.controller;

import com.ajmayen.smartdepo.dto.CategoryRequest;
import com.ajmayen.smartdepo.model.Category;
import com.ajmayen.smartdepo.service.CategoryService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/categories")
public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @PostMapping
    public Category createCategory(@RequestBody CategoryRequest request) {
        return categoryService.createCategory(request.getName());
    }

    @GetMapping
    public List<Category> getAllCategories() {
        return categoryService.getAllCategories();
    }


}
