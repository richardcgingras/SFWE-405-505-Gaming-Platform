package com.example.gaming_platform.entity;

import java.util.Calendar;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;

/**
 * Entity representing chat data.
 */
@Entity
public class Chat {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    private UserProfile sender;

    @OneToOne
    private UserProfile receiver;

    private String msg;

    private Calendar sentTimestamp;

    private Calendar readTimestamp;
}
