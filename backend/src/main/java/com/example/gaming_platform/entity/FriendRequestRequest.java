package com.example.gaming_platform.entity;

/**
 * Request object used when sending a friend request.
 */
public class FriendRequestRequest {

    private Long senderId;
    private Long receiverId;

    public FriendRequestRequest() {}

    public Long getSenderId() { return senderId; }
    public Long getReceiverId() { return receiverId; }

    public void setSenderId(Long senderId) { this.senderId = senderId; }
    public void setReceiverId(Long receiverId) { this.receiverId = receiverId; }
}