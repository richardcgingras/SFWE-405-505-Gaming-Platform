package com.example.gaming_platform.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.gaming_platform.entity.GameLibrary;
import com.example.gaming_platform.entity.UserProfile;
import com.example.gaming_platform.entity.VideoGame;
import com.example.gaming_platform.repository.GameLibraryRepository;
import com.example.gaming_platform.repository.UserProfileRepository;
import com.example.gaming_platform.repository.VideoGameRepository;
import com.example.gaming_platform.service.GameLibraryService;

/**
 * REST controller for game library operations.
 */
@RestController
@RequestMapping("/api/gamelibrary")
public class GameLibraryController {

    private GameLibraryService gameLibraryService;
    private GameLibraryRepository gameLibraryRepository;
    private UserProfileRepository userProfileRepository;
    private VideoGameRepository videoGameRepository;

/**
 * Creates a new GameLibraryController instance.
 *
 * @param gameLibraryService the game library service
 * @param gameLibraryRepository the game library repository
 * @param userProfileRepository the user profile repository
 * @param videoGameRepository the video game repository
 */
    public GameLibraryController(GameLibraryService gameLibraryService,
                                 GameLibraryRepository gameLibraryRepository,
                                 UserProfileRepository userProfileRepository,
                                 VideoGameRepository videoGameRepository) {
        this.gameLibraryService = gameLibraryService;
        this.gameLibraryRepository = gameLibraryRepository;
        this.userProfileRepository = userProfileRepository;
        this.videoGameRepository = videoGameRepository;
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
            library = new GameLibrary();
            UserProfile theUser = userProfileRepository.findById(id).get();
            library.setOwner(theUser);
            gameLibraryRepository.save(library);
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
            library = new GameLibrary();
            UserProfile theUser = userProfileRepository.findById(id).get();
            library.setOwner(theUser);
            gameLibraryRepository.save(library);
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
            library = new GameLibrary();
            UserProfile theUser = userProfileRepository.findById(id).get();
            library.setOwner(theUser);
            gameLibraryRepository.save(library);
        }

        return ResponseEntity.ok(library);
    }

    // POST /api/gamelibrary/user/{userId}/games/{gameId}
    /**
     * Adds a game to the library owned by the given user.
     * Auto-creates the library if one does not exist yet.
     *
     * @param userId the user (owner) ID
     * @param gameId the game ID
     * @return the updated library
     */
    @PostMapping("/user/{userId}/games/{gameId}")
    public ResponseEntity<GameLibrary> addGameByUserId(@PathVariable Long userId,
                                                       @PathVariable Long gameId) {
        UserProfile owner = userProfileRepository.findById(userId).orElse(null);
        if (owner == null) {
            return ResponseEntity.notFound().build();
        }

        GameLibrary library = gameLibraryRepository.findByOwner(owner);
        if (library == null) {
            library = new GameLibrary();
            library.setOwner(owner);
            library = gameLibraryRepository.save(library);
        }

        VideoGame game = videoGameRepository.findById(gameId).orElse(null);
        if (game == null) {
            return ResponseEntity.notFound().build();
        }

        library.addGame(game);
        return ResponseEntity.ok(gameLibraryRepository.save(library));
    }

    // GET /api/gamelibrary/user/{userId}/games
    /**
     * Returns the game list for the library owned by the given user.
     * Auto-creates an empty library if none exists.
     *
     * @param userId the user (owner) ID
     * @return list of games
     */
    @GetMapping("/user/{userId}/games")
    public ResponseEntity<List<VideoGame>> getGamesByUserId(@PathVariable Long userId) {
        UserProfile owner = userProfileRepository.findById(userId).orElse(null);
        if (owner == null) {
            return ResponseEntity.notFound().build();
        }

        GameLibrary library = gameLibraryRepository.findByOwner(owner);
        if (library == null) {
            library = new GameLibrary();
            library.setOwner(owner);
            library = gameLibraryRepository.save(library);
        }

        return ResponseEntity.ok(library.getGames());
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
            // Library does not exist yet, so the game does not exist in it
            result = false;
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
            // Library does not exist yet, so 0
            size = 0.0f;
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
