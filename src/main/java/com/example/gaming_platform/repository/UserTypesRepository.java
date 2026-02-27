package com.example.gaming_platform.repository;

import org.springframework.data.repository.CrudRepository;

import com.example.gaming_platform.entity.UserTypes;

public interface UserTypesRepository extends CrudRepository<UserTypes, Long> {
    UserTypes findByType(String name);
}
