package com.gbesh.first.service.category;

import com.gbesh.first.model.Category;

import java.util.List;

public interface ICategoryService {
    Category getCategoryById(Long id);
    Category getCategoryByName(String name);
    List<Category> getAllCategories();
    Category addCategory(Category category);
    Category updateCategory(String name, Long id);
    void deleteCategoryById(Long id);
}
