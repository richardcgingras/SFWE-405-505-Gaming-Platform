package com.example.gaming_platform.entity;

import java.util.Date;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;

@Entity
public class Orders {
    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    private UserProfile destinationAccount;

    @ManyToOne
    private VideoGame game;

    private Date purchaseTimestamp;

    private Boolean paymentProcessed;
}
