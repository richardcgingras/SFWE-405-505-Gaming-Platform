package com.example.gaming_platform.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.gaming_platform.entity.UserProfile;
import com.example.gaming_platform.repository.UserProfileRepository;

@RestController
@RequestMapping("/api/user-profiles")
@CrossOrigin(origins = "*")
public class UserProfileController {

    private final UserProfileRepository userProfileRepository;

    public UserProfileController(UserProfileRepository userProfileRepository) {
        this.userProfileRepository = userProfileRepository;
    }

    // GET /api/user-profiles
    @GetMapping
    public Iterable<UserProfile> getAllUserProfiles() {
        return userProfileRepository.findAll();
    }

    // GET /api/user-profiles/{id}
    @GetMapping("/{id}")
    public ResponseEntity<UserProfile> getUserProfileById(@PathVariable Long id) {
        return userProfileRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // POST /api/user-profiles
    @PostMapping(consumes = "application/json")
    public ResponseEntity<UserProfile> createUserProfile(@RequestBody UserProfile userProfile) {
        UserProfile saved = userProfileRepository.save(userProfile);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }
}
