package com.example.gaming_platform.service;

import com.example.gaming_platform.entity.Developer;
import com.example.gaming_platform.entity.VideoGame;
import com.example.gaming_platform.repository.DeveloperRepository;
import com.example.gaming_platform.repository.VideoGameRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DeveloperService {

    private final DeveloperRepository developerRepository;
    private final VideoGameRepository videoGameRepository;

    public DeveloperService(DeveloperRepository developerRepository, VideoGameRepository videoGameRepository) {
        this.developerRepository = developerRepository;
        this.videoGameRepository = videoGameRepository;
    }

    //Add/Remove Developers
    public Developer addDeveloper(Developer developer) {
        return developerRepository.save(developer);
    }
    public void deleteDeveloper(Developer developer) {
        developerRepository.delete(developer);
    }

    //Published Games
    public void addPublishedGame(Long developerId, Long gameId) {
        Developer developer = developerRepository.findById(developerId)
                .orElseThrow(() -> new RuntimeException("Developer not found"));

        VideoGame game = videoGameRepository.findById(gameId)
                .orElseThrow(() -> new RuntimeException("Game not found"));

        // set relationship
        game.setPublisher(developer);

        videoGameRepository.save(game);
    }

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

    public List<VideoGame> getPublishedGames(Long developerId) {
        Developer developer = developerRepository.findById(developerId)
                .orElseThrow(() -> new RuntimeException("Developer not found"));

        return developer.getPublishedGames();
    }
}
