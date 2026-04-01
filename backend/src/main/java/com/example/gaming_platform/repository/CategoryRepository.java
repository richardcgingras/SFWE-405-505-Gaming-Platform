package com.example.gaming_platform.repository;

import org.springframework.data.repository.CrudRepository;

import com.example.gaming_platform.entity.Category;

/**
 * Repository for persisting and querying category records.
 */
public interface CategoryRepository extends CrudRepository<Category, Long> {
/**
 * Finds by type.
 *
 * @param type the type
 * @return the matching by type
 */
    Category findByType(String type);
}
