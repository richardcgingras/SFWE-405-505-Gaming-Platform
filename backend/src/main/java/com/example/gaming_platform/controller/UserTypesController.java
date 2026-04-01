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

import com.example.gaming_platform.entity.UserTypes;
import com.example.gaming_platform.repository.UserTypesRepository;

/**
 * REST controller for user types operations.
 */
@RestController
@RequestMapping("/api/user-types")
@CrossOrigin(origins = "*")
public class UserTypesController {

    private final UserTypesRepository userTypesRepository;

/**
 * Creates a new UserTypesController instance.
 *
 * @param userTypesRepository the user types repository
 */
    public UserTypesController(UserTypesRepository userTypesRepository) {
        this.userTypesRepository = userTypesRepository;
    }

    // GET /api/user-types
/**
 * Retrieves all user types.
 *
 * @return all user types
 */
    @GetMapping
    public Iterable<UserTypes> getAllUserTypes() {
        return userTypesRepository.findAll();
    }

    // GET /api/user-types/{id}
/**
 * Retrieves a user types by ID.
 *
 * @param id the ID
 * @return the matching user types when found
 */
    @GetMapping("/{id}")
    public ResponseEntity<UserTypes> getUserTypesById(@PathVariable Long id) {
        return userTypesRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // POST /api/user-types
/**
 * Creates a new user types.
 *
 * @param userTypes the user types
 * @return the created user types
 */
    @PostMapping(consumes = "application/json")
    public ResponseEntity<UserTypes> createUserTypes(@RequestBody UserTypes userTypes) {
        UserTypes saved = userTypesRepository.save(userTypes);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }
}
