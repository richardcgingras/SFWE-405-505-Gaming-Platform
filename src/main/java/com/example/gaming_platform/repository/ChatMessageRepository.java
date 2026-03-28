package com.example.gaming_platform.repository;

import com.example.gaming_platform.entity.ChatMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {
    List<ChatMessage> findByChatRoomIdOrderByTimestampAsc(String chatRoomId);
    long countByChatRoomIdAndRecipientIdAndStatus(
        String chatRoomId, String recipientId, ChatMessage.MessageStatus status
    );
}