package com.example.gaming_platform.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.gaming_platform.entity.Category;
import com.example.gaming_platform.service.CategoryService;

/**
 * REST controller for category operations.
 */
@RestController
@RequestMapping("/api/category")
public class CategoryController {

    private final CategoryService categoryService;

/**
 * Creates a new CategoryController instance.
 *
 * @param categoryService the category service
 */
    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

/**
 * Retrieves all categories.
 *
 * @return all categories
 */
    @GetMapping
    public Iterable<Category> getAllCategories() {
        return categoryService.getAllCategories();
    }

/**
 * Retrieves a category by ID.
 *
 * @param id the ID
 * @return the matching category when found
 */
    @GetMapping("/{id}")
    public ResponseEntity<Category> getCategoryById(@PathVariable Long id) {
        return categoryService.getCategoryById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

/**
 * Creates a new category.
 *
 * @param category the category
 * @return the created category
 */
    @PostMapping(consumes = "application/json")
    public ResponseEntity<Category> createCategory(@RequestBody Category category) {
        Category saved = categoryService.createCategory(category);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }
}
