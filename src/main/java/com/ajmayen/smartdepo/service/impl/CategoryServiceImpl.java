package com.ajmayen.smartdepo.service.impl;

import com.ajmayen.smartdepo.model.Category;
import com.ajmayen.smartdepo.repository.CategoryRepository;
import com.ajmayen.smartdepo.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    public CategoryServiceImpl(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    public Category createCategory(String name) {
        categoryRepository.findByName(name).ifPresent(c -> {
            throw new RuntimeException("Category already exists!");
        });

        Category category = Category.builder()
                .name(name)
                .build();

        return categoryRepository.save(category);
    }

    @Override
    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }
}
