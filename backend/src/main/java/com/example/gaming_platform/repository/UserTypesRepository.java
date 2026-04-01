package com.example.gaming_platform.repository;

import org.springframework.data.repository.CrudRepository;

import com.example.gaming_platform.entity.UserTypes;

/**
 * Repository for persisting and querying user types records.
 */
public interface UserTypesRepository extends CrudRepository<UserTypes, Long> {
/**
 * Finds by type.
 *
 * @param name the name
 * @return the matching by type
 */
    UserTypes findByType(String name);
}
