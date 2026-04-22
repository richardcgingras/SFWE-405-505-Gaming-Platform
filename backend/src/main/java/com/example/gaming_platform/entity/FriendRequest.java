package com.example.gaming_platform.entity;

import java.time.LocalDateTime;
import jakarta.persistence.*;

/**
 * Represents a friend request between two users.
 */
@Entity
public class FriendRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // user who sends the request
    @ManyToOne(optional = false)
    private UserProfile sender;

    // user who receives the request
    @ManyToOne(optional = false)
    private UserProfile receiver;

    // status can be PENDING or ACCEPTED
    private String status;

    // when the request was created
    private LocalDateTime createdAt;

    public FriendRequest() {}

    /**
     * Constructor to create a new friend request.
     */
    public FriendRequest(UserProfile sender, UserProfile receiver, String status) {
        this.sender = sender;
        this.receiver = receiver;
        this.status = status;
        this.createdAt = LocalDateTime.now();
    }

    public Long getId() { return id; }
    public UserProfile getSender() { return sender; }
    public UserProfile getReceiver() { return receiver; }
    public String getStatus() { return status; }
    public LocalDateTime getCreatedAt() { return createdAt; }

    public void setId(Long id) { this.id = id; }
    public void setSender(UserProfile sender) { this.sender = sender; }
    public void setReceiver(UserProfile receiver) { this.receiver = receiver; }
    public void setStatus(String status) { this.status = status; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}