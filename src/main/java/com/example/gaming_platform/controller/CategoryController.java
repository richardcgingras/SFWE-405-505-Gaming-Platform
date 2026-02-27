package com.example.gaming_platform.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.gaming_platform.entity.Category;
import com.example.gaming_platform.repository.CategoryRepository;

@RestController
@RequestMapping("/api/category")
@CrossOrigin(origins = "*")
public class CategoryController {

    private final CategoryRepository categoryRepository;

    public CategoryController(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    // GET /api/category
    @GetMapping
    public Iterable<Category> getAllCategorys() {
        return categoryRepository.findAll();
    }

    // GET /api/category/{id}
    @GetMapping("/{id}")
    public ResponseEntity<Category> getCategoryById(@PathVariable Long id) {
        return categoryRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // POST /api/category
    @PostMapping(consumes = "application/json")
    public ResponseEntity<Category> createCategory(@RequestBody Category userTypes) {
        Category saved = categoryRepository.save(userTypes);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }
}
