package com.example.gaming_platform.util;

import java.util.regex.Pattern;

/**
 * Validates passwords against platform security rules:
 * 8-12 characters, 1 uppercase, 1 number, 1 special character.
 */
public class PasswordValidator {

    private PasswordValidator() {}

    private static final Pattern PASSWORD_PATTERN = Pattern.compile(
        "^(?=.*[A-Z])(?=.*\\d)(?=.*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>\\/?]).{8,12}$"
    );

    public static boolean isValid(String password) {
        return password != null && PASSWORD_PATTERN.matcher(password).matches();
    }

    public static String requirementsMessage() {
        return "Password must be 8-12 characters and include at least one uppercase letter, one number, and one special character.";
    }
}
