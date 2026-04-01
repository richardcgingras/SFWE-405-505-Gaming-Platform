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

/**
 * REST controller for user profile operations.
 */
@RestController
@RequestMapping("/api/user-profiles")
@CrossOrigin(origins = "*")
public class UserProfileController {

    private final UserProfileRepository userProfileRepository;

/**
 * Creates a new UserProfileController instance.
 *
 * @param userProfileRepository the user profile repository
 */
    public UserProfileController(UserProfileRepository userProfileRepository) {
        this.userProfileRepository = userProfileRepository;
    }

    // GET /api/user-profiles
/**
 * Retrieves all user profiles.
 *
 * @return all user profiles
 */
    @GetMapping
    public Iterable<UserProfile> getAllUserProfiles() {
        return userProfileRepository.findAll();
    }

    // GET /api/user-profiles/{id}
/**
 * Retrieves a user profile by ID.
 *
 * @param id the ID
 * @return the matching user profile when found
 */
    @GetMapping("/{id}")
    public ResponseEntity<UserProfile> getUserProfileById(@PathVariable Long id) {
        return userProfileRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // POST /api/user-profiles
/**
 * Creates a new user profile.
 *
 * @param userProfile the user profile
 * @return the created user profile
 */
    @PostMapping(consumes = "application/json")
    public ResponseEntity<UserProfile> createUserProfile(@RequestBody UserProfile userProfile) {
        UserProfile saved = userProfileRepository.save(userProfile);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    // GET user profile by username
    @GetMapping("/username/{userName}")
    public ResponseEntity<UserProfile> getUserProfileByUserName(@PathVariable String userName) {
        UserProfile user = userProfileRepository.findByUserName(userName);
        if (user == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(user);
    }

    // GET user profile by email
    @GetMapping("/email/{email}")
    public ResponseEntity<UserProfile> getUserProfileByEmail(@PathVariable String email) {
        UserProfile user = userProfileRepository.findByEmail(email);
        if (user == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(user);
    }

}
