package com.example.gaming_platform.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

/**
 * Entity representing user types data.
 */
@Entity
public class UserTypes {
    @Id
    @GeneratedValue
    private Long id;

    private String type;

    private String description;
}
