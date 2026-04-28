package com.example.gaming_platform.entity;

/**
 * DTO for password reset requests.
 * Carries the username and the desired new password.
 */
public class PasswordResetRequest {

    private String username;
    private String newPassword;

    public PasswordResetRequest() {}

    public PasswordResetRequest(String username, String newPassword) {
        this.username = username;
        this.newPassword = newPassword;
    }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getNewPassword() { return newPassword; }
    public void setNewPassword(String newPassword) { this.newPassword = newPassword; }
}
