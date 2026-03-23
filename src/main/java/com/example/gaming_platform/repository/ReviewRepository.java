package com.example.gaming_platform.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.example.gaming_platform.entity.Review;

public interface ReviewRepository extends CrudRepository<Review, Long> {
    List<Review> findByGameId(Long gameId);
}