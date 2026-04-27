package com.example.gaming_platform.service;

import org.springframework.stereotype.Service;

import com.example.gaming_platform.entity.GameLibrary;
import com.example.gaming_platform.entity.UserProfile;
import com.example.gaming_platform.entity.VideoGame;
import com.example.gaming_platform.repository.GameLibraryRepository;
import com.example.gaming_platform.repository.VideoGameRepository;

/**
 * Service for game library business operations.
 */
@Service
public class GameLibraryService {

    private GameLibraryRepository gameLibraryRepository;
    private VideoGameRepository gameRepository;

/**
 * Creates a new GameLibraryService instance.
 *
 * @param gameLibraryRepository the game library repository
 * @param gameRepository the game repository
 */
    public GameLibraryService(GameLibraryRepository gameLibraryRepository,
                              VideoGameRepository gameRepository) {
        this.gameLibraryRepository = gameLibraryRepository;
        this.gameRepository = gameRepository;
    }

/**
 * Creates a new library.
 *
 * @param gameLibrary the game library
 * @return the created library
 */
    public GameLibrary createLibrary(GameLibrary gameLibrary) {
        gameLibrary.calculateTotalSize();
        return gameLibraryRepository.save(gameLibrary);
    }

/**
 * Adds game.
 *
 * @param libraryId the library ID
 * @param gameId the game ID
 * @return the updated result
 */
    public GameLibrary addGame(Long libraryId, Long gameId) {
        GameLibrary library = gameLibraryRepository.findById(libraryId).orElse(null);
        VideoGame game = gameRepository.findById(gameId).orElse(null);

        if (library != null && game != null) {
            library.addGame(game);
            return gameLibraryRepository.save(library);
        }

        return null;
    }

/**
 * Removes game.
 *
 * @param libraryId the library ID
 * @param gameId the game ID
 * @return the updated result
 */
    public GameLibrary removeGame(Long libraryId, Long gameId) {
        GameLibrary library = gameLibraryRepository.findById(libraryId).orElse(null);
        VideoGame game = gameRepository.findById(gameId).orElse(null);

        if (library != null && game != null) {
            library.removeGame(game);
            return gameLibraryRepository.save(library);
        }

        return null;
    }

/**
 * Checks whether the game exists.
 *
 * @param libraryId the library ID
 * @param gameId the game ID
 * @return {@code true} when the game exists
 */
    public Boolean hasGame(Long libraryId, Long gameId) {
        GameLibrary library = gameLibraryRepository.findById(libraryId).orElse(null);
        VideoGame game = gameRepository.findById(gameId).orElse(null);

        if (library != null && game != null) {
            return library.hasGame(game);
        }

        return null;
    }

/**
 * Gets the total size.
 *
 * @param id the ID
 * @return the total size
 */
    public Float getTotalSize(Long id) {
        GameLibrary library = gameLibraryRepository.findById(id).orElse(null);

        if (library != null) {
            return library.getTotalSize();
        }

        return null;
    }

/**
 * Deletes the library.
 *
 * @param id the ID
 * @return the result of the delete operation
 */
    public boolean deleteLibrary(Long id) {
        if (!gameLibraryRepository.existsById(id)) {
            return false;
        }
        gameLibraryRepository.deleteById(id);
        return true;
    }

/**
 * Finds by owner.
 *
 * @param owner the owner
 * @return the matching by owner
 */
    public GameLibrary findByOwner(UserProfile owner) {
        return gameLibraryRepository.findByOwner(owner);
    }
}
