package com.example.gaming_platform.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.gaming_platform.entity.VideoGame;
import com.example.gaming_platform.repository.VideoGameRepository;

@RestController
@RequestMapping("/api/video-games")
@CrossOrigin(origins = "*") // helpful for Postman + later UI
public class VideoGameController {

    private final VideoGameRepository videoGameRepository;

    public VideoGameController(VideoGameRepository videoGameRepository) {
        this.videoGameRepository = videoGameRepository;
    }

    // GET /api/video-games
    @GetMapping
    public Iterable<VideoGame> getAllVideoGames() {
        return videoGameRepository.findAll();
    }

    // GET /api/video-games/{id}
    @GetMapping("/{id}")
    public ResponseEntity<VideoGame> getVideoGameById(@PathVariable Long id) {
        return videoGameRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // POST /api/video-games
    @PostMapping(consumes = "application/json")
    public ResponseEntity<VideoGame> createVideoGame(@RequestBody VideoGame videoGame) {
        VideoGame saved = videoGameRepository.save(videoGame);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }
}
