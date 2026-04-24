package com.example.gaming_platform.entity;

public class AuthResponse {
    private String accessToken;
    private String tokenType;
    private Long userId;

    public AuthResponse(String accessToken, String tokenType, Long userId) {
        this.accessToken = accessToken;
        this.tokenType = tokenType;
        this.userId = userId;
    }

    // Getters
    public String getAccessToken() { return accessToken; }
    public String getTokenType() { return tokenType; }
    public Long getUserId() { return userId; }
}