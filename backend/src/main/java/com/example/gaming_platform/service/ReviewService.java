package com.example.gaming_platform.service;

import org.springframework.stereotype.Service;
import com.example.gaming_platform.entity.Review;
import com.example.gaming_platform.entity.UserProfile;
import com.example.gaming_platform.entity.VideoGame;
import com.example.gaming_platform.repository.ReviewRepository;
import com.example.gaming_platform.repository.UserProfileRepository;
import com.example.gaming_platform.repository.VideoGameRepository;

/**
 * Service for review business operations.
 */
@Service
public class ReviewService {
    private final ReviewRepository reviewRepo;
    private final UserProfileRepository userProfileRepo;
    private final VideoGameRepository videoGameRepo;

/**
 * Creates a new ReviewService instance.
 *
 * @param reviewRepo the review repo
 * @param userProfileRepo the user profile repo
 * @param videoGameRepo the video game repo
 */
    public ReviewService(ReviewRepository reviewRepo, UserProfileRepository userProfileRepo, VideoGameRepository videoGameRepo) {
        this.reviewRepo = reviewRepo;
        this.userProfileRepo = userProfileRepo;
        this.videoGameRepo = videoGameRepo;
    }

    // submit a review
/**
 * Executes the submitReview operation.
 *
 * @param userId the user ID
 * @param gameId the game ID
 * @param comments the comments
 * @param rating the rating
 * @return the result of the operation
 */
    public Review submitReview(Long userId, Long gameId, String comments, int rating) {
        UserProfile user = userProfileRepo.findById(userId).orElseThrow();
        VideoGame game = videoGameRepo.findById(gameId).orElseThrow();

        Review review = new Review(user, game, comments, rating);
        return reviewRepo.save(review);
    }

/**
 * Gets the reviews by game.
 *
 * @param gameId the game ID
 * @return the reviews by game
 */
    public Iterable<Review> getReviewsByGame(Long gameId) {
        return reviewRepo.findByGameId(gameId);
    }
}
