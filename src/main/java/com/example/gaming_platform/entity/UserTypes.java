package com.example.gaming_platform.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

@Entity
public class UserTypes {
    @Id
    @GeneratedValue
    private Long id;

    private String type;

    private String description;
}
