package com.example.gaming_platform.service;

import java.util.List;

import com.example.gaming_platform.entity.GameLibrary;
import com.example.gaming_platform.entity.UserProfile;
import com.example.gaming_platform.entity.VideoGame;
import com.example.gaming_platform.repository.GameLibraryRepository;
import com.example.gaming_platform.repository.VideoGameRepository;

import org.springframework.stereotype.Service;

@Service
public class GameLibraryService {

    private GameLibraryRepository gameLibraryRepository;
    private VideoGameRepository gameRepository;

    public GameLibraryService(GameLibraryRepository gameLibraryRepository,
                              VideoGameRepository gameRepository) {
        this.gameLibraryRepository = gameLibraryRepository;
        this.gameRepository = gameRepository;
    }

    public GameLibrary createLibrary(GameLibrary gameLibrary) {
        gameLibrary.calculateTotalSize();
        return gameLibraryRepository.save(gameLibrary);
    }

    public GameLibrary addGame(Long libraryId, Long gameId) {
        GameLibrary library = gameLibraryRepository.findById(libraryId).orElse(null);
        VideoGame game = gameRepository.findById(gameId).orElse(null);

        if (library != null && game != null) {
            library.addGame(game);
            return gameLibraryRepository.save(library);
        }

        return null;
    }

    public GameLibrary removeGame(Long libraryId, Long gameId) {
        GameLibrary library = gameLibraryRepository.findById(libraryId).orElse(null);
        VideoGame game = gameRepository.findById(gameId).orElse(null);

        if (library != null && game != null) {
            library.removeGame(game);
            return gameLibraryRepository.save(library);
        }

        return null;
    }

    public Boolean hasGame(Long libraryId, Long gameId) {
        GameLibrary library = gameLibraryRepository.findById(libraryId).orElse(null);
        VideoGame game = gameRepository.findById(gameId).orElse(null);

        if (library != null && game != null) {
            return library.hasGame(game);
        }

        return null;
    }

    public Float getTotalSize(Long id) {
        GameLibrary library = gameLibraryRepository.findById(id).orElse(null);

        if (library != null) {
            return library.getTotalSize();
        }

        return null;
    }

    public boolean deleteLibrary(Long id) {
        if (!gameLibraryRepository.existsById(id)) {
            return false;
        }
        gameLibraryRepository.deleteById(id);
        return true;
    }

    public GameLibrary findByOwner(UserProfile owner) {
        return gameLibraryRepository.findByOwner(owner);
    }
}