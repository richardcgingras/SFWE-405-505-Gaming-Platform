package com.example.gaming_platform.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.gaming_platform.entity.AuthResponse;
import com.example.gaming_platform.entity.LoginRequest;
import com.example.gaming_platform.repository.UserProfileRepository;
import com.example.gaming_platform.service.JwtTokenProvider;

/**
 * Authentication controller for user login and JWT token generation.
 */
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider tokenProvider;
    private final UserProfileRepository userProfileRepository;

    /**
     * Creates an AuthController instance with authentication manager and token provider.
     * @param authenticationManager the Spring Security authentication manager for validating credentials
     * @param tokenProvider the JWT token provider for generating tokens after successful authentication
     */
    public AuthController(AuthenticationManager authenticationManager,
                        JwtTokenProvider tokenProvider,
                        UserProfileRepository userProfileRepository) {
        this.authenticationManager = authenticationManager;
        this.tokenProvider = tokenProvider;
        this.userProfileRepository = userProfileRepository;
    }

    /**
     * Authenticates a user and generates a JWT token upon successful login.
     * 
     * @param loginRequest the login request containing username and password
     * @return ResponseEntity with authenticated user information including JWT token in AuthResponse format
     */
    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@RequestBody LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getUsername(),
                        loginRequest.getPassword()
                )
        );

        long userId = userProfileRepository.findByUserName(loginRequest.getUsername()).getId();

        String jwt = tokenProvider.generateToken(authentication);
        return ResponseEntity.ok(new AuthResponse(jwt, "Bearer", userId));
    }
}