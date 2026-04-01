package com.example.gaming_platform.service;

import com.example.gaming_platform.entity.Developer;
import com.example.gaming_platform.entity.VideoGame;
import com.example.gaming_platform.repository.DeveloperRepository;
import com.example.gaming_platform.repository.VideoGameRepository;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service for developer business operations.
 */
@Service
public class DeveloperService {

    private final DeveloperRepository developerRepository;
    private final VideoGameRepository videoGameRepository;

/**
 * Creates a new DeveloperService instance.
 *
 * @param developerRepository the developer repository
 * @param videoGameRepository the video game repository
 */
    public DeveloperService(DeveloperRepository developerRepository, VideoGameRepository videoGameRepository) {
        this.developerRepository = developerRepository;
        this.videoGameRepository = videoGameRepository;
    }

    //Add/Remove Developers
/**
 * Adds developer.
 *
 * @param developer the developer
 * @return the updated result
 */
    public Developer addDeveloper(Developer developer) {
        return developerRepository.save(developer);
    }
/**
 * Deletes the developer.
 *
 * @param developer the developer
 */
    public void deleteDeveloper(Developer developer) {
        developerRepository.delete(developer);
    }

    //Published Games
/**
 * Adds published game.
 *
 * @param developerId the developer ID
 * @param gameId the game ID
 */
    public void addPublishedGame(Long developerId, Long gameId) {
        Developer developer = developerRepository.findById(developerId)
                .orElseThrow(() -> new RuntimeException("Developer not found"));

        VideoGame game = videoGameRepository.findById(gameId)
                .orElseThrow(() -> new RuntimeException("Game not found"));

        // set relationship
        game.setPublisher(developer);

        videoGameRepository.save(game);
    }

/**
 * Removes published game.
 *
 * @param developerId the developer ID
 * @param gameId the game ID
 */
    public void removePublishedGame(Long developerId, Long gameId) {
        Developer developer = developerRepository.findById(developerId)
                .orElseThrow(() -> new RuntimeException("Developer not found"));

        VideoGame game = videoGameRepository.findById(gameId)
                .orElseThrow(() -> new RuntimeException("Game not found"));

        if (game.getPublisher() != null &&
                game.getPublisher().getId().equals(developer.getId())) {

            game.setPublisher(null);
            videoGameRepository.save(game);
        }
    }

/**
 * Gets the published games.
 *
 * @param developerId the developer ID
 * @return the published games
 */
    public List<VideoGame> getPublishedGames(Long developerId) {
        Developer developer = developerRepository.findById(developerId)
                .orElseThrow(() -> new RuntimeException("Developer not found"));

        return developer.getPublishedGames();
    }
}
