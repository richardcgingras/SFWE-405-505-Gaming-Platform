package com.example.gaming_platform.controller;

import com.example.gaming_platform.entity.GameLibrary;
import com.example.gaming_platform.entity.VideoGame;
import com.example.gaming_platform.repository.GameLibraryRepository;
import com.example.gaming_platform.service.GameLibraryService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/gamelibrary")
@CrossOrigin(origins = "*")
public class GameLibraryController {

    private GameLibraryService gameLibraryService;
    private GameLibraryRepository gameLibraryRepository;

    public GameLibraryController(GameLibraryService gameLibraryService,
                                 GameLibraryRepository gameLibraryRepository) {
        this.gameLibraryService = gameLibraryService;
        this.gameLibraryRepository = gameLibraryRepository;
    }

    // GET /api/gamelibrary
    @GetMapping
    public Iterable<GameLibrary> getAllLibraries() {
        return gameLibraryRepository.findAll();
    }

    // GET /api/gamelibrary/{id}
    @GetMapping("/{id}")
    public ResponseEntity<GameLibrary> getLibraryById(@PathVariable Long id) {
        GameLibrary library = gameLibraryRepository.findById(id).orElse(null);

        if (library == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(library);
    }

    // POST /api/gamelibrary
    @PostMapping
    public ResponseEntity<GameLibrary> createLibrary(@RequestBody GameLibrary gameLibrary) {
        GameLibrary newLibrary = gameLibraryService.createLibrary(gameLibrary);
        return ResponseEntity.status(HttpStatus.CREATED).body(newLibrary);
    }

    // GET /api/gamelibrary/{id}/games
    @GetMapping("/{id}/games")
    public ResponseEntity<List<VideoGame>> getGames(@PathVariable Long id) {
        GameLibrary library = gameLibraryRepository.findById(id).orElse(null);

        if (library == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(library.getGames());
    }

    // POST /api/gamelibrary/{id}/games/{gameId}
    @PostMapping("/{id}/games/{gameId}")
    public ResponseEntity<GameLibrary> addGame(@PathVariable Long id,
                                               @PathVariable Long gameId) {
        GameLibrary library = gameLibraryService.addGame(id, gameId);

        if (library == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(library);
    }

    // DELETE /api/gamelibrary/{id}/games/{gameId}
    @DeleteMapping("/{id}/games/{gameId}")
    public ResponseEntity<GameLibrary> removeGame(@PathVariable Long id,
                                                  @PathVariable Long gameId) {
        GameLibrary library = gameLibraryService.removeGame(id, gameId);

        if (library == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(library);
    }

    // GET /api/gamelibrary/{id}/games/{gameId}/has
    @GetMapping("/{id}/games/{gameId}/has")
    public ResponseEntity<Boolean> hasGame(@PathVariable Long id,
                                           @PathVariable Long gameId) {
        Boolean result = gameLibraryService.hasGame(id, gameId);

        if (result == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(result);
    }

    // GET /api/gamelibrary/{id}/totalsize
    @GetMapping("/{id}/totalsize")
    public ResponseEntity<Float> getTotalSize(@PathVariable Long id) {
        Float size = gameLibraryService.getTotalSize(id);

        if (size == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(size);
    }

    // DELETE /api/gamelibrary/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLibrary(@PathVariable Long id) {
        boolean deleted = gameLibraryService.deleteLibrary(id);

        if (!deleted) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.noContent().build();
    }
}