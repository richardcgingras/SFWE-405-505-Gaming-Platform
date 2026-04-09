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

    /**
     * Returns all user profiles.
     *
     * @return all stored user profiles
     */
    @GetMapping
    public Iterable<UserProfile> getAllUserProfiles() {
        return userProfileRepository.findAll();
    }

    /**
     * Returns a user profile by id.
     *
     * @param id the profile id
     * @return the matching profile, or 404 if none exists
     */
    @GetMapping("/{id}")
    public ResponseEntity<UserProfile> getUserProfileById(@PathVariable Long id) {
        return userProfileRepository.findById(id)
            .map(user -> ResponseEntity.ok().body(user))
            .orElseGet(() -> ResponseEntity.notFound().build());
    }

    /**
     * Creates a new user profile.
     *
     * @param userProfile the user profile
     * @return the created profile, validation error, or conflict response
     */
    @PostMapping(consumes = "application/json")
    public ResponseEntity<?> createUserProfile(@RequestBody UserProfile userProfile) {
        if (userProfile.getEmail() == null || userProfile.getEmail().trim().isEmpty()) {
            return ResponseEntity.badRequest().body("Email is required");
        }

        if (userProfileRepository.findByEmail(userProfile.getEmail()) != null) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Email already in use");
        }

        if (userProfile.getUserName() == null || userProfile.getUserName().trim().isEmpty()) {
            userProfile.setUserName(userProfile.getEmail());
        }
        if (userProfile.getStatus() == null || userProfile.getStatus().trim().isEmpty()) {
            userProfile.setStatus("active");
        }

        UserProfile savedUserProfile = userProfileRepository.save(userProfile);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedUserProfile);
    }

    /**
     * Returns a user profile by username.
     *
     * @param userName the username
     * @return the matching profile, or 404 if none exists
     */
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
