package com.example.gaming_platform.repository;

import com.example.gaming_platform.entity.VideoGame;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Date;

public interface VideoGameRepository extends CrudRepository<VideoGame, Long> {
    VideoGame findByName(String name);

    VideoGame findByReleaseDateBefore(Date releaseDate);

    VideoGame findByReleaseDateAfter(Date releaseDate);

    List<VideoGame> findAllByCategory(Category category);
}
