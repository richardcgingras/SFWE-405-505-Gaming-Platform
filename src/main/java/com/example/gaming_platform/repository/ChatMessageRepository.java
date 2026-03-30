package com.example.gaming_platform.repository;

import com.example.gaming_platform.entity.ChatMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

/**
 * Repository for persisting and querying chat message records.
 */
@Repository
public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {
/**
 * Finds by chat room ID order by timestamp asc.
 *
 * @param chatRoomId the chat room ID
 * @return the matching by chat room ID order by timestamp asc
 */
    List<ChatMessage> findByChatRoomIdOrderByTimestampAsc(String chatRoomId);
/**
 * Counts by chat room ID and recipient ID and status.
 *
 * @param chatRoomId the chat room ID
 * @param recipientId the recipient ID
 * @param status the status
 * @return the matching count
 */
    long countByChatRoomIdAndRecipientIdAndStatus(
        String chatRoomId, String recipientId, ChatMessage.MessageStatus status
    );
}
