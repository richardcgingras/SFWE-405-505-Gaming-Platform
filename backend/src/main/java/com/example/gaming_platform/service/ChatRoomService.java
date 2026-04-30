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
        // Normalize the chatRoomId so both directions produce the same key
        String smaller = senderId.compareTo(recipientId) <= 0 ? senderId : recipientId;
        String larger  = senderId.compareTo(recipientId) <= 0 ? recipientId : senderId;
        String chatRoomId = smaller + "_" + larger;

        // Look up by the normalized chatRoomId
        Optional<ChatRoom> existing = chatRoomRepository.findByChatRoomId(chatRoomId);
        if (existing.isPresent()) {
            return existing.get().getChatRoomId();
        }

        // Create a new room with the normalized ID
        ChatRoom room = new ChatRoom(chatRoomId, senderId, recipientId);
        chatRoomRepository.save(room);
        return chatRoomId;
    }
}