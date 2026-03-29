package com.example.gaming_platform.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "chat_rooms")
public class ChatRoom {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String chatRoomId;

    @Column(nullable = false)
    private String senderId;

    @Column(nullable = false)
    private String recipientId;

    public ChatRoom() {}

    public ChatRoom(String chatRoomId, String senderId, String recipientId) {
        this.chatRoomId = chatRoomId;
        this.senderId = senderId;
        this.recipientId = recipientId;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getChatRoomId() { return chatRoomId; }
    public void setChatRoomId(String chatRoomId) { this.chatRoomId = chatRoomId; }
    public String getSenderId() { return senderId; }
    public void setSenderId(String senderId) { this.senderId = senderId; }
    public String getRecipientId() { return recipientId; }
    public void setRecipientId(String recipientId) { this.recipientId = recipientId; }
}