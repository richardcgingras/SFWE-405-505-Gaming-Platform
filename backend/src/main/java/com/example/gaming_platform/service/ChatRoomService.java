package com.example.gaming_platform.service;

import com.example.gaming_platform.entity.ChatRoom;
import com.example.gaming_platform.repository.ChatRoomRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Optional;

@Service
public class ChatRoomService {

    private final ChatRoomRepository chatRoomRepository;

    public ChatRoomService(ChatRoomRepository chatRoomRepository) {
        this.chatRoomRepository = chatRoomRepository;
    }

    @Transactional
    public String getOrCreateChatRoomId(String senderId, String recipientId) {
        // Check both directions first
        Optional<ChatRoom> existing = chatRoomRepository
                .findBySenderIdAndRecipientId(senderId, recipientId);
        if (existing.isPresent()) return existing.get().getChatRoomId();

        Optional<ChatRoom> reverse = chatRoomRepository
                .findBySenderIdAndRecipientId(recipientId, senderId);
        if (reverse.isPresent()) return reverse.get().getChatRoomId();

        // Try to create, catch duplicate if another request beat us to it
        try {
            String chatRoomId = senderId + "_" + recipientId;
            chatRoomRepository.save(new ChatRoom(chatRoomId, senderId, recipientId));
            return chatRoomId;
        } catch (Exception e) {
            // Another request already created it, just look it up
            return chatRoomRepository
                    .findBySenderIdAndRecipientId(senderId, recipientId)
                    .or(() -> chatRoomRepository.findBySenderIdAndRecipientId(recipientId, senderId))
                    .map(ChatRoom::getChatRoomId)
                    .orElseThrow(() -> new RuntimeException("Could not find or create chat room"));
        }
    }
}