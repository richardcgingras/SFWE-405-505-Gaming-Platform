package com.example.gaming_platform.service;

import org.springframework.stereotype.Service;
import com.example.gaming_platform.entity.Review;
import com.example.gaming_platform.entity.UserProfile;
import com.example.gaming_platform.entity.VideoGame;
import com.example.gaming_platform.repository.ReviewRepository;
import com.example.gaming_platform.repository.UserProfileRepository;
import com.example.gaming_platform.repository.VideoGameRepository;
import java.time.LocalDateTime;

@Service
public class ReviewService {
    private final ReviewRepository reviewRepo;
    private final UserProfileRepository userProfileRepo;
    private final VideoGameRepository videoGameRepo;

    public ReviewService(ReviewRepository reviewRepo, UserProfileRepository userProfileRepo, VideoGameRepository videoGameRepo) {
        this.reviewRepo = reviewRepo;
        this.userProfileRepo = userProfileRepo;
        this.videoGameRepo = videoGameRepo;
    }

    // submit a review
    public Review submitReview(Long userId, Long gameId, String comments, int rating) {
        UserProfile user = userProfileRepo.findById(userId).orElseThrow();
        VideoGame game = videoGameRepo.findById(gameId).orElseThrow();

        Review review = new Review(user, game, comments, rating);
        return reviewRepo.save(review);
    }

    public Iterable<Review> getReviewsByGame(Long gameId) {
        return reviewRepo.findByGameId(gameId);
    }
}