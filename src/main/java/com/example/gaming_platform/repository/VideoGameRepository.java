package com.example.gaming_platform.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.example.gaming_platform.entity.Category;
import com.example.gaming_platform.entity.VideoGame;

public interface VideoGameRepository extends CrudRepository<VideoGame, Long> {
    VideoGame findByName(String name);

    VideoGame findByReleaseDateBefore(Date releaseDate);

    VideoGame findByReleaseDateAfter(Date releaseDate);

    List<VideoGame> findAllByCategory(Category category);
}
