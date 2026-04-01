package com.example.gaming_platform.controller;
import com.example.gaming_platform.entity.ChatMessage;
import com.example.gaming_platform.service.ChatMessageService;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;
import java.util.List;

/**
 * REST controller for chat operations.
 */
@RestController
public class ChatController {

    private final SimpMessagingTemplate messagingTemplate;
    private final ChatMessageService chatMessageService;

/**
 * Creates a new ChatController instance.
 *
 * @param messagingTemplate the messaging template
 * @param chatMessageService the chat message service
 */
    public ChatController(SimpMessagingTemplate messagingTemplate,
                          ChatMessageService chatMessageService) {
        this.messagingTemplate = messagingTemplate;
        this.chatMessageService = chatMessageService;
    }

/**
 * Processes message.
 *
 * @param chatMessage the chat message
 *  This method is weboscket pushing a reveciving in real time
 */
    @MessageMapping("/chat")
    public void processMessage(@Payload ChatMessage chatMessage) {
        ChatMessage saved = chatMessageService.save(chatMessage);
        messagingTemplate.convertAndSendToUser(
                saved.getRecipientId(), "/queue/messages", saved
        );
    }

/**
 * Finds chat messages.
 *
 * @param senderId the sender ID
 * @param recipientId the recipient ID
 * @return the matching chat messages
 */
    @GetMapping("/api/messages/{senderId}/{recipientId}")
    public ResponseEntity<List<ChatMessage>> findChatMessages(
            @PathVariable String senderId,
            @PathVariable String recipientId) {
        return ResponseEntity.ok(chatMessageService.findChatMessages(senderId, recipientId));
    }

/**
 * Counts new messages.
 *
 * @param senderId the sender ID
 * @param recipientId the recipient ID
 * @return the matching count
 */
    @GetMapping("/api/messages/{senderId}/{recipientId}/count")
    public ResponseEntity<Long> countNewMessages(
            @PathVariable String senderId,
            @PathVariable String recipientId) {
        return ResponseEntity.ok(chatMessageService.countNewMessages(senderId, recipientId));
    }
}
