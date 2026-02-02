package com.ajmayen.smartdepo.service;

import com.ajmayen.smartdepo.model.Category;

import java.util.List;

public interface CategoryService {

    Category createCategory(String name);

    List<Category> getAllCategories();
}
