package com.example.gaming_platform.repository;

import com.example.gaming_platform.entity.Developer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.List;

/**
 * Repository for persisting and querying developer records.
 */
public interface DeveloperRepository extends JpaRepository<Developer, Long> {

/**
 * Finds by username.
 *
 * @param username the username
 * @return the matching by username
 */
    Optional<Developer> findByUsername(String username);

/**
 * Finds by email.
 *
 * @param email the email
 * @return the matching by email
 */
    Optional<Developer> findByEmail(String email);

/**
 * Finds by username containing.
 *
 * @param username the username
 * @return the matching by username containing
 */
    List<Developer> findByUsernameContaining(String username);
}
