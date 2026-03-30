package com.example.gaming_platform.repository;

import com.example.gaming_platform.entity.UserProfile;
import org.springframework.data.repository.CrudRepository;

/**
 * Repository for persisting and querying user profile records.
 */
public interface UserProfileRepository extends CrudRepository<UserProfile, Long> {
/**
 * Finds by user name.
 *
 * @param name the name
 * @return the matching by user name
 */
    UserProfile findByUserName(String name);

/**
 * Finds by email.
 *
 * @param email the email
 * @return the matching by email
 */
    UserProfile findByEmail(String email);
}
