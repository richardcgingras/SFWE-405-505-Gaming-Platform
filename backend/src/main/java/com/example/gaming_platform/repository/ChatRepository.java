package com.example.gaming_platform.repository;

import org.springframework.data.repository.CrudRepository;

import com.example.gaming_platform.entity.Chat;
import com.example.gaming_platform.entity.UserProfile;

/**
 * Repository for persisting and querying chat records.
 */
public interface ChatRepository extends CrudRepository<Chat, Long> {
/**
 * Finds by sender.
 *
 * @param name the name
 * @return the matching by sender
 */
    Chat findBySender(UserProfile name);
/**
 * Finds by receiver.
 *
 * @param name the name
 * @return the matching by receiver
 */
    Chat findByReceiver(UserProfile name);

}
