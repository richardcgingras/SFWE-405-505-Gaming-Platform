package com.example.gaming_platform.repository;

import java.util.Calendar;
import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.example.gaming_platform.entity.Category;
import com.example.gaming_platform.entity.VideoGame;

/**
 * Repository for persisting and querying video game records.
 */
public interface VideoGameRepository extends CrudRepository<VideoGame, Long> {
/**
 * Finds by name.
 *
 * @param name the name
 * @return the matching by name
 */
    VideoGame findByName(String name);

/**
 * Finds by release date before.
 *
 * @param releaseDate the release date
 * @return the matching by release date before
 */
    VideoGame findByReleaseDateBefore(Calendar releaseDate);

/**
 * Finds by release date after.
 *
 * @param releaseDate the release date
 * @return the matching by release date after
 */
    VideoGame findByReleaseDateAfter(Calendar releaseDate);

/**
 * Finds all by category.
 *
 * @param category the category
 * @return the matching all by category
 */
    List<VideoGame> findAllByCategory(Category category);
}
