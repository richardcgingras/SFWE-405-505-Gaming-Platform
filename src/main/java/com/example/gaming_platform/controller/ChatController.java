package com.example.gaming_platform.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.gaming_platform.entity.Chat;
import com.example.gaming_platform.repository.ChatRepository;

@RestController
@RequestMapping("/api/chat")
@CrossOrigin(origins = "*")
public class ChatController {

    private final ChatRepository chatRepository;

    public ChatController(ChatRepository chatRepository) {
        this.chatRepository = chatRepository;
    }

    // GET /api/chat
    @GetMapping
    public Iterable<Chat> getAllChat() {
        return chatRepository.findAll();
    }

    // GET /api/chat/{id}
    @GetMapping("/{id}")
    public ResponseEntity<Chat> getChatById(@PathVariable Long id) {
        return chatRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // POST /api/chat
    @PostMapping(consumes = "application/json")
    public ResponseEntity<Chat> createChat(@RequestBody Chat chat) {
        Chat saved = chatRepository.save(chat);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }
}
