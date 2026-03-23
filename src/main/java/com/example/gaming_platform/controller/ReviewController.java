package com.example.gaming_platform.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.gaming_platform.entity.Review;
import com.example.gaming_platform.service.ReviewService;

@RestController
@RequestMapping("/api/reviews")
@CrossOrigin(origins = "*")
public class ReviewController {
    
    private final ReviewService reviewService;

    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @PostMapping("/submit")
    public ResponseEntity<Review> submitReview(
            @RequestParam Long userId, 
            @RequestParam Long gameId, 
            @RequestParam String comments, 
            @RequestParam int rating) {
        
        Review saved = reviewService.submitReview(userId, gameId, comments, rating);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @GetMapping("/game/{gameId}")
    public Iterable<Review> getReviewsByGame(@PathVariable Long gameId) {
        return reviewService.getReviewsByGame(gameId);
    }
}