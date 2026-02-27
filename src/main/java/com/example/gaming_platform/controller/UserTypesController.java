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

@RestController
@RequestMapping("/api/user-types")
@CrossOrigin(origins = "*")
public class UserTypesController {

    private final UserTypesRepository userTypesRepository;

    public UserTypesController(UserTypesRepository userTypesRepository) {
        this.userTypesRepository = userTypesRepository;
    }

    // GET /api/user-types
    @GetMapping
    public Iterable<UserTypes> getAllUserTypes() {
        return userTypesRepository.findAll();
    }

    // GET /api/user-types/{id}
    @GetMapping("/{id}")
    public ResponseEntity<UserTypes> getUserTypesById(@PathVariable Long id) {
        return userTypesRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // POST /api/user-types
    @PostMapping(consumes = "application/json")
    public ResponseEntity<UserTypes> createUserTypes(@RequestBody UserTypes userTypes) {
        UserTypes saved = userTypesRepository.save(userTypes);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }
}
