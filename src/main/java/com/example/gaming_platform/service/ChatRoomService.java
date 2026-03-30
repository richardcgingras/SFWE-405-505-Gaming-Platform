package com.example.gaming_platform.service;

import com.example.gaming_platform.entity.ChatRoom;
import com.example.gaming_platform.repository.ChatRoomRepository;
import org.springframework.stereotype.Service;
import java.util.Optional;

/**
 * Service for chat room business operations.
 */
@Service
public class ChatRoomService {

    private final ChatRoomRepository chatRoomRepository;

/**
 * Creates a new ChatRoomService instance.
 *
 * @param chatRoomRepository the chat room repository
 */
    public ChatRoomService(ChatRoomRepository chatRoomRepository) {
        this.chatRoomRepository = chatRoomRepository;
    }

/**
 * Gets the or create chat room ID.
 *
 * @param senderId the sender ID
 * @param recipientId the recipient ID
 * @return the or create chat room ID
 */
    public String getOrCreateChatRoomId(String senderId, String recipientId) {
        Optional<ChatRoom> existing = chatRoomRepository
                .findBySenderIdAndRecipientId(senderId, recipientId);
        if (existing.isPresent()) return existing.get().getChatRoomId();

        Optional<ChatRoom> reverse = chatRoomRepository
                .findBySenderIdAndRecipientId(recipientId, senderId);
        if (reverse.isPresent()) return reverse.get().getChatRoomId();

        String chatRoomId = senderId + "_" + recipientId;
        chatRoomRepository.save(new ChatRoom(chatRoomId, senderId, recipientId));
        return chatRoomId;
    }
}
