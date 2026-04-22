package com.example.gaming_platform.service;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.example.gaming_platform.entity.Category;
import com.example.gaming_platform.entity.Device;
import com.example.gaming_platform.entity.GameLibrary;
import com.example.gaming_platform.entity.UserProfile;
import com.example.gaming_platform.entity.VideoGame;
import com.example.gaming_platform.repository.GameLibraryRepository;
import com.example.gaming_platform.repository.UserProfileRepository;
import com.example.gaming_platform.repository.VideoGameRepository;

/**
 * Service for video game business operations.
 */
@Service
public class VideoGameService {

    private final VideoGameRepository videoGameRepository;
    private final UserProfileRepository userProfileRepository;
    private final GameLibraryRepository gameLibraryRepository;

/**
 * Creates a new VideoGameService instance.
 *
 * @param videoGameRepository the video game repository
 */
    public VideoGameService(VideoGameRepository videoGameRepository,
                            UserProfileRepository userProfileRepository,
                            GameLibraryRepository gameLibraryRepository) {
        this.videoGameRepository = videoGameRepository;
        this.userProfileRepository = userProfileRepository;
        this.gameLibraryRepository = gameLibraryRepository;
    }

/**
 * Gets the game.
 *
 * @param id the ID
 * @return the game
 */
    public VideoGame getGame(Long id) {
        return videoGameRepository.findById(id).orElseThrow(() -> new RuntimeException("Game not found"));
    }

/**
 * Saves the game.
 *
 * @param videoGame the video game
 * @return the saved game
 */
    public VideoGame saveGame(VideoGame videoGame) {
        return videoGameRepository.save(videoGame);
    }

/**
 * Deletes the game.
 *
 * @param id the ID
 */
    public void deleteGame(Long id) {
        videoGameRepository.deleteById(id);
    }

    //Category
/**
 * Adds category.
 *
 * @param gameId the game ID
 * @param category the category
 * @return the updated result
 */
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

/**
 * Removes category.
 *
 * @param gameId the game ID
 * @param category the category
 * @return the updated result
 */
    public VideoGame removeCategory(Long gameId, Category category) {
        VideoGame videoGame = getGame(gameId);

        List<Category> categories = videoGame.getCategory();
        if (categories != null) {
            categories.remove(category);
        }
        return videoGameRepository.save(videoGame);
    }

/**
 * Checks whether the category exists.
 *
 * @param gameId the game ID
 * @param category the category
 * @return {@code true} when the category exists
 */
    public boolean hasCategory(Long gameId, Category category) {
        VideoGame videoGame = getGame(gameId);
        return videoGame.getCategory() != null && videoGame.getCategory().contains(category);
    }

    //Device
/**
 * Adds supported system.
 *
 * @param gameId the game ID
 * @param device the device
 * @return the updated result
 */
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

/**
 * Removes supported system.
 *
 * @param gameId the game ID
 * @param device the device
 * @return the updated result
 */
    public VideoGame removeSupportedSystem(Long gameId, Device device) {
        VideoGame videoGame = getGame(gameId);

        List<Device> systems = videoGame.getSystem();
        if (systems != null) {
            systems.remove(device);
        }
        return videoGameRepository.save(videoGame);
    }

    //Reviews
/**
 * Adds review.
 *
 * @param gameId the game ID
 * @param review the review
 * @return the updated result
 */
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

/**
 * Removes review.
 *
 * @param gameId the game ID
 * @param review the review
 * @return the updated result
 */
    public VideoGame removeReview(Long gameId, Integer review) {
        VideoGame videoGame = getGame(gameId);

        List<Integer> reviews = videoGame.getReviews();
        if (reviews != null) {
            reviews.remove(review);
        }

        return videoGameRepository.save(videoGame);
    }

/**
 * Gets the average rating.
 *
 * @param gameId the game ID
 * @return the average rating
 */
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

/**
 * Verifies if a file exists for the video game and provides it if it does.
 *
 * @param gameId the game ID
 * @param fileName the file name to check
 * @return String containing the file or an error
 */
    public byte[] downloadFile(Long gameId, String fileName, Long userId) throws Exception {
        VideoGame videoGame = getGame(gameId);
        UserProfile userProfile = userProfileRepository.findById(userId).get();
        GameLibrary gameLibrary = gameLibraryRepository.findByOwner(userProfile);
        Boolean owned = false;

        for (VideoGame currentGame : gameLibrary.getGames()){
            if (currentGame.getId() == gameId){
                // User owns game, exit check and continue
                owned = true;
                break;
            }
        }

        if (!owned){
            throw new RuntimeException("User does not own this game");
        }

        List<String> files = videoGame.getFiles();
        if (files == null) {
            throw new RuntimeException("Video Game has no files");
        }

        if (!files.contains(fileName)) {
            throw new RuntimeException("File not part of VideoGame: " + fileName);
        }

        return Files.readAllBytes(Paths.get("src/main/resources/public/"+fileName));
    }
}

