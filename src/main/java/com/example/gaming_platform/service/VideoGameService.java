package com.example.gaming_platform.service;

import org.springframework.stereotype.Service;

import com.example.gaming_platform.entity.*;
import com.example.gaming_platform.repository.VideoGameRepository;

import java.util.List;
import java.util.ArrayList;

@Service
public class VideoGameService {

    private VideoGameRepository videoGameRepository;

    public VideoGameService(VideoGameRepository videoGameRepository) {
        this.videoGameRepository = videoGameRepository;
    }

    public VideoGame getGame(Long id) {
        return videoGameRepository.findById(id).orElseThrow(() -> new RuntimeException("Game not found"));
    }

    public VideoGame saveGame(VideoGame videoGame) {
        return videoGameRepository.save(videoGame);
    }

    public void deleteGame(Long id) {
        videoGameRepository.deleteById(id);
    }

    //Category
    public VideoGame addCategory(Long gameId, Category category) {
        VideoGame videoGame = getGame(gameId);

        List<Category> categories = videoGame.getCategory();
        if (categories == null) {
            categories = new ArrayList<>();
        }
        if (!categories.contains(category)) {
            categories.add(category);
        }
        videoGame.setCategory(categories);
        return videoGameRepository.save(videoGame);
    }

    public VideoGame removeCategory(Long gameId, Category category) {
        VideoGame videoGame = getGame(gameId);

        List<Category> categories = videoGame.getCategory();
        if (categories != null) {
            categories.remove(category);
        }
        return videoGameRepository.save(videoGame);
    }

    public boolean hasCategory(Long gameId, Category category) {
        VideoGame videoGame = getGame(gameId);
        return videoGame.getCategory() != null && videoGame.getCategory().contains(category);
    }

    //Device
    public VideoGame addSupportedSystem(Long gameId, Device device) {
        VideoGame videoGame = getGame(gameId);

        List<Device> systems = videoGame.getSystem();
        if (systems == null) {
            systems = new ArrayList<>();
        }
        if (!systems.contains(device)) {
            systems.add(device);
        }
        videoGame.setSystem(systems);
        return videoGameRepository.save(videoGame);
    }

    public VideoGame removeSupportedSystem(Long gameId, Device device) {
        VideoGame videoGame = getGame(gameId);

        List<Device> systems = videoGame.getSystem();
        if (systems != null) {
            systems.remove(device);
        }
        return videoGameRepository.save(videoGame);
    }

    //Reviews
    public VideoGame addReview(Long gameId, Integer review) {
        VideoGame videoGame = getGame(gameId);

        List<Integer> reviews = videoGame.getReviews();
        if (reviews == null) {
            reviews = new ArrayList<>();
        }
        reviews.add(review);
        videoGame.setReviews(reviews);

        return videoGameRepository.save(videoGame);
    }

    public VideoGame removeReview(Long gameId, Integer review) {
        VideoGame videoGame = getGame(gameId);

        List<Integer> reviews = videoGame.getReviews();
        if (reviews != null) {
            reviews.remove(review);
        }

        return videoGameRepository.save(videoGame);
    }

    public double getAverageRating(Long gameId) {
        VideoGame videoGame = getGame(gameId);

        List<Integer> reviews = videoGame.getReviews();
        if (reviews == null || reviews.isEmpty()) {
            return 0.0;
        }

        double total = 0.0;
        for (Integer review : reviews) {
            total += review;
        }

        return total / reviews.size();
    }
}

