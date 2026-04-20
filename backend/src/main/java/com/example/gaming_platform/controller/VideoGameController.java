package com.example.gaming_platform.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.gaming_platform.entity.VideoGame;
import com.example.gaming_platform.repository.VideoGameRepository;

/**
 * REST controller for video game operations.
 */
@RestController
@RequestMapping("/api/video-games")
public class VideoGameController {

    private final VideoGameRepository videoGameRepository;

/**
 * Creates a new VideoGameController instance.
 *
 * @param videoGameRepository the video game repository
 */
    public VideoGameController(VideoGameRepository videoGameRepository) {
        this.videoGameRepository = videoGameRepository;
    }

    // GET /api/video-games
/**
 * Retrieves all video games.
 *
 * @return all video games
 */
    @GetMapping
    public Iterable<VideoGame> getAllVideoGames() {
        return videoGameRepository.findAll();
    }

    // GET /api/video-games/{id}
/**
 * Retrieves a video game by ID.
 *
 * @param id the ID
 * @return the matching video game when found
 */
    @GetMapping("/{id}")
    public ResponseEntity<VideoGame> getVideoGameById(@PathVariable Long id) {
        return videoGameRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // POST /api/video-games
/**
 * Creates a new video game.
 *
 * @param videoGame the video game
 * @return the created video game
 */
    @PostMapping(consumes = "application/json")
    public ResponseEntity<VideoGame> createVideoGame(@RequestBody VideoGame videoGame) {
        VideoGame saved = videoGameRepository.save(videoGame);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }
}
