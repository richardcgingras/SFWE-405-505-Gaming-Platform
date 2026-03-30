package com.example.gaming_platform.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.example.gaming_platform.entity.Review;

/**
 * Repository for persisting and querying review records.
 */
public interface ReviewRepository extends CrudRepository<Review, Long> {
/**
 * Finds by game ID.
 *
 * @param gameId the game ID
 * @return the matching by game ID
 */
    List<Review> findByGameId(Long gameId);
}
