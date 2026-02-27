package com.example.gaming_platform.repository;

import org.springframework.data.repository.CrudRepository;

import com.example.gaming_platform.entity.Category;

public interface CategoryRepository extends CrudRepository<Category, Long> {
    Category findByType(String name);
}
