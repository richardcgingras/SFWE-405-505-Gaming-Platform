package com.example.gaming_platform.controller;

import com.example.gaming_platform.entity.Developer;
import com.example.gaming_platform.entity.VideoGame;
import com.example.gaming_platform.repository.DeveloperRepository;
import com.example.gaming_platform.repository.VideoGameRepository;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/developers")
@CrossOrigin(origins = "*")
public class DeveloperController {

    private final DeveloperRepository developerRepository;
    private final VideoGameRepository videoGameRepository;

    public DeveloperController(DeveloperRepository developerRepository,
                               VideoGameRepository videoGameRepository) {
        this.developerRepository = developerRepository;
        this.videoGameRepository = videoGameRepository;
    }

    // GET /api/developers
    @GetMapping
    public Iterable<Developer> getAllDevelopers() {
        return developerRepository.findAll();
    }

    // GET /api/developers/{id}
    @GetMapping("/{id}")
    public ResponseEntity<Developer> getDeveloperById(@PathVariable Long id) {
        return developerRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // POST /api/developers
    @PostMapping(consumes = "application/json")
    public ResponseEntity<Developer> createDeveloper(@RequestBody Developer developer) {
        Developer saved = developerRepository.save(developer);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    // POST /api/developers/{developerId}/games/{gameId}
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
    @GetMapping("/{developerId}/games")
    public ResponseEntity<List<VideoGame>> getDeveloperGames(@PathVariable Long developerId) {
        return developerRepository.findById(developerId)
                .map(developer -> ResponseEntity.ok(developer.getPublishedGames()))
                .orElse(ResponseEntity.notFound().build());
    }
}