package com.example.gaming_platform.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.gaming_platform.entity.PasswordResetRequest;
import com.example.gaming_platform.entity.UserProfile;
import com.example.gaming_platform.repository.UserProfileRepository;
import com.example.gaming_platform.service.UserProfileService;
import com.example.gaming_platform.util.PasswordValidator;

/**
 * REST controller for user profile operations.
 */
@RestController
@RequestMapping("/api/user-profiles")
public class UserProfileController {

    private final UserProfileRepository userProfileRepository;
    private final UserProfileService userProfileService;
    private final PasswordEncoder passwordEncoder;

    /**
     * Creates a new UserProfileController instance.
     *
     * @param userProfileRepository the user profile repository
     * @param userProfileService the user profile service
     * @param passwordEncoder the BCrypt password encoder
     */
    public UserProfileController(UserProfileRepository userProfileRepository,
                                 UserProfileService userProfileService,
                                 PasswordEncoder passwordEncoder) {
        this.userProfileRepository = userProfileRepository;
        this.userProfileService = userProfileService;
        this.passwordEncoder = passwordEncoder;
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

        // Validate password against platform rules
        if (!PasswordValidator.isValid(userProfile.getPassword())) {
            return ResponseEntity.badRequest().body(PasswordValidator.requirementsMessage());
        }

        // Hash the password before persisting
        userProfile.setPassword(passwordEncoder.encode(userProfile.getPassword()));

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

    // add friend to user profile
    @PostMapping("/{id}/friends/{friendId}")
    public ResponseEntity<UserProfile> addFriendToUserProfile(@PathVariable Long id, @PathVariable Long friendId) {
        try {
            userProfileService.addFriend(id, friendId);
            return userProfileRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    /**
     * Resets a user's password.
     * Validates the new password against platform rules, then saves the BCrypt hash.
     *
     * @param request the reset request containing the username and new password
     * @return 200 OK on success, 400 on invalid password, 404 if user not found
     */
    @PutMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@RequestBody PasswordResetRequest request) {
        if (request.getUsername() == null || request.getUsername().trim().isEmpty()) {
            return ResponseEntity.badRequest().body("Username is required");
        }

        if (!PasswordValidator.isValid(request.getNewPassword())) {
            return ResponseEntity.badRequest().body(PasswordValidator.requirementsMessage());
        }

        UserProfile user = userProfileRepository.findByUserName(request.getUsername());
        if (user == null) {
            return ResponseEntity.notFound().build();
        }

        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userProfileRepository.save(user);
        return ResponseEntity.ok("Password reset successfully");
    }

    /**
     * Updates a user's bio.
     *
     * @param id the user id
     * @param bio the new bio text
     * @return 200 OK on success, or 400 if validation fails
     */
    @PutMapping("/{id}/bio")
    public ResponseEntity<?> updateBio(@PathVariable Long id, @RequestBody String bio) {
        try {
            userProfileService.editBio(id, bio);
            return ResponseEntity.ok().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
