package com.example.gaming_platform.repository;

import com.example.gaming_platform.entity.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

/**
 * Repository for persisting and querying chat room records.
 */
@Repository
public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {
/**
 * Finds by chat room ID.
 *
 * @param chatRoomId the chat room ID
 * @return the matching by chat room ID
 */
    Optional<ChatRoom> findByChatRoomId(String chatRoomId);
/**
 * Finds by sender ID and recipient ID.
 *
 * @param senderId the sender ID
 * @param recipientId the recipient ID
 * @return the matching by sender ID and recipient ID
 */
    Optional<ChatRoom> findBySenderIdAndRecipientId(String senderId, String recipientId);
}
