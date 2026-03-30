package com.example.gaming_platform.service;

import com.example.gaming_platform.entity.ChatMessage;
import com.example.gaming_platform.repository.ChatMessageRepository;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Service for chat message business operations.
 */
@Service
public class ChatMessageService {

    private final ChatMessageRepository chatMessageRepository;
    private final ChatRoomService chatRoomService;

/**
 * Creates a new ChatMessageService instance.
 *
 * @param chatMessageRepository the chat message repository
 * @param chatRoomService the chat room service
 */
    public ChatMessageService(ChatMessageRepository chatMessageRepository,
                               ChatRoomService chatRoomService) {
        this.chatMessageRepository = chatMessageRepository;
        this.chatRoomService = chatRoomService;
    }

/**
 * Saves the record.
 *
 * @param message the message
 * @return the saved record
 */
    public ChatMessage save(ChatMessage message) {
        String chatRoomId = chatRoomService.getOrCreateChatRoomId(
                message.getSenderId(), message.getRecipientId()
        );
        message.setChatRoomId(chatRoomId);
        message.setTimestamp(LocalDateTime.now());
        message.setStatus(ChatMessage.MessageStatus.SENT);
        return chatMessageRepository.save(message);
    }

/**
 * Finds chat messages.
 *
 * @param senderId the sender ID
 * @param recipientId the recipient ID
 * @return the matching chat messages
 */
    public List<ChatMessage> findChatMessages(String senderId, String recipientId) {
        String chatRoomId = chatRoomService.getOrCreateChatRoomId(senderId, recipientId);
        List<ChatMessage> messages = chatMessageRepository
                .findByChatRoomIdOrderByTimestampAsc(chatRoomId);
        messages.stream()
                .filter(m -> m.getRecipientId().equals(recipientId)
                        && m.getStatus() == ChatMessage.MessageStatus.SENT)
                .forEach(m -> {
                    m.setStatus(ChatMessage.MessageStatus.DELIVERED);
                    chatMessageRepository.save(m);
                });
        return messages;
    }

/**
 * Counts new messages.
 *
 * @param senderId the sender ID
 * @param recipientId the recipient ID
 * @return the matching count
 */
    public long countNewMessages(String senderId, String recipientId) {
        String chatRoomId = chatRoomService.getOrCreateChatRoomId(senderId, recipientId);
        return chatMessageRepository.countByChatRoomIdAndRecipientIdAndStatus(
                chatRoomId, recipientId, ChatMessage.MessageStatus.SENT
        );
    }
}
