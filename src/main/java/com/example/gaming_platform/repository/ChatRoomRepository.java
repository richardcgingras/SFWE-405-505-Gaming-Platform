package com.example.gaming_platform.repository;

import com.example.gaming_platform.entity.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {
    Optional<ChatRoom> findByChatRoomId(String chatRoomId);
    Optional<ChatRoom> findBySenderIdAndRecipientId(String senderId, String recipientId);
}