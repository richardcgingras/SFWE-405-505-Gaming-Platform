package com.example.gaming_platform.entity;

import java.util.Date;

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

    private Date sentTimestamp;

    private Date readTimestamp;
}
