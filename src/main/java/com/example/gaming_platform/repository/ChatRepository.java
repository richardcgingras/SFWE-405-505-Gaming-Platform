package com.example.gaming_platform.repository;

import org.springframework.data.repository.CrudRepository;

import com.example.gaming_platform.entity.Chat;
import com.example.gaming_platform.entity.UserProfile;

public interface ChatRepository extends CrudRepository<Chat, Long> {
    Chat findBySender(UserProfile name);
    Chat findByReceiver(UserProfile name);

}
