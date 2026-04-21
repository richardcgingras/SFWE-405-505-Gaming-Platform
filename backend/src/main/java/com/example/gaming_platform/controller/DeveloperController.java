package com.example.gaming_platform.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.gaming_platform.entity.Developer;
import com.example.gaming_platform.entity.VideoGame;
import com.example.gaming_platform.repository.DeveloperRepository;
import com.example.gaming_platform.repository.VideoGameRepository;

/**
 * REST controller for developer operations.
 */
@RestController
@RequestMapping("/api/developers")
public class DeveloperController {

    private final DeveloperRepository developerRepository;
    private final VideoGameRepository videoGameRepository;

/**
 * Creates a new DeveloperController instance.
 *
 * @param developerRepository the developer repository
 * @param videoGameRepository the video game repository
 */
    public DeveloperController(DeveloperRepository developerRepository,
                               VideoGameRepository videoGameRepository) {
        this.developerRepository = developerRepository;
        this.videoGameRepository = videoGameRepository;
    }

    // GET /api/developers
/**
 * Retrieves all developers.
 *
 * @return all developers
 */
    @GetMapping
    public Iterable<Developer> getAllDevelopers() {
        return developerRepository.findAll();
    }

    // GET /api/developers/{id}
/**
 * Retrieves a developer by ID.
 *
 * @param id the ID
 * @return the matching developer when found
 */
    @GetMapping("/{id}")
    public ResponseEntity<Developer> getDeveloperById(@PathVariable Long id) {
        return developerRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // POST /api/developers
/**
 * Creates a new developer.
 *
 * @param developer the developer
 * @return the created developer
 */
    @PostMapping(consumes = "application/json")
    public ResponseEntity<Developer> createDeveloper(@RequestBody Developer developer) {
        Developer saved = developerRepository.save(developer);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    // POST /api/developers/{developerId}/games/{gameId}
/**
 * Adds game to developer.
 *
 * @param developerId the developer ID
 * @param gameId the game ID
 * @return the updated result
 */
    @PostMapping("/{developerId}/games/{gameId}")
    public ResponseEntity<Developer> addGameToDeveloper(@PathVariable Long developerId,
                                                        @PathVariable Long gameId) {

        return developerRepository.findById(developerId)
                .map(developer -> {
                    return videoGameRepository.findById(gameId)
                            .map(game -> {
                                // Set publisher relationship
                                game.setPublisher(developer);
                                videoGameRepository.save(game);
                                return ResponseEntity.ok(developer);
                            })
                            .orElse(ResponseEntity.notFound().build());
                })
                .orElse(ResponseEntity.notFound().build());
    }

    // GET /api/developers/{developerId}/games
/**
 * Gets the developer games.
 *
 * @param developerId the developer ID
 * @return the developer games
 */
    @GetMapping("/{developerId}/games")
    public ResponseEntity<List<VideoGame>> getDeveloperGames(@PathVariable Long developerId) {
        return developerRepository.findById(developerId)
                .map(developer -> ResponseEntity.ok(developer.getPublishedGames()))
                .orElse(ResponseEntity.notFound().build());
    }
}
