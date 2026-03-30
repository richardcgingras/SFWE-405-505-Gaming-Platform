package com.example.gaming_platform.service;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.example.gaming_platform.entity.Category;
import com.example.gaming_platform.repository.CategoryRepository;

/**
 * Service for category business operations.
 */
@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;

/**
 * Creates a new CategoryService instance.
 *
 * @param categoryRepository the category repository
 */
    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

/**
 * Retrieves all categories.
 *
 * @return all categories
 */
    public Iterable<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

/**
 * Retrieves a category by ID.
 *
 * @param id the ID
 * @return the matching category when found
 */
    public Optional<Category> getCategoryById(Long id) {
        return categoryRepository.findById(id);
    }

/**
 * Creates a new category.
 *
 * @param category the category
 * @return the created category
 */
    public Category createCategory(Category category) {
        return categoryRepository.save(category);
    }
}
