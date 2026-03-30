package com.example.gaming_platform.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.gaming_platform.entity.Review;
import com.example.gaming_platform.service.ReviewService;

/**
 * REST controller for review operations.
 */
@RestController
@RequestMapping("/api/reviews")
@CrossOrigin(origins = "*")
public class ReviewController {
    
    private final ReviewService reviewService;

/**
 * Creates a new ReviewController instance.
 *
 * @param reviewService the review service
 */
    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

/**
 * Executes the submitReview operation.
 *
 * @param userId the user ID
 * @param gameId the game ID
 * @param comments the comments
 * @param rating the rating
 * @return the result of the operation
 */
    @PostMapping("/submit")
    public ResponseEntity<Review> submitReview(
            @RequestParam Long userId, 
            @RequestParam Long gameId, 
            @RequestParam String comments, 
            @RequestParam int rating) {
        
        Review saved = reviewService.submitReview(userId, gameId, comments, rating);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

/**
 * Gets the reviews by game.
 *
 * @param gameId the game ID
 * @return the reviews by game
 */
    @GetMapping("/game/{gameId}")
    public Iterable<Review> getReviewsByGame(@PathVariable Long gameId) {
        return reviewService.getReviewsByGame(gameId);
    }
}
