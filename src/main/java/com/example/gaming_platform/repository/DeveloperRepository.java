package com.example.gaming_platform.repository;

import com.example.gaming_platform.entity.Developer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.List;

public interface DeveloperRepository extends JpaRepository<Developer, Long> {

    Optional<Developer> findByUsername(String username);

    Optional<Developer> findByEmail(String email);

    List<Developer> findByUsernameContaining(String username);
}