package com.example.gaming_platform.controller;

import com.example.gaming_platform.entity.GameLibrary;
import com.example.gaming_platform.entity.VideoGame;
import com.example.gaming_platform.repository.GameLibraryRepository;
import com.example.gaming_platform.service.GameLibraryService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST controller for game library operations.
 */
@RestController
@RequestMapping("/api/gamelibrary")
@CrossOrigin(origins = "*")
public class GameLibraryController {

    private GameLibraryService gameLibraryService;
    private GameLibraryRepository gameLibraryRepository;

/**
 * Creates a new GameLibraryController instance.
 *
 * @param gameLibraryService the game library service
 * @param gameLibraryRepository the game library repository
 */
    public GameLibraryController(GameLibraryService gameLibraryService,
                                 GameLibraryRepository gameLibraryRepository) {
        this.gameLibraryService = gameLibraryService;
        this.gameLibraryRepository = gameLibraryRepository;
    }

    // GET /api/gamelibrary
/**
 * Retrieves all libraries.
 *
 * @return all libraries
 */
    @GetMapping
    public Iterable<GameLibrary> getAllLibraries() {
        return gameLibraryRepository.findAll();
    }

    // GET /api/gamelibrary/{id}
/**
 * Retrieves a library by ID.
 *
 * @param id the ID
 * @return the matching library when found
 */
    @GetMapping("/{id}")
    public ResponseEntity<GameLibrary> getLibraryById(@PathVariable Long id) {
        GameLibrary library = gameLibraryRepository.findById(id).orElse(null);

        if (library == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(library);
    }

    // POST /api/gamelibrary
/**
 * Creates a new library.
 *
 * @param gameLibrary the game library
 * @return the created library
 */
    @PostMapping
    public ResponseEntity<GameLibrary> createLibrary(@RequestBody GameLibrary gameLibrary) {
        GameLibrary newLibrary = gameLibraryService.createLibrary(gameLibrary);
        return ResponseEntity.status(HttpStatus.CREATED).body(newLibrary);
    }

    // GET /api/gamelibrary/{id}/games
/**
 * Gets the games.
 *
 * @param id the ID
 * @return the games
 */
    @GetMapping("/{id}/games")
    public ResponseEntity<List<VideoGame>> getGames(@PathVariable Long id) {
        GameLibrary library = gameLibraryRepository.findById(id).orElse(null);

        if (library == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(library.getGames());
    }

    // POST /api/gamelibrary/{id}/games/{gameId}
/**
 * Adds game.
 *
 * @param id the ID
 * @param gameId the game ID
 * @return the updated result
 */
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
/**
 * Removes game.
 *
 * @param id the ID
 * @param gameId the game ID
 * @return the updated result
 */
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
/**
 * Checks whether the game exists.
 *
 * @param id the ID
 * @param gameId the game ID
 * @return {@code true} when the game exists
 */
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
/**
 * Gets the total size.
 *
 * @param id the ID
 * @return the total size
 */
    @GetMapping("/{id}/totalsize")
    public ResponseEntity<Float> getTotalSize(@PathVariable Long id) {
        Float size = gameLibraryService.getTotalSize(id);

        if (size == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(size);
    }

    // DELETE /api/gamelibrary/{id}
/**
 * Deletes the library.
 *
 * @param id the ID
 * @return the result of the delete operation
 */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLibrary(@PathVariable Long id) {
        boolean deleted = gameLibraryService.deleteLibrary(id);

        if (!deleted) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.noContent().build();
    }
}
