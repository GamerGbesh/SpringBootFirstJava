package com.gbesh.first.service.category;

import com.gbesh.first.exceptions.AlreadyExistsException;
import com.gbesh.first.exceptions.ResourceNotFoundException;
import com.gbesh.first.model.Category;
import com.gbesh.first.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CategoryService implements ICategoryService{
    private final CategoryRepository categoryRepository;

    @Override
    public Category getCategoryById(Long id) {
        return categoryRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Category not found!")
        );
    }

    @Override
    public Category getCategoryByName(String name) {
        return Optional.ofNullable(categoryRepository.findByName(name))
                .orElseThrow(() -> new ResourceNotFoundException("Category not found"));
    }

    @Override
    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    @Override
    public Category addCategory(Category category) {
        return Optional.of(category).filter(c -> !categoryRepository.existsByName(c.getName()))
                .map(categoryRepository::save)
                .orElseThrow(() -> new AlreadyExistsException(category.getName() + " already exists!"));
    }

    @Override
    public Category updateCategory(String name, Long id) {
        return Optional.ofNullable(getCategoryById(id))
                .map(existingCategory -> {
                    existingCategory.setName(name);
                    return categoryRepository.save(existingCategory);
                })
                .orElseThrow(() -> new ResourceNotFoundException("Category not found!"));
    }

    @Override
    public void deleteCategoryById(Long id) {
        categoryRepository.findById(id).ifPresentOrElse(categoryRepository::delete,
                () -> {
                    throw new ResourceNotFoundException("Category not found!");
                });
    }
}
