package com.example.gaming_platform.repository;

import com.example.gaming_platform.entity.WebStorePage;
import org.springframework.data.repository.CrudRepository;
//s
/**
 * Repository for persisting and querying web store records.
 */
public interface WebStoreRepository extends CrudRepository<WebStorePage, Long> {
}
